package com.davinciapp.holmesclub.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.feed.FeedViewModel.ArticleFeedItem

class FeedListAdapter() : RecyclerView.Adapter<FeedListAdapter.FeedItemViewHolder>() {

    private var items = listOf<ArticleFeedItem>()

    fun populateList(feed: List<ArticleFeedItem>) {
        items = feed
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val view =
            when (viewType) {
                ArticleFeedItem.VIEW_ONE ->
                    LayoutInflater.from(parent.context).inflate(R.layout.item_feed_article_one, parent, false)
                else ->
                    LayoutInflater.from(parent.context).inflate(R.layout.item_feed_article_two, parent, false)
            }
        return FeedItemViewHolder(view)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {
        holder.bind(items[position])
    }


    inner class FeedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val titleView = itemView.findViewById<TextView>(R.id.tv_title_feed_item)
        private val coverView = itemView.findViewById<ImageView>(R.id.iv_cover_feed_item)

        fun bind(item: ArticleFeedItem) {
            titleView.text = item.title
            coverView.setImageResource(R.color.colorAccent)
        }
    }



}