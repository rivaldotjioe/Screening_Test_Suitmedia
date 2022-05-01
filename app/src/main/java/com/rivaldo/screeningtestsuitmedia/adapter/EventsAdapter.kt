package com.rivaldo.screeningtestsuitmedia.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rivaldo.screeningtestsuitmedia.databinding.ItemRowBinding
import com.rivaldo.screeningtestsuitmedia.model.Event

class EventsAdapter(private var eventList: List<Event>, private val onClick: (Event) -> Unit) :
    RecyclerView.Adapter<EventsAdapter.MyViewHolder>() {

    inner class MyViewHolder(var binding : ItemRowBinding, val onClick: (Event) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        var title = binding.txtTitleEvent as TextView
        var desc = binding.txtDescEvent
        var date = binding.txtDateEvent
        var img = binding.imageViewEvent
        var time = binding.txtTimeEvent
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventsAdapter.MyViewHolder {
        var binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder){
            val event = eventList[position]
            title.text = event.title
            desc.text = event.desc
            date.text = event.date
            time.text = event.time
            img.setImageResource(event.image)
            itemView.setOnClickListener {
                onClick(event)
            }
        }
    }

    override fun getItemCount(): Int = eventList.size

}