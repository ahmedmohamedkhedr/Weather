package com.example.robustatask.ui.main_activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.robustatask.R
import com.example.robustatask.databinding.ActivityMainBinding
import com.example.robustatask.ui.preview_image.PreviewActivity
import com.example.robustatask.utils.*
import org.koin.android.ext.android.inject
import java.io.File

class MainActivity : AppCompatActivity(), MainActivityContract.View {
    private val presenter: MainActivityContract.Presenter by inject()
    private lateinit var ui: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(LayoutInflater.from(this))
        presenter.attachView(this, lifecycle)
        setContentView(ui.root)
        setupViewControllers()
    }


    private fun setupViewControllers() {
        with(ui) {
            openCameraBtn.setOnClickListener {
                checkCameraPermission()
            }
        }
    }

    private fun checkCameraPermission() {
        if (isCameraPermissionGranted(this)) {
            onPermissionGranted()
        } else {
            onPermissionNotGranted()
        }
    }


    override fun onPermissionGranted() {
        presenter.setPickedImagePath(openCamera(this))
    }

    override fun onPermissionNotGranted() {
        requestCameraPermission(this)
    }

    override fun onPickImageSuccess(imagePath: String) {
        navigateToPreviewActivity(imagePath)
    }

    private fun navigateToPreviewActivity(imagePath: String) {
        val intent = Intent(this, PreviewActivity::class.java)
        intent.putExtra(ARG_IMAGE_FILE, imagePath)
        startActivity(intent)
    }


    override fun onPickImageError() {
        showError(getString(R.string.an_error_happened))
    }

    override fun showError(err: String?) {
        err?.let { showToastMessage(this, it) }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.attachView(this, lifecycle)
        checkCameraPermission()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            presenter.checkDataFromActivityResult()
        }
    }


    companion object {
        const val ARG_IMAGE_FILE = "ARG_IMAGE_FILE"
    }

}