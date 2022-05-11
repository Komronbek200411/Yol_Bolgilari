package com.bunyodjon.yolbelgilari

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bunyodjon.yolbelgilari.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (MyDate.a == 0) {
            finish()
            super.onBackPressed()
        }
    }
}