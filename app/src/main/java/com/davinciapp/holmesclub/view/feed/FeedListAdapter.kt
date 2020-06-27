package com.davinciapp.holmesclub.view.feed

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.view.feed.FeedViewModel.ArticleFeedItem

class FeedListAdapter(private val listener: OnItemClick) : RecyclerView.Adapter<FeedListAdapter.FeedItemViewHolder>() {

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

        init {
            itemView.setOnClickListener {
                listener.onItemClicked(0)
            }
        }

        private val titleView = itemView.findViewById<TextView>(R.id.tv_title_feed_item)
        private val coverView = itemView.findViewById<ImageView>(R.id.iv_cover_feed_item)
        private val authorImageView = itemView.findViewById<ImageView>(R.id.iv_author_feed_item)
        private val authorView = itemView.findViewById<TextView>(R.id.tv_author_feed_item)

        fun bind(item: ArticleFeedItem) {
            titleView.text = item.title
            Glide.with(coverView.context)
                .load(R.drawable.ic_launcher_background)
                //.apply(RequestOptions.bitmapTransform(RoundedCorners(16)))
                .transform(CenterCrop(), RoundedCorners(16))
                .into(coverView)
            //coverView.setBackgroundResource(R.drawable.rectangle_rounded)
            Glide.with(authorImageView.context).load(R.drawable.ic_sherlock).circleCrop().into(authorImageView)
            authorView.text = item.author
        }
    }

    interface OnItemClick {
        fun onItemClicked(articleId: Int)
    }


}