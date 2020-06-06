package com.davinciapp.holmesclub.drafts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.holmesclub.R

class DraftAdapter(private val listener: OnDraftItemClickListener) :
    ListAdapter<DraftListItem, DraftAdapter.DraftViewHolder>(DraftItemDiffCallback()) {

    //private var drafts = listOf<DraftListItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_draft, parent,false)
        return DraftViewHolder(view)
    }

    //override fun getItemCount(): Int = drafts.size


    override fun onBindViewHolder(holder: DraftViewHolder, position: Int) {
        //holder.bind(drafts[position])
        holder.bind(getItem(position))
    }

    //fun populateList(draftList: List<DraftListItem>) {
    //    this.drafts = draftList
    //    notifyDataSetChanged()
    //}

    fun deleteItem(position: Int) {
        listener.onDraftItemSwiped(getItem(position).id)
        //listener.onDraftItemSwiped(drafts[position].id)
        //val draftsArray = ArrayList(drafts)
        //draftsArray.removeAt(position)
        //this.drafts = draftsArray
        //notifyItemRemoved(position)
    }

    inner class DraftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleView = itemView.findViewById<TextView>(R.id.tv_title_article_item)
        private val modifTime = itemView.findViewById<TextView>(R.id.tv_date_article_item)

        init {
            itemView.setOnClickListener {
                listener.onDraftItemClicked(getItem(adapterPosition).id)
                //listener.onDraftItemClicked(drafts[adapterPosition].id)
            }
        }

        fun bind(draftItem: DraftListItem) {
            titleView.text = draftItem.title
            modifTime.text = draftItem.modifTime
        }
    }

    private class DraftItemDiffCallback: DiffUtil.ItemCallback<DraftListItem>() {
        override fun areItemsTheSame(oldItem: DraftListItem, newItem: DraftListItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DraftListItem, newItem: DraftListItem): Boolean {
            return oldItem == newItem
        }

    }

    interface OnDraftItemClickListener {
        fun onDraftItemClicked(draftId: Int)
        fun onDraftItemSwiped(draftId: Int)
    }
}