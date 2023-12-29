package com.yxl.userphotosapp.main

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView
import com.yxl.userphotosapp.R
import com.yxl.userphotosapp.databinding.ActivityPhotoBinding
import com.yxl.userphotosapp.main.map.MapFragment
import com.yxl.userphotosapp.main.photos.PhotosFragment

class PhotoActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityPhotoBinding
    var token:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        val toggle = ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close)
        binding.drawer.addDrawerListener(toggle)
        toggle.syncState()
        binding.navView.setNavigationItemSelectedListener(this)
        token = intent.getStringExtra("token")
        val username = intent.getStringExtra("username")
        binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tvUsername).text = username
        binding.navView.setCheckedItem(R.id.iPhotos)
        if(savedInstanceState == null){
            openFragment(PhotosFragment())
        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.iPhotos ->{
                if(binding.navView.checkedItem?.itemId != item.itemId){
                    openFragment(PhotosFragment())
                }

            }
            R.id.iMap -> {
                if(binding.navView.checkedItem?.itemId != item.itemId){
                    openFragment(MapFragment())
                }
            }
        }

        binding.drawer.closeDrawer(GravityCompat.START)
        return true
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START)
        } else{
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    fun openFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(fragment.tag)
            .commit()
    }
}