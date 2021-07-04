package com.elacqua.geotask.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elacqua.geotask.data.model.Place
import com.elacqua.geotask.databinding.MapRvItemBinding

class PlaceRecyclerAdapter(
    private val placeSelectedListener: OnPlaceSelectedListener
) : RecyclerView.Adapter<PlaceRecyclerAdapter.PlaceViewHolder>() {

    private val places = ArrayList<Place>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MapRvItemBinding.inflate(inflater, parent, false)
        return PlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        holder.run {
            onBind(position)
        }
    }

    override fun getItemCount() =
        places.size

    fun clearAndAddPlaces(placesList: List<Place>) {
        places.clear()
        places.addAll(placesList)
        notifyDataSetChanged()
    }

    fun getPlaces() = places

    inner class PlaceViewHolder(private val binding: MapRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isExpanded = false
        init {
            binding.presenter = this
        }

        fun onBind(position: Int) {
            binding.txtRvItemTitle.text = places[position].title
            binding.expandLayout.visibility = View.GONE
        }

        fun toggle() {
            if (isExpanded) {
                collapse()
            } else {
                expand()
            }
            isExpanded = !isExpanded
        }

        private fun collapse() {
            binding.expandLayout.visibility = View.GONE
        }

        private fun expand() {
            binding.expandLayout.visibility = View.VISIBLE
        }

        fun handleDetailButton() {
            placeSelectedListener.onDetailClicked(places[adapterPosition])
        }

        fun handleShowLocationButton() {
            placeSelectedListener.onLocationClicked(places[adapterPosition])
        }
    }
}