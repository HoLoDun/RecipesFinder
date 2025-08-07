package com.example.recipesfinder.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.recipesfinder.databinding.FragmentChangeProfilePictureBinding
import com.example.recipesfinder.ui.viewmodel.UserViewModel

/**
 * A Fragment that allows the user to change their profile picture.
 *
 * This fragment displays a grid of profile picture options. When the user clicks on one of the images,
 * the [UserViewModel] is used to update the user's profile image in the database. After updating the image,
 * the fragment pops from the navigation stack, returning the user to the previous screen.
 *
 * The fragment uses view binding ([FragmentChangeProfilePictureBinding]) to access its UI elements,
 * Firebase Authentication to obtain the current user ID, and the Navigation Component for navigation.
 *
 * @constructor Creates an instance of [ChangeProfilePictureFragment].
 */
class ChangeProfilePictureFragment : Fragment() {

    private var _binding: FragmentChangeProfilePictureBinding? = null
    private val binding get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private lateinit var userVM: UserViewModel

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding and returns the root view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view for the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChangeProfilePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after [onCreateView].
     *
     * Initializes the [UserViewModel] and sets up click listeners for each profile image option.
     * When an image is clicked, it updates the user's profile image by calling [UserViewModel.updateUserImage]
     * with the corresponding image identifier, then pops the back stack to return to the previous screen.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userVM = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)

        binding.image1.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile1")
            findNavController().popBackStack()
        }
        binding.image2.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile2")
            findNavController().popBackStack()
        }
        binding.image3.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile3")
            findNavController().popBackStack()
        }
        binding.image4.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile4")
            findNavController().popBackStack()
        }
        binding.image5.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile5")
            findNavController().popBackStack()
        }
        binding.image6.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile6")
            findNavController().popBackStack()
        }
        binding.image7.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile7")
            findNavController().popBackStack()
        }
        binding.image8.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile8")
            findNavController().popBackStack()
        }
        binding.image9.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile9")
            findNavController().popBackStack()
        }
        binding.image10.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile10")
            findNavController().popBackStack()
        }
        binding.image11.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile11")
            findNavController().popBackStack()
        }
        binding.image12.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile12")
            findNavController().popBackStack()
        }
        binding.image13.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile13")
            findNavController().popBackStack()
        }
        binding.image14.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile14")
            findNavController().popBackStack()
        }
        binding.image15.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile15")
            findNavController().popBackStack()
        }
        binding.image16.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile16")
            findNavController().popBackStack()
        }
        binding.image17.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile17")
            findNavController().popBackStack()
        }
        binding.image18.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile18")
            findNavController().popBackStack()
        }
        binding.image19.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile19")
            findNavController().popBackStack()
        }
        binding.image20.setOnClickListener {
            userVM.updateUserImage(auth.currentUser?.uid.toString(), "profile20")
            findNavController().popBackStack()
        }
    }

    /**
     * Called when the view created by [onCreateView] is about to be destroyed.
     *
     * Clears the view binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

