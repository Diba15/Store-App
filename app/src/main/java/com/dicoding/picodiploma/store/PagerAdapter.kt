package com.dicoding.picodiploma.store

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.dicoding.picodiploma.store.ui.AddFragment
import com.dicoding.picodiploma.store.ui.ViewFragment

class PagerAdapter(fm: FragmentManager, id: Int): FragmentPagerAdapter(fm) {

    private val pages = listOf(
        AddFragment(id),
        ViewFragment(id)
    )

    override fun getCount(): Int {
        return pages.size
    }

    override fun getItem(position: Int): Fragment {
        return pages[position]
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when(position) {
            0 -> "Tambah Barang"
            else -> "Lihat Barang"
        }
    }

}