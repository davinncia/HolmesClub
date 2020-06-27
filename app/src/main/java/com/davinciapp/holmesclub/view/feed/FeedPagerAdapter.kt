package com.davinciapp.holmesclub.view.feed

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class FeedPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val pagesNbr = 2
    private val titles = arrayOf("Trending", "In Review")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FeedFragment.newInstance(
                "trends"
            )
            else -> FeedFragment.newInstance(
                "reviews"
            )
        }
    }

    override fun getCount(): Int = pagesNbr

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> titles[0]
            else -> titles[1]
        }
    }
}