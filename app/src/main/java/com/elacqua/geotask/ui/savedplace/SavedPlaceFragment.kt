package com.elacqua.geotask.ui.savedplace

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elacqua.geotask.data.local.LocalDatabase
import com.elacqua.geotask.data.local.LocalRepository
import com.elacqua.geotask.data.model.Place
import com.elacqua.geotask.databinding.SavedPlaceFragmentBinding

class SavedPlaceFragment : Fragment() {

    private lateinit var viewModel: SavedPlaceViewModel
    private lateinit var binding: SavedPlaceFragmentBinding
    private lateinit var mAdapter: SavedPlaceAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        observePlaces()
        initRecyclerView()
    }

    private fun initViewModel() {
        val db = LocalDatabase.getInstance(requireContext())
        val localRepo = LocalRepository.getInstance(db.placeDao())
        viewModel = ViewModelProvider(this, SavedPlaceViewModelFactory(localRepo))
            .get(SavedPlaceViewModel::class.java)
    }

    private fun observePlaces() {
        viewModel.placesLiveData.observe(viewLifecycleOwner) {
            mAdapter.replacePlaceList(it)
        }
    }

    private fun initRecyclerView() {
        mAdapter = SavedPlaceAdapter(object: OnPlaceDeleteListener {
            override fun deletePlace(place: Place) {
                viewModel.deletePlace(place)
            }
        })
        binding.rvPlaceFrag.run {
            adapter = mAdapter
        }
    }

    fun deleteAllPlaces() {
        viewModel.deleteAllPlaces()
        mAdapter.replacePlaceList(emptyList())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SavedPlaceFragmentBinding.inflate(inflater, container, false)
        binding.presenter = this
        return binding.root
    }


}