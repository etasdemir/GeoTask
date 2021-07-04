package com.elacqua.geotask.ui.main

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.elacqua.geotask.R
import com.elacqua.geotask.databinding.ActivityMainBinding
import com.elacqua.geotask.utils.UIState
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        observeState()
    }

    private fun observeState() {
        UIState.state.observe(this) { state ->
            when (state) {
                is UIState.Success -> {
                    binding.progressBarMain.visibility = View.GONE
                    binding.viewProgressMain.visibility = View.GONE
                }
                is UIState.Error -> {
                    binding.progressBarMain.visibility = View.GONE
                    binding.viewProgressMain.visibility = View.GONE
                    Timber.e("Error: ${state.exception?.stackTraceToString()}")
                    Toast.makeText(
                        this,
                        getString(R.string.error, state.errorMessage),
                        Toast.LENGTH_LONG
                    ).show()
                }
                is UIState.Loading -> {
                    binding.progressBarMain.visibility = View.VISIBLE
                    binding.viewProgressMain.visibility = View.VISIBLE
                }
            }
        }
    }
}