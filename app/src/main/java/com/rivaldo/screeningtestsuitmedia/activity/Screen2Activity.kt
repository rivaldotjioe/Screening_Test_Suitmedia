package com.rivaldo.screeningtestsuitmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.rivaldo.screeningtestsuitmedia.utils.CommonUtil
import com.rivaldo.screeningtestsuitmedia.databinding.ActivityScreen2Binding

class Screen2Activity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding : ActivityScreen2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScreen2Binding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.txtName.text = intent.getStringExtra(CommonUtil.NAMES)+"!"
        binding.btnEvent.setOnClickListener(this)
        binding.btnGuest.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var intent : Intent? = null
        when(v?.id){
            binding.btnEvent.id -> {
                intent = Intent(this, EventsActivity::class.java)
                startActivityForResult(intent,1)
            }
            binding.btnGuest.id -> {
                intent = Intent(this, GuestActivity::class.java)
                startActivityForResult(intent,2)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
               val textButton = data?.getStringExtra(CommonUtil.EVENT_BUTTON)
                binding.btnEvent.text = textButton
            }
        } else if (requestCode == 2){
            if (resultCode == RESULT_OK){
                val textButton = data?.getStringExtra(CommonUtil.GUEST_BUTTON)
                binding.btnGuest.text = textButton
            }
        }
    }


}