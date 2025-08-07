package com.example.recipesfinder.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.appcompat.app.AlertDialog
import com.example.recipesfinder.R
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.databinding.FragmentUserProfileBinding
import com.example.recipesfinder.ui.view.MainActivity
import com.example.recipesfinder.ui.viewmodel.UserViewModel

import com.google.firebase.auth.*


/**
 * A Fragment that displays the user's profile information.
 *
 * This fragment retrieves and displays user details such as nickname, first name, last name,
 * email, and profile image. It provides navigation to change the profile picture, view the user's
 * favorite recipes, view the user's recipe list, and log out. On logout, it shows a confirmation
 * dialog before signing the user out and navigating back to the main activity.
 *
 * The fragment uses [FragmentUserProfileBinding] for view binding and interacts with [UserViewModel]
 * to obtain user data.
 *
 * @constructor Creates an instance of [UserProfileFragment].
 */
class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private lateinit var userViewModel: UserViewModel
    private lateinit var dialog: AlertDialog

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view of the inflated layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after [onCreateView].
     *
     * Initializes the [UserViewModel], sets click listeners for various UI elements for navigation
     * (changing profile picture, viewing favorites, user recipes, and logout), and retrieves the user data
     * based on the currently authenticated user. It also sets up observers to update the UI with user data.
     *
     * @param view The view returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        // Navigate to change profile picture.
        binding.ivIconUsuario.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_changeProfilePictureFragment)
        }
        // Navigate to favorite list.
        binding.btnFavoritos.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_favoriteListFragment)
        }
        // Navigate to user's recipe list.
        binding.btnMinhasReceitas.setOnClickListener {
            findNavController().navigate(R.id.action_userProfileFragment_to_userRecipeListFragment)
        }
        // Show logout confirmation dialog.
        binding.btnLogout.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
                .setTitle(R.string.atencion)
                .setMessage(R.string.log_out_msg)
                .setNegativeButton(R.string.no) { _, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton(R.string.yes) { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    dialog.dismiss()
                }

            dialog = builder.create()
            dialog.show()
        }

        // Retrieve user data for the current authenticated user.
        auth.currentUser?.let { userViewModel.getById(it.uid) }
        setObserver()
    }

    /**
     * Clears the text fields and resets the profile image to a placeholder.
     */
    private fun clearTexts() {
        binding.tvNickname.text = ""
        binding.tvPrimeiroNome.text = ""
        binding.tvSegundoNome.text = ""
        binding.tvEmailUsuario.text = ""
        binding.ivIconUsuario.setImageResource(R.drawable.ic_image_placeholder)
    }

    /**
     * Sets up observers on the UserViewModel's LiveData objects.
     *
     * Observes the user data and updates the UI elements accordingly. If the user has a custom profile image,
     * it is retrieved from [Constants.USER_IMAGES.profileImageMap] and set in the ImageView.
     */
    private fun setObserver() {
        userViewModel.getUser().observe(viewLifecycleOwner,Observer { user ->
            binding.tvNickname.text = user.nickname
            binding.tvPrimeiroNome.text = user.firstname
            binding.tvSegundoNome.text = user.lastname
            binding.tvEmailUsuario.text = user.email
            val resId = Constants.USER_IMAGES.profileImageMap[user.imageURL]
            if (resId != null) {
                binding.ivIconUsuario.setImageResource(resId)
            }
        })
    }

    /**
     * Called when the view previously created by [onCreateView] is being destroyed.
     *
     * Cleans up the binding reference to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
