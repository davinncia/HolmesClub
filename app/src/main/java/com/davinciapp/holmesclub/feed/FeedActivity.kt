package com.davinciapp.holmesclub.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.davinciapp.holmesclub.R
import com.davinciapp.holmesclub.bind
import com.davinciapp.holmesclub.drafts.DraftActivity
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout

class FeedActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val drawerLayout by bind<DrawerLayout>(R.id.drawer_layout_feed)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        //DRAWER
        configureDrawerLayout()

        //VIEW PAGER
        val viewPager = findViewById<ViewPager>(R.id.view_pager_articles)
        val pagerAdapter = FeedPagerAdapter(
            supportFragmentManager
        )
        viewPager.adapter = pagerAdapter

        //TABS
        val tabLayout = findViewById<TabLayout>(R.id.view_pager_tab_articles)
        tabLayout.setupWithViewPager(viewPager)


    }

    //--------------------------------------------------------------------------------------------//
    //                                        D R A W E R
    //--------------------------------------------------------------------------------------------//

    private fun configureDrawerLayout() {
        //Tool bar
        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_feed)
        setSupportActionBar(toolBar)
        //Drawer
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Navigation view
        val navView = findViewById<NavigationView>(R.id.nav_view_feed)
        navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_drawer_feed_main -> Toast.makeText(this, "Feed", Toast.LENGTH_SHORT).show()
            R.id.item_drawer_profile_main -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            R.id.item_drawer_my_articles_main -> startActivity(DraftActivity.newIntent(this))
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    override fun onBackPressed() {
        //Handle back click to close menu drawer
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, FeedActivity::class.java)
    }
}
