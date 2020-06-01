package com.davinciapp.holmesclub

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val drawerLayout by bind<DrawerLayout>(R.id.drawer_layout_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //startActivity(WritingActivity.newIntent(this))
        configureDrawerLayout()
    }

    //--------------------------------------------------------------------------------------------//
    //                                        D R A W E R
    //--------------------------------------------------------------------------------------------//

    private fun configureDrawerLayout() {
        //Tool bar
        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_main)
        setSupportActionBar(toolBar)
        //Drawer
        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolBar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        //Navigation view
        val navView = findViewById<NavigationView>(R.id.nav_view_main)
        navView.setNavigationItemSelectedListener(this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.item_drawer_feed_main -> Toast.makeText(this, "Feed", Toast.LENGTH_SHORT).show()
            R.id.item_drawer_profile_main -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
            R.id.item_drawer_my_articles_main -> Toast.makeText(this, "Articles", Toast.LENGTH_SHORT).show()
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    override fun onBackPressed() {
        //Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



}
