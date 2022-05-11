package com.bunyodjon.yolbelgilari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.bunyodjon.yolbelgilari.MyDate.count
import com.bunyodjon.yolbelgilari.databinding.FragmentLikesBinding
import com.bunyodjon.yolbelgilari.databinding.ItemRvBinding
import com.bunyodjon.yolbelgilari.dbhelper.MyDBHelper
import com.bunyodjon.yolbelgilari.dbhelper.RoadSign

class Likes_fragment : Fragment() {
    lateinit var binding: FragmentLikesBinding
    lateinit var list: ArrayList<RoadSign>
    lateinit var myAdapterClue: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        count = 1
        binding = FragmentLikesBinding.inflate(layoutInflater)
        val myDBHelper = MyDBHelper(requireActivity())
        val lista = myDBHelper.showRoadSign()
        list= ArrayList()
        lista.forEach {
            if (it.like.toBoolean()){
                list.add(it)
            }
        }
        myAdapterClue = RecyclerViewAdapter(requireActivity(),list,object :RecyclerViewAdapter.RVClick{

        })
        binding.rvList.adapter = myAdapterClue
        binding.about.setOnClickListener {
            count =0
            findNavController().navigate(R.id.action_likes_fragment_to_about_fragment)
        }
        binding.home.setOnClickListener {
            count =0
            findNavController().navigate(R.id.action_likes_fragment_to_home_fragment)
        }
        return binding.root
    }
}