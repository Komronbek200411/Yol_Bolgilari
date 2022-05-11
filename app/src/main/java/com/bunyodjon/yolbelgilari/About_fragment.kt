package com.bunyodjon.yolbelgilari

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.bunyodjon.yolbelgilari.databinding.FragmentAboutFragmentBinding

class About_fragment : androidx.fragment.app.Fragment() {
    lateinit var binding: FragmentAboutFragmentBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutFragmentBinding.inflate(layoutInflater)
        binding.home.setOnClickListener {
            findNavController().navigate(R.id.action_about_fragment_to_home_fragment)
        }
        binding.like.setOnClickListener {
            findNavController().navigate(R.id.action_about_fragment_to_likes_fragment)
        }
        return binding.root
    }
}