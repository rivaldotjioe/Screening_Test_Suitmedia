package com.rivaldo.screeningtestsuitmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rivaldo.screeningtestsuitmedia.databinding.ActivityScreen2Binding

class Screen2Activity : AppCompatActivity() {

    private lateinit var binding : ActivityScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreen2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.txtName.text = intent.getStringExtra(CommonUtil.NAMES)+"!"
    }

}