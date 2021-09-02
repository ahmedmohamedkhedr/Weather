package com.example.robustatask.ui.preview_image

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.robustatask.R
import com.example.robustatask.databinding.ActivityPreviewBinding
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import com.example.robustatask.utils.*
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.widget.ShareDialog
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
    }

    override fun onPreviewEntrance(imagePath: String) {
        ui.weatherImageView.loadImage(imagePath)
    }

    override fun onNotPreviewEntrance(imagePath: String) {
        ui.weatherImageView.loadImage(imagePath, object : OnImageLoad {
            override fun onFinished() {
                checkLocationPermission()
            }
        })
    }

    override fun onLoadWeatherDetailsSuccess(weather: WeatherModel) {
        ui.weatherImageView.showWeatherBanner(weather)
        presenter.prepareSavingStory(
            weather,
            createFileFromBitmap(
                this,
                ui.weatherImageView.generateBitmap()
            )
        )
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

    override fun onPrepareSavingStorySuccess(weatherStory: WeatherStoryModel) {
        presenter.saveWeatherStory(weatherStory)
    }

    override fun onSaveWeatherStorySuccess() {
        presenter.publishHistoryEvent()
        showToastMessage(this, getString(R.string.story_saved))
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

    companion object {
        const val ARG_IMAGE_FILE = "ARG_IMAGE_FILE"
        const val ARG_ENTRANCE_TYPE = "ARG_ENTRANCE_TYPE"

        fun start(context: Context, entranceType: EntranceType, imagePath: String?) {
            val intent = Intent(context, PreviewActivity::class.java)
            intent.putExtra(ARG_IMAGE_FILE, imagePath)
            intent.putExtra(ARG_ENTRANCE_TYPE, entranceType)
            context.startActivity(intent)
        }

        enum class EntranceType {
            PREVIEW_TYPE,
            OTHER
        }
    }
}

