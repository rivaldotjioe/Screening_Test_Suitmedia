package com.rivaldo.screeningtestsuitmedia.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.rivaldo.screeningtestsuitmedia.R
import com.rivaldo.screeningtestsuitmedia.adapter.EventsAdapter
import com.rivaldo.screeningtestsuitmedia.databinding.ActivityEventsBinding
import com.rivaldo.screeningtestsuitmedia.databinding.ActivityScreen2Binding
import com.rivaldo.screeningtestsuitmedia.model.Event
import com.rivaldo.screeningtestsuitmedia.utils.CommonUtil

class EventsActivity : AppCompatActivity() {
    var binding : ActivityEventsBinding? = null
    var bindingScreen2 : ActivityScreen2Binding? = null
    private var selectedEvent : String? = null
    private lateinit var eventsAdapter : EventsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityEventsBinding.inflate(layoutInflater)
        bindingScreen2 = ActivityScreen2Binding.inflate(layoutInflater)
        val view = binding?.root
        super.onCreate(savedInstanceState)
        setContentView(view)
        initActionBar()
        initRecyclerView()
    }

    private fun initActionBar(){
        var actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.title = "Events"
        }
    }

    private fun initRecyclerView(){
        val recyclerView = binding?.recyclerViewEvents
        eventsAdapter = EventsAdapter(CommonUtil.generateDummyDataEvent(resources), { event ->
            adapterOnClick(event)
        })
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView?.layoutManager = layoutManager
        recyclerView?.itemAnimator = DefaultItemAnimator()
        recyclerView?.adapter = eventsAdapter
        eventsAdapter.notifyDataSetChanged()
    }

    private fun adapterOnClick(event: Event){
        selectedEvent = event.title
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun sendIntentToPreviousActivity(){
        val intent = Intent()
        if (selectedEvent != null){
            intent.putExtra(CommonUtil.EVENT_BUTTON, selectedEvent)
            setResult(RESULT_OK, intent)
        } else {
            setResult(RESULT_CANCELED, intent)
        }
    }

    override fun onBackPressed() {
        sendIntentToPreviousActivity()
        super.onBackPressed()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_events, menu)
        return super.onCreateOptionsMenu(menu)
    }
}