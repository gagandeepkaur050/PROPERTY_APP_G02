package com.example.property_app_g02.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.property_app_g02.House
import com.example.property_app_g02.databinding.WatchlistItemBinding

class WatchlistAdapter(
    private var houses: List<House>
) : RecyclerView.Adapter<WatchlistAdapter.WatchlistViewHolder>() {

    // ViewHolder class for item views
    inner class WatchlistViewHolder(private val binding: WatchlistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(house: House) {
            binding.addressText.setText("Adress: " + house.address)
            binding.priceText.text = "Price: $" +house.monthPrice.toString()
            binding.bedroomsText.text = "Bedrooms: " + house.numberOfBedrooms.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchlistViewHolder {
        val binding = WatchlistItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return WatchlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchlistViewHolder, position: Int) {
        val house = houses[position]
        holder.bind(house)
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
