package com.davinciapp.holmesclub.view.drafts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.di.ViewModelFactory
import com.davinciapp.holmesclub.view.editor.EditorActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DraftActivity : AppCompatActivity() {
    //TODO clear tablayout

    private val fab by bind<FloatingActionButton>(R.id.fab_articles)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        //ViewModel
        val viewModel = ViewModelProvider(this, ViewModelFactory.getInstance(application))[DraftViewModel::class.java]

        //Recycler View
        val adapter = DraftAdapter(object : DraftAdapter.OnDraftItemClickListener {
            override fun onDraftItemClicked(draftId: Int) {
                startActivity(EditorActivity.newIntent(this@DraftActivity, draftId))
            }
            override fun onDraftItemSwiped(draftId: Int) {
                viewModel.removeDraft(draftId)
            }
        })
        initRecyclerView(adapter)

        //Listen
        viewModel.draftItems.observe(this, Observer {
            //adapter.populateList(it)
            adapter.submitList(it)
        })

        fab.setOnClickListener {
            startActivity(EditorActivity.newIntent(this))
        }
    }

    private fun initRecyclerView(adapter: DraftAdapter) {

        findViewById<RecyclerView>(R.id.recycler_view_draft_list).apply {
            this.adapter = adapter
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            //Swipe to delete function
            ItemTouchHelper(SwipeToDeleteCallback(adapter, this@DraftActivity)).attachToRecyclerView(this)
        }

    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, DraftActivity::class.java)
    }
}
