package com.davinciapp.holmesclub.drafts

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.editor.EditorActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DraftActivity : AppCompatActivity() {
    //TODO clear tablayout

    private val fab by bind<FloatingActionButton>(R.id.fab_articles)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_articles)

        //VIEW PAGER
        val viewPager = findViewById<ViewPager>(R.id.view_pager_articles)
        val pagerAdapter = DraftPagerAdapter(supportFragmentManager)
        viewPager.adapter = pagerAdapter

        //TABS
        //val tabLayout = findViewById<TabLayout>(R.id.view_pager_tab_articles)
        //tabLayout.setupWithViewPager(viewPager)

        fab.setOnClickListener {
            startActivity(EditorActivity.newIntent(this))
        }
    }

    companion object {
        fun newIntent(context: Context): Intent = Intent(context, DraftActivity::class.java)
    }
}
