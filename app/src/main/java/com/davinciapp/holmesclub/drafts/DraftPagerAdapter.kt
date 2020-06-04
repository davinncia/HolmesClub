package com.davinciapp.holmesclub.drafts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class DraftPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val pagesNbr = 2
    private val titles = arrayOf("Drafts", "Published")

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> DraftListFragment.newInstance("Draft")
            else -> DraftListFragment.newInstance("Published")
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