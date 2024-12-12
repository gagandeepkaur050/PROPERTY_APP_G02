package com.example.property_app_g02.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.property_app_g02.ClickDetectorInterface
import com.example.property_app_g02.House
import com.example.property_app_g02.R
import com.example.property_app_g02.databinding.WatchlistItemBinding

class WatchlistAdapter(
    private var houses: List<House>, val clickInterface:ClickDetectorInterface
) : RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {

    // ViewHolder class for item views
    inner class WatchlistViewHolder(val binding: WatchlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(house: House) {
//            binding.addressText.setText("Adress: " + house.address)
//            binding.priceText.text = "Price: $" +house.monthPrice.toString()
//            binding.bedroomsText.text = "Bedrooms: " + house.numberOfBedrooms.toString()
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val binding = WatchlistItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WatchlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val house:House = this.houses[position]
//
//        val imageId = holder.itemView.context.resources.getIdentifier("${house.img}", "drawable", holder.itemView.context.packageName)
//        holder.binding.imgHouse.setImageResource(imageId)

        // Use Glide to load the image
        Glide.with(holder.itemView.context)
            .load(house.img) // URL from Firebase
            .into(holder.binding.imgHouse)

        holder.binding.addressText.setText("Adress: " + house.address)
        holder.binding.priceText.text = "Price: $" +house.monthPrice.toString()
        holder.binding.bedroomsText.text = "Bedrooms: " + house.numberOfBedrooms.toString()
        Log.d("TESTING","Imgurl:${house.img}")
        // holder.bind(house)

        holder.binding.btnRemove.setOnClickListener {
            clickInterface.deleteFunction(position)
        }
    }

    override fun getItemCount(): Int {
        return houses.size
    }

    // Method to update the data in the adapter
    fun updateHouses(newHouses: List<House>) {
        houses = newHouses
        notifyDataSetChanged()  // Or use notifyItemInserted() for better efficiency
    }
}
