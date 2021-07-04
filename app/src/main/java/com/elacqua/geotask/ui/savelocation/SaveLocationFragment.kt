package com.elacqua.geotask.ui.savelocation


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.elacqua.geotask.R
import com.elacqua.geotask.data.local.LocalDatabase
import com.elacqua.geotask.data.local.LocalRepository
import com.elacqua.geotask.data.model.Place
import com.elacqua.geotask.databinding.SaveLocationFragmentBinding
import com.elacqua.geotask.utils.Constants
import com.huawei.hms.maps.model.LatLng
import java.text.SimpleDateFormat
import java.util.*


class SaveLocationFragment : Fragment() {

    private lateinit var binding: SaveLocationFragmentBinding
    private lateinit var viewModel: SaveLocationViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        binding.txtSaveLocTitle.requestFocus()
        showKeyboard(binding.txtSaveLocTitle)
        initViewModel()

        val argPlace = arguments?.get(Constants.PLACE_BUNDLE_KEY) as Place?
        argPlace?.let {
            initViews(argPlace)
        }
    }

    private fun initViews(argPlace: Place) {
        binding.txtSaveLocTitle.setText(argPlace.title)
        binding.txtSaveLocDesc.setText(argPlace.description)
        binding.txtSaveLocLat.text = getString(R.string.location_lat, argPlace.latitude.toFloat())
        binding.txtSaveLocLong.text =
            getString(R.string.location_long, argPlace.longitude.toFloat())

        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val date = sdf.format(Date(argPlace.date.toLong()))
        binding.txtSaveLocDate.text = getString(R.string.location_date, date)
    }

    private fun showKeyboard(view: EditText) {
        view.requestFocus()
        view.postDelayed({
            requireActivity().window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
            val keyboard: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            keyboard.showSoftInput(view, 0)
        }, 100)
    }

    private fun initViewModel() {
        val db = LocalDatabase.getInstance(requireContext())
        val localRepo = LocalRepository.getInstance(db.placeDao())
        viewModel = ViewModelProvider(
            this,
            SaveLocationViewModelFactory(localRepo)
        ).get(SaveLocationViewModel::class.java)
    }

    fun onSubmitButtonClicked() {
        val markerPos = arguments?.get(Constants.MARKER_POS_KEY) as LatLng?
        val argPlace = arguments?.get(Constants.PLACE_BUNDLE_KEY) as Place?
        val location = if (argPlace == null) {
            markerPos!!
        } else {
            LatLng(argPlace.latitude, argPlace.longitude)
        }

        val title = binding.txtSaveLocTitle.text.toString()
        val description = binding.txtSaveLocDesc.text.toString()
        val date = System.currentTimeMillis()

        val place = Place(
            title = title,
            description = description,
            latitude = location.latitude,
            longitude = location.longitude,
            date = date.toString()
        )
        argPlace?.let {
            place.id = argPlace.id
        }
        viewModel.savePlace(place)
        findNavController().popBackStack()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SaveLocationFragmentBinding.inflate(inflater, container, false)
        binding.presenter = this
        return binding.root
    }

}