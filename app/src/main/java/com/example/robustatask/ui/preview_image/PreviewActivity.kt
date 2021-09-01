package com.example.robustatask.ui.preview_image

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.example.robustatask.R
import com.example.robustatask.databinding.ActivityPreviewBinding
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.utils.*
import org.koin.android.ext.android.inject

class PreviewActivity : AppCompatActivity(), PreviewActivityContract.View {
    private lateinit var ui: ActivityPreviewBinding
    private val presenter: PreviewActivityContract.Presenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFullScreen()
        ui = ActivityPreviewBinding.inflate(LayoutInflater.from(this))
        setContentView(ui.root)
        presenter.attachView(this, lifecycle)
        initClickListeners()
        presenter.getActivityIntent(intent)
        checkLocationPermission()
    }

    override fun onFetchActivityArgs(imagePath: String) {
        ui.weatherImageView.loadImage(imagePath)
    }

    override fun onLoadWeatherDetailsSuccess(weather: WeatherModel) {
        ui.weatherImageView.showWeatherBanner(weather)
    }

    override fun onGetLatLon(lat: Double, lon: Double) {
        presenter.loadWeatherDetails(lat, lon)
    }

    override fun onPrepareFacebookSharingSuccess(storyPath: String) {
        shareToFaceBook(this, storyPath)
    }

    override fun onPrepareTwitterSharingSuccess(storyPath: String) {
        shareToTwitter(this, storyPath)
    }


    override fun showLoading() {
        ui.progressBar.show()
    }

    override fun hideLoading() {
        ui.progressBar.gone()
    }

    override fun showError(err: String?) {
        err?.let { showToastMessage(this, it) }
    }

    override fun onPermissionGranted() {
        presenter.getLatLon(getLocation())
    }

    override fun onPermissionNotGranted() {
        requestLocationPermission(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        presenter.attachView(this, lifecycle)
        checkLocationPermission()
    }

    private fun checkLocationPermission() {
        if (isLocationPermissionGranted(this)) {
            onPermissionGranted()
        } else {
            onPermissionNotGranted()
        }
    }

    private fun initClickListeners() {
        with(ui) {
            shareBtn.setOnClickListener {
                initBottomSheet()
            }
        }
    }

    private fun initBottomSheet() {
        with(showShareBottomSheet()) {
            storyImageView.loadCircularImage(ui.weatherImageView.generateBitmap())
            this.shareFbBtn.setOnClickListener {
                presenter.shareStoryToFacebook(
                    createFileFromBitmap(
                        this@PreviewActivity,
                        ui.weatherImageView.generateBitmap()
                    )
                )
            }

            this.shareTwBtn.setOnClickListener {
                presenter.shareStoryToTwitter(
                    createFileFromBitmap(
                        this@PreviewActivity,
                        ui.weatherImageView.generateBitmap()
                    )
                )
            }
        }
    }
}