package com.bunyodjon.yolbelgilari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bunyodjon.yolbelgilari.databinding.FragmentOgohlantiruvchiBinding
import com.bunyodjon.yolbelgilari.dbhelper.MyDBHelper
import com.bunyodjon.yolbelgilari.dbhelper.RoadSign

class Ogohlantiruvchi : Fragment() {
    lateinit var binding: FragmentOgohlantiruvchiBinding
    lateinit var list:ArrayList<RoadSign>
    lateinit var myAdapterClue: RecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =FragmentOgohlantiruvchiBinding.inflate(layoutInflater)
        list = ArrayList()
        val myDBHelper =MyDBHelper(requireActivity())
        val lista = myDBHelper.showRoadSign()
        lista.forEach {
            if (it.type == "Ogohlantiruvchi"){
                list.add(it)
            }
        }
        myAdapterClue = RecyclerViewAdapter(requireActivity(),list,object :RecyclerViewAdapter.RVClick{

        })
        binding.rvOgohlantiruvchi.adapter = myAdapterClue
        return binding.root
    }
    fun work(){

    }
}