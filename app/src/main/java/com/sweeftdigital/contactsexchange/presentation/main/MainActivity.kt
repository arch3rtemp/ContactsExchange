package com.sweeftdigital.contactsexchange.presentation.main

import android.Manifest
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.findNavController
import com.sweeftdigital.contactsexchange.R
import com.sweeftdigital.contactsexchange.databinding.ActivityMainBinding
import com.sweeftdigital.contactsexchange.presentation.qr.QrActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        viewModel.onPermissionResult(granted)
    }

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val toastText = resources.getString(R.string.contact_added)
            Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeLiveViewState()
        setListeners()
    }

    private fun setListeners() {
        with(binding) {
            llQrScanner.setOnClickListener {
                requestPermission.launch(Manifest.permission.CAMERA)
            }
            llBack.setOnClickListener {
                fragmentContainerView.findNavController().popBackStack()
            }
        }
    }

    private fun subscribeLiveViewState() {
        viewModel.liveViewState.observe(this) { state ->
            state.isCameraPermissionGranted?.let { granted ->
                if (granted) {
                    val intent = Intent(this, QrActivity::class.java)
                    resultLauncher.launch(intent)
                } else {
                    val toastText = resources.getString(R.string.permission_denied)
                    Toast.makeText(this, toastText, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}