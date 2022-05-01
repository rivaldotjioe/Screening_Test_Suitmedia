package com.rivaldo.screeningtestsuitmedia.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rivaldo.screeningtestsuitmedia.databinding.ItemGridBinding
import com.rivaldo.screeningtestsuitmedia.model.Guest

class GuestAdapter(private val onClick: (Guest) -> Unit) : RecyclerView.Adapter<GuestAdapter.MyViewHolder>() {
    private var guestList = ArrayList<Guest>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestAdapter.MyViewHolder {
        var binding = ItemGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding, onClick)
    }

    inner class MyViewHolder(var binding: ItemGridBinding, val onClick: (Guest)-> Unit) : RecyclerView.ViewHolder(binding.root) {
        var name = binding.txtUserName
        var image = binding.imageGuest
    }

    override fun onBindViewHolder(holder: GuestAdapter.MyViewHolder, position: Int) {
        with(holder){
            val guest = guestList[position]
            name.text = guest.firstName + " " + guest.lastName
            Glide.with(image).load(guest.avatar).into(image)
            itemView.setOnClickListener{
                onClick(guest)
            }
        }
    }

    override fun getItemCount(): Int = guestList.size


    fun addList(guestList: ArrayList<Guest>) {
        this.guestList.addAll(guestList)
        notifyDataSetChanged()
    }

    fun clear(){
        guestList.clear()
        notifyDataSetChanged()
    }
}