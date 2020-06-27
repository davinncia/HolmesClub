package com.davinciapp.holmesclub.view.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.view.article.ArticleActivity
import com.davinciapp.holmesclub.view.login.LoginActivity


class FeedFragment : Fragment(), FeedListAdapter.OnItemClick {

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
        val rootView =  inflater.inflate(R.layout.fragment_feed, container, false)

        //RecyclerView
        val feedAdapter = FeedListAdapter(this)
        initRecyclerView(rootView, feedAdapter)

        //ViewModel
        val viewModel = ViewModelProvider(
            this,
            ViewModelFactory.getInstance(requireActivity().application)
        )[FeedViewModel::class.java]

        viewModel.notLoggedIn.observe(viewLifecycleOwner, Observer {
            if (it) {
                startActivity(LoginActivity.newIntent(requireContext()))
                this.activity?.finish()
            }
        })



        viewModel.articles.observe(viewLifecycleOwner, Observer {
            feedAdapter.populateList(it)
        })

        return rootView
    }

    private fun initRecyclerView(rootView: View, feedAdapter: FeedListAdapter) {

        rootView.findViewById<RecyclerView>(R.id.recycler_view_feed).apply {
            adapter = feedAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }


    companion object {
        const val ARG_TITLE = "title_arg"

        @JvmStatic
        fun newInstance(title: String) =
            FeedFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_TITLE, title)
                }
            }
    }

    override fun onItemClicked(articleId: Int) {
        startActivity(ArticleActivity.newIntent(requireContext()))
    }
}
