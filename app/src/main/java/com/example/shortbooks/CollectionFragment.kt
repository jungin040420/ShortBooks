package com.example.shortbooks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2

class CollectionFragment : Fragment(R.layout.fragment_collection) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = view.findViewById<ViewPager2>(R.id.viewPager)
        val dbHelper = DBHelper(requireContext())

        viewPager.adapter = ShortsAdapter(dbHelper.getAllData(), dbHelper)
        viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL // 세로 방향
    }
}