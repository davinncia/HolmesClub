package com.davinciapp.holmesclub.drafts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.holmesclub.R

class DraftAdapter : RecyclerView.Adapter<DraftAdapter.DraftViewHolder>() {

    private var drafts = listOf<DraftListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_draft, parent,false)
        return DraftViewHolder(view)
    }

    override fun getItemCount(): Int = drafts.size


    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) {
        holder.bind(drafts[position])
    }

    fun populateList(draftList: List<DraftListItem>) {
        this.drafts = draftList
        notifyDataSetChanged()
    }

    inner class DraftViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val titleView = view.findViewById<TextView>(R.id.tv_title_article_item)
        private val modifTime = view.findViewById<TextView>(R.id.tv_date_article_item)

        fun bind(draftItem: DraftListItem) {
            titleView.text = draftItem.title
            modifTime.text = draftItem.modifTime
        }
    }

}