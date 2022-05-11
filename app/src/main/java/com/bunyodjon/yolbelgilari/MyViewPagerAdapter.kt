package com.bunyodjon.yolbelgilari

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bunyodjon.yolbelgilari.databinding.ItemPageBinding

class MyViewPagerAdapter(private val list :List<String>): PagerAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemPageBinding=
            ItemPageBinding.inflate(LayoutInflater.from(container.context),container ,false)
        container.addView(itemPageBinding.root,position)
        return itemPageBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        val view = `object` as View
        container.removeView(view)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return list[position]
    }
}