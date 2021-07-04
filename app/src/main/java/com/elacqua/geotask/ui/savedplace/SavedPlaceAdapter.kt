package com.elacqua.geotask.ui.savedplace

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elacqua.geotask.R
import com.elacqua.geotask.data.model.Place
import com.elacqua.geotask.databinding.SavedPlaceRvItemBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SavedPlaceAdapter(private val listener: OnPlaceDeleteListener) :
    RecyclerView.Adapter<SavedPlaceAdapter.SavedPlaceViewHolder>() {

    private val places = ArrayList<Place>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedPlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = SavedPlaceRvItemBinding.inflate(inflater, parent, false)
        val viewHolder = SavedPlaceViewHolder(binding)
        binding.presenter = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: SavedPlaceViewHolder, position: Int) {
        holder.onBind()
    }

    override fun getItemCount() = places.size

    fun replacePlaceList(newPlaces: List<Place>) {
        places.clear()
        places.addAll(newPlaces)
        notifyDataSetChanged()
    }

    inner class SavedPlaceViewHolder(private val binding: SavedPlaceRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind() {
            val pos = adapterPosition
            binding.txtSavedPlaceTitle.text = places[pos].title
            binding.txtSavedPlaceDesc.text = places[pos].description
            binding.txtSavedPlaceLat.text =
                binding.root.context.getString(R.string.location_lat, places[pos].latitude)
            binding.txtSavedPlaceLong.text =
                binding.root.context.getString(R.string.location_long, places[pos].longitude)
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = sdf.format(Date(places[pos].date.toLong()))
            binding.txtSavedPlaceDate.text =
                binding.root.context.getString(R.string.location_date, date)
        }

        fun onDeleteButtonCalled() {
            listener.deletePlace(places[adapterPosition])
            places.removeAt(adapterPosition)
            notifyItemRemoved(adapterPosition)
        }
    }
}