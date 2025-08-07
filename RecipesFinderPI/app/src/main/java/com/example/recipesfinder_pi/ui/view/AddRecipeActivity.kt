package com.example.recipesfinder.ui.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore.Audio.Media
import android.provider.Settings
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.recipesfinder.R
import com.example.recipesfinder.data.model.FoodTypeModel
import com.example.recipesfinder.data.model.Recipe
import com.example.recipesfinder.data.model.Ingredient
import com.example.recipesfinder.data.model.UsedIngredient
import com.example.recipesfinder.databinding.AddRecipeActivityBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.viewmodel.FoodTypeViewModel
import com.example.recipesfinder.ui.viewmodel.RecipeViewModel
import com.example.recipesfinder.ui.viewmodel.IngredientViewModel
import com.example.recipesfinder.ui.viewmodel.UsedIngredientViewModel
import com.google.firebase.auth.*
import javax.annotation.meta.When

/**
 * Activity for adding a new recipe.
 *
 * This activity allows the user to enter recipe details, select an image for the dish
 * (not used because the lack of storage space, but with all function implemented),
 * add ingredients (with quantities), and save the recipe. It also handles permissions
 * for image selection from the gallery and uploading the selected image to Firebase Storage.
 *
 * The activity uses various ViewModels:
 * - [RecipeViewModel] for managing recipe data.
 * - [IngredientViewModel] for handling ingredient operations.
 * - [UsedIngredientViewModel] for associating ingredients with recipes.
 * - [FoodTypeViewModel] for managing food type data.
 *
 * It also uses ViewBinding ([AddRecipeActivityBinding]) for accessing UI elements.
 *
 * The activity implements [View.OnClickListener] to handle click events for certain views.
 *
 * @constructor Creates an instance of [AddRecipeActivity].
 */
class AddRecipeActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var binding: AddRecipeActivityBinding
    private lateinit var recipeVM: RecipeViewModel
    private lateinit var ingVM: IngredientViewModel
    private lateinit var usedIngVM: UsedIngredientViewModel
    private lateinit var foodListVM: FoodTypeViewModel
    private val PICK_IMAGE_PERMISSION_CODE = 1
    private var imageUri: Uri? = null
    private val auth = FirebaseAuth.getInstance()
    private val ingredients = mutableListOf<String>()
    private val qtds = mutableListOf<String>()

    private lateinit var dialog: AlertDialog

    companion object {
        /**
         * Permission constant for gallery access.
         *
         * Uses [Manifest.permission.READ_MEDIA_IMAGES] on Android Tiramisu (API 33) or higher,
         * and [Manifest.permission.READ_EXTERNAL_STORAGE] on lower API versions.
         */
        val PERMISSAO_GALERIA = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)
            Manifest.permission.READ_MEDIA_IMAGES
        else Manifest.permission.READ_EXTERNAL_STORAGE
    }

    /**
     * Registers for gallery permission request result.
     *
     * If permission is granted, launches an intent to pick an image from the gallery;
     * otherwise, shows a permission dialog.
     */
    private val requestGaleria =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { permissao ->
            if (permissao) {
                resultGaleria.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showDialogPermisson()
            }
        }

    /**
     * Registers for activity result for picking an image from the gallery.
     *
     * On success, retrieves the image URI, decodes the image into a Bitmap, and sets it on the ImageView.
     */
    private val resultGaleria =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let { uri ->
                // Assign the URI to the global variable imageUri.
                imageUri = uri

                // Convert URI to Bitmap and display it in the ImageView.
                val bitmap: Bitmap = if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(baseContext.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(this.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
                binding.imageDish.setImageBitmap(bitmap)
            } ?: run {
                Toast.makeText(this, R.string.no_image_selected, Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Called when the activity is starting.
     *
     * Inflates the layout, hides the action bar, sets up click listeners, initializes ViewModels,
     * initializes the food type spinner, and sets observers.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this contains the data it most recently supplied.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddRecipeActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.imageBack.setOnClickListener(this)
        binding.buttonRegister.setOnClickListener(this)
        binding.imageDish.setOnClickListener { selectImage() }
        binding.buttonAddIngredient.setOnClickListener {
            val ingredient = binding.editTextIngredient1.text.toString().trim()
            val qtd = binding.editTextQuantity1.text.toString().trim()
            if (ingredient.isNotEmpty() && qtd.isNotEmpty()) {
                ingredients.add(ingredient)
                qtds.add(qtd)
                Toast.makeText(this, getString(R.string.selected_ingredient_add, ingredient), Toast.LENGTH_SHORT).show()
                binding.editTextIngredient1.setText("")
                binding.editTextQuantity1.setText("")
            } else {
                Toast.makeText(this, R.string.type_ingredient, Toast.LENGTH_SHORT).show()
            }
        }

        recipeVM = ViewModelProvider(this).get(RecipeViewModel::class.java)
        ingVM = ViewModelProvider(this).get(IngredientViewModel::class.java)
        usedIngVM = ViewModelProvider(this).get(UsedIngredientViewModel::class.java)
        foodListVM = ViewModelProvider(this).get(FoodTypeViewModel::class.java)
        foodListVM.getAllFoodTypes()
        initFoodTypeSpinner()
        setObserver()
    }

    /**
     * Initializes the food type spinner with data from [FoodTypeViewModel].
     *
     * Observes the food type list, maps the food type names, and sets up an [ArrayAdapter] for the spinner.
     * Also registers a listener to handle selection events.
     */
    private fun initFoodTypeSpinner() {
        foodListVM.getFoodTypeList().observe(this, Observer { foodTypes ->
            // Map food type names from the list of FoodTypeModel.
            val foodTypeNames = foodTypes.map { it.name }
            val spinner: Spinner = binding.spinnerFoodType
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, foodTypeNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Listener for item selection.
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val selectedType = foodTypeNames[position]
                    Toast.makeText(
                        this@AddRecipeActivity,
                        getString(R.string.selected_type_add, selectedType),
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
        })
    }

    /**
     * Initiates image selection from the gallery.
     *
     * Checks for gallery permission; if granted, launches the gallery intent,
     * if not, shows a rationale dialog or requests permission.
     */
    private fun selectImage() {
        val permissaoGaleriaAceita = verificaPermissao(PERMISSAO_GALERIA)

        when {
            permissaoGaleriaAceita -> {
                resultGaleria.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            }
            shouldShowRequestPermissionRationale(PERMISSAO_GALERIA) -> showDialogPermisson()
            else -> requestGaleria.launch(PERMISSAO_GALERIA)
        }
    }

    /**
     * Shows a dialog explaining the need for gallery permission.
     *
     * If the user agrees, opens the application settings; if not, dismisses the dialog.
     */
    private fun showDialogPermisson() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.atencion)
            .setMessage(R.string.galery_msg)
            .setNegativeButton(R.string.no) { _, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(R.string.yes) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", packageName, null)
                )
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                dialog.dismiss()
            }

        dialog = builder.create()
        dialog.show()
    }

    /**
     * Checks if the specified permission is granted.
     *
     * @param permissao The permission to check.
     * @return True if the permission is granted, false otherwise.
     */
    private fun verificaPermissao(permissao: String) =
        ContextCompat.checkSelfPermission(this, permissao) == PackageManager.PERMISSION_GRANTED

    /**
     * Handles click events for views registered with this OnClickListener.
     *
     * - If the back image is clicked, the activity is finished.
     * - If the register button is clicked, the recipe data is validated and saved along with its ingredients.
     *
     * @param view The view that was clicked.
     */
    override fun onClick(view: View) {
        if (view.id == R.id.image_back) {
            finish()
        } else if (view.id == R.id.button_register) {
            try {
                val selectedFoodType = binding.spinnerFoodType.selectedItem.toString()
                val caloriesText = binding.etCalories.text.toString().trim()
                val calories = if (caloriesText.isNotEmpty()) caloriesText.toInt() else 0

                val r = Recipe().apply {
                    this.name = binding.editTextName.text.toString()
                    this.description = binding.editTextDescription.text.toString()
                    this.imageURL = selectedFoodType
                    this.method = binding.editTextPreparation.text.toString()
                    this.iduser = auth.currentUser?.uid.toString()
                    this.type = selectedFoodType
                    this.calories = calories
                }
                if (r.name.isEmpty()) {
                    Toast.makeText(this, R.string.empty_name_msg, Toast.LENGTH_SHORT).show()
                } else if (r.description.isEmpty()) {
                    Toast.makeText(this, R.string.empty_description_msg, Toast.LENGTH_SHORT).show()
                } else if (r.method.isEmpty()) {
                    Toast.makeText(this, R.string.empty_method_msg, Toast.LENGTH_SHORT).show()
                } else {
                    val recipeId = recipeVM.saveRecipe(r)
                    for (i in ingredients.indices) {
                        val ingredient = ingredients[i]
                        val qtd = qtds[i]

                        val ingredientObjt = Ingredient().apply {
                            this.name = ingredient
                        }
                        val ingredientId = ingVM.checkAndSaveIngredient(ingredientObjt)
                        val usedIngredient = UsedIngredient().apply {
                            this.idref = recipeId
                            this.iding = ingredientId
                            this.quantity = qtd
                        }
                        usedIngVM.saveUser(usedIngredient)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this, R.string.empty_number_msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Clears all input fields and resets the dish image to a placeholder.
     */
    private fun clearEdits() {
        binding.editTextName.text.clear()
        binding.editTextDescription.text.clear()
        binding.editTextPreparation.text.clear()
        binding.imageDish.setImageResource(R.drawable.ic_image_placeholder)
        imageUri = null
    }

    /**
     * Uploads an image to Firebase Storage.
     *
     * This method uploads the image at the specified [uri] to Firebase Storage.
     * Upon success, it retrieves the download URL and passes it to the [onComplete] callback.
     *
     * @param uri The [Uri] of the image to upload.
     * @param onComplete Callback invoked with the download URL upon successful upload.
     */
    private fun uploadImageToFirebase(uri: Uri, onComplete: (downloadUrl: String) -> Unit) {
        // Create a reference to Firebase Storage.
        val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference
        // Generate a unique file name for the image.
        val fileRef = storageRef.child("recipes_images/${System.currentTimeMillis()}.jpg")
        // Upload the file.
        fileRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // After upload, obtain the download URL.
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onComplete(downloadUri.toString())
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, getString(R.string.fail_URL, exception.message), Toast.LENGTH_SHORT).show()

                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, getString(R.string.fail_upload, exception.message), Toast.LENGTH_SHORT).show()

            }
    }

    /**
     * Sets observers for LiveData objects from the RecipeViewModel.
     *
     * Observes the save status for recipes and shows corresponding messages to the user.
     */
    private fun setObserver() {
        recipeVM.getIsSaved().observe(this, Observer {
            when (it) {
                Constants.BD_MSGS.SUCCESS -> {
                    Toast.makeText(this, R.string.success_add, Toast.LENGTH_SHORT).show()
                    clearEdits()
                }
                Constants.BD_MSGS.FAIL -> Toast.makeText(this, R.string.fail_add, Toast.LENGTH_SHORT).show()
                Constants.BD_MSGS.CONSTRAINT -> Toast.makeText(this, R.string.constraint_add, Toast.LENGTH_SHORT).show()
            }
        })
    }
}

