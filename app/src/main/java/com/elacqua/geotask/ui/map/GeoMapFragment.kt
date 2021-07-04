package com.elacqua.geotask.ui.map

import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elacqua.geotask.R
import com.elacqua.geotask.data.local.LocalDatabase
import com.elacqua.geotask.data.local.LocalRepository
import com.elacqua.geotask.data.model.Place
import com.elacqua.geotask.databinding.FragmentGeomapBinding
import com.elacqua.geotask.utils.*
import com.huawei.hms.maps.CameraUpdateFactory
import com.huawei.hms.maps.HuaweiMap
import com.huawei.hms.maps.OnMapReadyCallback
import com.huawei.hms.maps.model.*

class GeoMapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var viewModel: GeoMapViewModel
    private lateinit var binding: FragmentGeomapBinding
    private lateinit var map: HuaweiMap
    private lateinit var currentMarker: Marker
    private lateinit var mAdapter: PlaceRecyclerAdapter
    private var state = MapMarkerState.ADD_MARKER

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMapSavedInstance(savedInstanceState)
        binding.mapView.getMapAsync(this)
        initViewModel()
        initRecyclerView()
        observePlaces()
        observeDecodedPolyline()
        if (!LocationPermission.hasLocationPerms(requireActivity())) {
            LocationPermission.requestPermission(
                binding.rlMapRoot,
                requireActivity() as AppCompatActivity
            ) {
                permsGranted()
            }
        }
    }

    private fun setMapSavedInstance(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        }
        binding.mapView.onCreate(mapViewBundle)
    }

    private fun initViewModel() {
        val db = LocalDatabase.getInstance(requireContext())
        val localRepo = LocalRepository.getInstance(db.placeDao())
        viewModel = ViewModelProvider(
            this,
            GeoMapViewModelFactory(localRepo)
        ).get(GeoMapViewModel::class.java)
    }

    private fun initRecyclerView() {
        mAdapter = PlaceRecyclerAdapter(
            object : OnPlaceSelectedListener {
                override fun onDetailClicked(place: Place) {
                    val args = bundleOf(Constants.PLACE_BUNDLE_KEY to place)
                    openSaveLocationFragment(args)
                }

                override fun onLocationClicked(place: Place) {
                    val pos = LatLng(place.latitude, place.longitude)
                    addMarkerAt(pos)
                    moveCamera(pos)
                }
            }
        )
        binding.recyclerRouteInclude.rvMap.run {
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun observePlaces() {
        viewModel.places.observe(viewLifecycleOwner) { placeList ->
            mAdapter.clearAndAddPlaces(placeList)
        }
    }

    private fun observeDecodedPolyline() {
        viewModel.decodedPolyline.observe(viewLifecycleOwner) { points ->
            if (points.isEmpty()) {
                return@observe
            }
            val polylineOpt = PolylineOptions().addAll(points).color(Color.RED).width(5f)
            map.addPolyline(polylineOpt)
            val pos = LatLng(points[0].latitude, points[0].longitude)
            moveCamera(pos)
        }
    }

    private fun permsGranted() {
        LocationFinder.getLocation(requireActivity() as AppCompatActivity)
        var calledOnce = false
        LocationFinder.location.observe(viewLifecycleOwner) { location ->
            if (!this::map.isInitialized || location == null) {
                return@observe
            }
            if (!calledOnce) {
                moveCamera(LatLng(location.latitude, location.longitude))
                LocationFinder.stopLocationListening()
                createRouteButtonListener(location)
                calledOnce = true
            }
        }
    }

    private fun createRouteButtonListener(location: Location) {
        binding.recyclerRouteInclude.btnMapCreateRoute.setOnClickListener {
            val isInternetAvailable = Helper.isInternetAvailable(requireContext())
            if (!isInternetAvailable) {
                Toast.makeText(requireContext(), R.string.no_internet, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            UIState.state.value = UIState.Loading
            showAllMarkers()
            val places = mAdapter.getPlaces()
            val latLng = LatLng(location.latitude, location.longitude)
            viewModel.findPath(places, latLng)
        }
    }

    private fun showAllMarkers() {
        val places = mAdapter.getPlaces()
        for (place in places) {
            addMarkerAt(LatLng(place.latitude, place.longitude))
        }
    }

    override fun onMapReady(_map: HuaweiMap?) {
        if (_map == null) {
            return
        }
        map = _map.apply {
            if (LocationPermission.hasLocationPerms(requireActivity())) {
                isMyLocationEnabled = true
                uiSettings.isMyLocationButtonEnabled = true
            }
            uiSettings.setLogoPadding(0, 0, 0, 0)
            setOnMapClickListener { latLng ->
                clear()
                handleAddMarkerState(latLng)
                state = MapMarkerState.SAVE_MARKER
            }
        }
    }

    fun onPlacesButtonClick() {
        findNavController().navigate(R.id.action_geoMapFragment_to_savedPlaceFragment)
    }

    fun handleState() {
        state = if (state == MapMarkerState.ADD_MARKER) {
            handleAddMarkerState()
            MapMarkerState.SAVE_MARKER
        } else {
            handleSaveMarkerState()
            MapMarkerState.ADD_MARKER
        }
    }

    private fun handleAddMarkerState(latLng: LatLng? = null) {
        if (::map.isInitialized) {
            val centerPos = latLng ?: map.cameraPosition.target
            currentMarker = addMarkerAt(centerPos)
            currentMarker.showInfoWindow()
            setMarkerClickListener()
            setMarkerDragListener()
        }
        binding.btnMapAddLocation.setImageResource(R.drawable.ic_check_48)
    }

    private fun setMarkerClickListener() {
        map.setOnMarkerClickListener { marker ->
            if (marker == currentMarker) {
                setMarkerDraggableAndChangeColor(marker)
            }
            false
        }
    }

    private fun setMarkerDraggableAndChangeColor(marker: Marker) {
        marker.run {
            isDraggable = true
            setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
        }
    }

    private fun setMarkerDragListener() {
        map.setOnMarkerDragListener(object : HuaweiMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker?) {}
            override fun onMarkerDrag(p0: Marker?) {}
            override fun onMarkerDragEnd(marker: Marker?) {
                marker?.let { newMarker ->
                    currentMarker = newMarker
                }
            }
        })
    }

    private fun handleSaveMarkerState() {
        binding.btnMapAddLocation.setImageResource(R.drawable.ic_add_location_48)
        val args =
            bundleOf(Constants.MARKER_POS_KEY to currentMarker.position)
        openSaveLocationFragment(args)
    }

    private fun addMarkerAt(pos: LatLng) =
        map.addMarker(
            MarkerOptions()
                .title(getString(R.string.new_marker_title))
                .snippet(getString(R.string.new_marker_snippet))
                .position(pos)
        )

    private fun moveCamera(latLng: LatLng) {
        val zoom = CameraUpdateFactory.zoomTo(15f)
        map.animateCamera(zoom)
        val move = CameraUpdateFactory.newLatLng(latLng)
        map.animateCamera(move)
    }

    private fun openSaveLocationFragment(args: Bundle) {
        findNavController().navigate(R.id.action_geoMapFragment_to_saveLocationFragment, args)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGeomapBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            presenter = this@GeoMapFragment
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        binding.mapView.onStop()
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
        LocationFinder.stopLocationListening()
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        if (LocationPermission.hasLocationPerms(requireActivity())) {
            permsGranted()
        }
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        var mapViewBundle = outState.getBundle(Constants.MAPVIEW_BUNDLE_KEY)
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle(Constants.MAPVIEW_BUNDLE_KEY, mapViewBundle)
        }
        binding.mapView.onSaveInstanceState(mapViewBundle)
        super.onSaveInstanceState(outState)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        LocationPermission.onRequestPermissionsResult(requestCode, grantResults)
    }
}


