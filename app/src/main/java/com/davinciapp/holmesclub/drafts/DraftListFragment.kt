package com.davinciapp.holmesclub.drafts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.editor.EditorActivity


class DraftListFragment : Fragment() {

    private lateinit var textView: TextView
    private var title: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView =  inflater.inflate(R.layout.fragment_my_articles_list, container, false)

        //ViewModel
        val viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(requireActivity().application))[DraftViewModel::class.java]

        //Recycler View
        val adapter = DraftAdapter(object : DraftAdapter.OnDraftItemClickListener {
            override fun onDraftItemClicked(draftId: Int) {
                startActivity(EditorActivity.newIntent(requireContext(), draftId))
            }
            override fun onDraftItemSwiped(draftId: Int) {
                viewModel.removeDraft(draftId)
            }
        })
        initRecyclerView(rootView, adapter)

        //Listen
        viewModel.draftItems.observe(this, Observer {
            //adapter.populateList(it)
            adapter.submitList(it)
        })

        return rootView
    }

    private fun initRecyclerView(rootView: View, adapter: DraftAdapter) {

        rootView.findViewById<RecyclerView>(R.id.recycler_view_article_list).apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //Swipe to delete function
            ItemTouchHelper(SwipeToDeleteCallback(adapter, requireContext())).attachToRecyclerView(this)
        }

    }

    companion object {
        const val ARG_TITLE = "title_arg"

        @JvmStatic
        fun newInstance(title: String) =
            DraftListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }
}
