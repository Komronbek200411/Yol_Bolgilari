package com.bunyodjon.yolbelgilari

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bunyodjon.yolbelgilari.databinding.FragmentBuyuruvchiBinding
import com.bunyodjon.yolbelgilari.databinding.ItemRvBinding
import com.bunyodjon.yolbelgilari.dbhelper.MyDBHelper
import com.bunyodjon.yolbelgilari.dbhelper.RoadSign

class Buyuruvchi : Fragment() {
    lateinit var binding: FragmentBuyuruvchiBinding
    lateinit var list:ArrayList<RoadSign>
    lateinit var myAdapterClue: RecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyuruvchiBinding.inflate(layoutInflater)
        list =ArrayList()
        val myDBHelper = MyDBHelper(requireActivity())
        val lista = myDBHelper.showRoadSign()
        lista.forEach {
            if (it.type.equals("Buyuruvchi")){
                list.add(it)
            }
        }
        myAdapterClue = RecyclerViewAdapter(requireActivity(),list,object :RecyclerViewAdapter.RVClick{

        })
        binding.rvBuyuruvchi.adapter = myAdapterClue
        return binding.root
    }
}