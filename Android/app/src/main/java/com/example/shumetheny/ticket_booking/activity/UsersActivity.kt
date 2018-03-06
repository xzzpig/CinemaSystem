package com.example.shumetheny.ticket_booking.activity

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.example.shumetheny.ticket_booking.R
import com.example.shumetheny.ticket_booking.bean.Movie
import com.example.shumetheny.ticket_booking.fragment.BlankFragment
import com.example.shumetheny.ticket_booking.fragment.OrderFragment
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_users.*
import kotlinx.android.synthetic.main.app_bar_users.*
import kotlinx.android.synthetic.main.content_users.*
import kotlinx.android.synthetic.main.nav_header_users.*
import okhttp3.OkHttpClient
import okhttp3.Request

class UsersActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val userPerf by lazy { getSharedPreferences("user", Context.MODE_PRIVATE) }

    var orderFragment:OrderFragment?=null

    val LOGINREQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        drawer_layout.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerStateChanged(newState: Int) {
            }

            override fun onDrawerSlide(drawerView: View?, slideOffset: Float) {
            }

            override fun onDrawerClosed(drawerView: View?) {
            }

            override fun onDrawerOpened(drawerView: View?) {
                textView_nav_name.text = userPerf.getString("phone", "");
            }
        })
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)
        nav_view.menu.getItem(0).title = if (userPerf.getBoolean("logined", false))  "退出登录" else  "登录/注册"
        viewpager_main.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return if (position == 0) BlankFragment() else OrderFragment().apply { orderFragment=this };
            }

            override fun getCount(): Int = 2

        }
        viewpager_main.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    bottomNavigationView_main.menu.getItem(0).setChecked(true)
                } else {
                    bottomNavigationView_main.menu.getItem(1).setChecked(true)
                    orderFragment?.update()
                }
            }
        })

        bottomNavigationView_main.setOnNavigationItemSelectedListener { menu -> menu.apply { viewpager_main.currentItem = if (menu.itemId == R.id.item_main1) 0 else 1 }.run { true } }

    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun OrderFragment.update(){
        this.LoadOrderask().execute()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.users, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        if (item.title.toString().equals("登录/注册")) {
            val intent = Intent(this, LoginActivity::class.java)
//            startActivity(intent)
            startActivityForResult(intent, LOGINREQUEST)
        } else {
            userPerf.edit().putBoolean("logined", false).commit()
            item.title = "登录/注册"
        }
//        when (item.itemId) {
//            R.id.nav_private -> {
//
//            }
//            R.id.nav_gallery -> {
//
//            }
//            R.id.nav_slideshow -> {
//
//            }
//            R.id.nav_manage -> {
//
//            }
//            R.id.nav_share -> {
//
//            }
//            R.id.nav_send -> {

//            }
//        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            LOGINREQUEST -> {
                if (resultCode == 1) {
                    nav_view.menu.getItem(0).title = "退出登录"
                } else {
                    nav_view.menu.getItem(0).title = "登录/注册"
                }
            }
        }
    }

}
