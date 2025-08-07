package com.example.recipesfinder.ui.view.fragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.replace
import androidx.fragment.app.commit
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.fragment.findNavController
import com.example.recipesfinder.R
import androidx.navigation.fragment.navArgs
import com.example.recipesfinder.data.model.Comment
import com.example.recipesfinder.databinding.FragmentCommentListBinding
import com.example.recipesfinder.util.Constants
import com.example.recipesfinder.ui.view.adapter.ListCommentAdapter
import com.example.recipesfinder.ui.view.listener.OnCommentListener
import com.example.recipesfinder.ui.viewmodel.CommentViewModel

/**
 * A Fragment that displays a list of comments associated with a specific recipe.
 *
 * This fragment retrieves comments from a [CommentViewModel] based on the recipe reference ID
 * passed through navigation arguments. The comments are displayed using a RecyclerView with a
 * [ListCommentAdapter].
 */
class CommentListFragment : Fragment() {

    private var _binding: FragmentCommentListBinding? = null
    private val binding get() = _binding!!
    private lateinit var commentVM: CommentViewModel
    private val adapter = ListCommentAdapter()
    private val args: CommentListFragmentArgs by navArgs()

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * Inflates the layout using view binding, sets up the RecyclerView with a [LinearLayoutManager]
     * and adapter, retrieves the recipe reference ID from navigation arguments, configures the
     * [CommentViewModel] and fetches the comments, and sets up observers to update the UI.
     *
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return The View for the fragment's UI.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentCommentListBinding.inflate(inflater, container, false)
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        val idRef = args.idRef

        commentVM = ViewModelProvider(requireActivity()).get(CommentViewModel::class.java)
        commentVM.getByIdref(idRef)

        setObserver()

        return binding.root
    }

    /**
     * Sets up observers for the ViewModel's LiveData objects.
     *
     * Observes:
     * - The status of the list operation via [CommentViewModel.getIsListed] to show appropriate messages.
     * - The list of comments via [CommentViewModel.getCommentList] to update the adapter.
     */
    private fun setObserver() {
        commentVM.getIsListed().observe(viewLifecycleOwner, Observer {
            if (it == Constants.BD_MSGS.NOT_FOUND) {
                Toast.makeText(activity, R.string.list_not_found, Toast.LENGTH_SHORT).show()
            } else if (it == Constants.BD_MSGS.FAIL) {
                Toast.makeText(activity, R.string.fail_search, Toast.LENGTH_SHORT).show()
            }
        })

        commentVM.getCommentList().observe(viewLifecycleOwner, Observer {
            adapter.updateCommentList(it)
        })
    }

    /**
     * Called when the view previously created by [onCreateView] has been detached from the fragment.
     *
     * This method cleans up the view binding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
