package com.yxl.userphotosapp.entry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.yxl.userphotosapp.databinding.ActivityEntryBinding
import com.yxl.userphotosapp.entry.adapters.PagerAdapter

class EntryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEntryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pagerAdapter = PagerAdapter(this)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Login"
                1 -> "Register"
                else -> null
            }
        }.attach()
    }
}