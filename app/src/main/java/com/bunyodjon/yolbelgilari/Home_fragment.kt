package com.bunyodjon.yolbelgilari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bunyodjon.yolbelgilari.databinding.FragmentHomeFragmentBinding

class Home_fragment : Fragment() {
    lateinit var myViewPagerAdapter:MyViewPagerAdapter
    lateinit var list: ArrayList<String>
    lateinit var binder: FragmentHomeFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binder  = FragmentHomeFragmentBinding.inflate(layoutInflater)
        work()
        binder.like.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_likes_fragment)
        }
        binder.about.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_about_fragment)
        }
        binder.addBtn.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_addFragment)
        }
        return binder.root
    }
    fun work(){
        list=ArrayList()
        myViewPagerAdapter  = MyViewPagerAdapter(list)
        val fragmentAdapter = FragmentAdapter(parentFragmentManager)
        fragmentAdapter.addFragment(Ogohlantiruvchi(),"Ogohlantiruvchi")
        fragmentAdapter.addFragment(Imtiyozli(),"Imtiyozli")
        fragmentAdapter.addFragment(Taqiqlovchi(),"Taqiqlovchi")
        fragmentAdapter.addFragment(Buyuruvchi(),"Buyuruvchi")
        binder.viewPager.adapter = fragmentAdapter
        fragmentAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        work()
        super.onResume()
    }
}