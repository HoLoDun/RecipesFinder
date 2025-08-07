package com.example.recipesfinder.ui.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.recipesfinder.R
import com.example.recipesfinder.data.model.Comment
import com.example.recipesfinder.databinding.FragmentAddCommentBinding
import com.example.recipesfinder.ui.viewmodel.CommentViewModel


/**
 * Fragment that allows the user to add a comment and rating for a recipe.
 *
 * This fragment provides an interface where the user can select a star rating (from 1 to 5) and write a comment.
 * It uses view binding ([FragmentAddCommentBinding]) for UI access and interacts with [CommentViewModel]
 * to persist the comment data. The recipe ID to which the comment is associated is received through
 * navigation arguments ([AddCommentFragmentArgs]). The fragment also handles the display of star icons based on the rating selected.
 *
 * @constructor Creates an instance of [AddCommentFragment].
 */
class AddCommentFragment : Fragment() {

    // View binding to access UI elements.
    private var _binding: FragmentAddCommentBinding? = null
    private val binding get() = _binding!!

    // Firebase authentication instance.
    private val auth = FirebaseAuth.getInstance()

    // Navigation arguments containing the recipe ID.
    private val args: AddCommentFragmentArgs by navArgs()

    // ViewModel for handling comment-related operations.
    private lateinit var comVM: CommentViewModel

    // Variable to store the selected rating (from 1 to 5).
    private var rating: Int = 0

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding and returns the root view.
     *
     * @param inflater The LayoutInflater object that can be used to inflate views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The root view for the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCommentBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * Called immediately after [onCreateView].
     *
     * Initializes the [CommentViewModel] and sets up click listeners for:
     * - The five star icons to allow the user to select a rating.
     * - The "Mandar" button to submit the comment.
     *
     * When the "Mandar" button is clicked, a [Comment] object is created with the current rating,
     * comment text, recipe ID, and user ID. If the comment text is empty, a toast is shown; otherwise,
     * the comment is saved using the [CommentViewModel].
     *
     * @param view The view returned by [onCreateView].
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        comVM = ViewModelProvider(requireActivity()).get(CommentViewModel::class.java)

        // Set click listeners for star icons to update the rating.
        binding.star1.setOnClickListener {
            rating = 1
            binding.star1.setImageResource(R.drawable.ic_star_filled)
            binding.star2.setImageResource(R.drawable.ic_star_empty)
            binding.star3.setImageResource(R.drawable.ic_star_empty)
            binding.star4.setImageResource(R.drawable.ic_star_empty)
            binding.star5.setImageResource(R.drawable.ic_star_empty)
        }
        binding.star2.setOnClickListener {
            rating = 2
            binding.star1.setImageResource(R.drawable.ic_star_filled)
            binding.star2.setImageResource(R.drawable.ic_star_filled)
            binding.star3.setImageResource(R.drawable.ic_star_empty)
            binding.star4.setImageResource(R.drawable.ic_star_empty)
            binding.star5.setImageResource(R.drawable.ic_star_empty)
        }
        binding.star3.setOnClickListener {
            rating = 3
            binding.star1.setImageResource(R.drawable.ic_star_filled)
            binding.star2.setImageResource(R.drawable.ic_star_filled)
            binding.star3.setImageResource(R.drawable.ic_star_filled)
            binding.star4.setImageResource(R.drawable.ic_star_empty)
            binding.star5.setImageResource(R.drawable.ic_star_empty)
        }
        binding.star4.setOnClickListener {
            rating = 4
            binding.star1.setImageResource(R.drawable.ic_star_filled)
            binding.star2.setImageResource(R.drawable.ic_star_filled)
            binding.star3.setImageResource(R.drawable.ic_star_filled)
            binding.star4.setImageResource(R.drawable.ic_star_filled)
            binding.star5.setImageResource(R.drawable.ic_star_empty)
        }
        binding.star5.setOnClickListener {
            rating = 5
            binding.star1.setImageResource(R.drawable.ic_star_filled)
            binding.star2.setImageResource(R.drawable.ic_star_filled)
            binding.star3.setImageResource(R.drawable.ic_star_filled)
            binding.star4.setImageResource(R.drawable.ic_star_filled)
            binding.star5.setImageResource(R.drawable.ic_star_filled)
        }

        // Set click listener for the "Mandar" button to submit the comment.
        binding.btnMandar.setOnClickListener {
            try {
                val c = Comment().apply {
                    this.Rating = rating.toFloat()
                    this.Commentary = binding.etComentario.text.toString()
                    this.idref = args.recipeId
                    this.iduser = auth.currentUser?.uid.toString()
                }
                if (c.Commentary.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.empty_number_msg),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    comVM.saveComment(c)
                }
            } catch (e: Exception) {
                Toast.makeText(requireContext(), getString(R.string.empty_number_msg), Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Called when the view previously created by [onCreateView] is being destroyed.
     *
     * Clears the binding reference to prevent memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

