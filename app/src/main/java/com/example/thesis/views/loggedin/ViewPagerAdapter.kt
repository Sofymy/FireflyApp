package com.example.thesis.views.loggedin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm:FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(position: Int): Fragment {
        return when(position) {
            0 -> {
                CurrentFriendsFragment()
            }
            1 -> {
                SearchFragment()
            }
            else -> RequestsFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when(position) {
            0 -> {
                return "Friends"
            }
            1 -> {
                return "Search friends"
            }
            2 -> {
                return "Requests"
            }
        }
        return super.getPageTitle(position)
    }

}