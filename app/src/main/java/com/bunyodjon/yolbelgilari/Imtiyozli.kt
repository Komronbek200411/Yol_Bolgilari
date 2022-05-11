package com.bunyodjon.yolbelgilari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bunyodjon.yolbelgilari.databinding.FragmentImtiyozliBinding
import com.bunyodjon.yolbelgilari.databinding.ItemRvBinding
import com.bunyodjon.yolbelgilari.dbhelper.MyDBHelper
import com.bunyodjon.yolbelgilari.dbhelper.RoadSign

class Imtiyozli : Fragment() {
    lateinit var binding: FragmentImtiyozliBinding
    lateinit var list: ArrayList<RoadSign>
    lateinit var myAdapterClue: RecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImtiyozliBinding.inflate(layoutInflater)
        list =ArrayList()
        val myDBHelper =MyDBHelper(requireActivity())
        val lista = myDBHelper.showRoadSign()
        lista.forEach {
            if (it.type =="Imtiyozli"){
                list.add(it)
            }
        }
        myAdapterClue = RecyclerViewAdapter(requireActivity(),list,object :RecyclerViewAdapter.RVClick{

        })
        binding.rvImtiyozli.adapter = myAdapterClue
        return binding.root
    }
}