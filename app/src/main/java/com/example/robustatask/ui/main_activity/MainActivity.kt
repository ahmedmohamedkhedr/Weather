package com.example.robustatask.ui.main_activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.example.robustatask.R
import com.example.robustatask.databinding.ActivityMainBinding
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import com.example.robustatask.ui.adapters.WeatherStoriesAdapter
import com.example.robustatask.ui.preview_image.PreviewActivity
import com.example.robustatask.utils.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), MainActivityContract.View {
    private val presenter: MainActivityContract.Presenter by inject()
    private lateinit var ui: ActivityMainBinding

    private val storiesAdapter: WeatherStoriesAdapter by lazy {
        WeatherStoriesAdapter(object : WeatherStoriesAdapter.StoryListener {
            override fun onRemoveStory(storyId: String, position: Int) {
                presenter.deleteStory(position, storyId)
            }

            override fun onItemClick(itemPath: String) {
                navigateToPreviewActivity(
                    itemPath,
                    PreviewActivity.Companion.EntranceType.PREVIEW_TYPE
                )
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(LayoutInflater.from(this))
        presenter.attachView(this, lifecycle)
        setContentView(ui.root)
        initClickListeners()
        setupRecyclerView()
        presenter.loadHistory()
    }


    private fun initClickListeners() {
        with(ui) {
            openCameraBtn.setOnClickListener {
                checkCameraPermission()
            }
        }
    }

    private fun setupRecyclerView() {
        ui.historyRecyclerView.adapter = storiesAdapter
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
        navigateToPreviewActivity(imagePath, PreviewActivity.Companion.EntranceType.OTHER)
    }

    private fun navigateToPreviewActivity(
        imagePath: String,
        entranceType: PreviewActivity.Companion.EntranceType
    ) {
        PreviewActivity.start(this, entranceType, imagePath)
    }


    override fun onPickImageError() {
        showError(getString(R.string.an_error_happened))
    }

    override fun onLoadHistorySuccess(historyStories: MutableList<WeatherStoryModel>?) {
        presenter.checkLoadedHistoryList(historyStories)
    }

    override fun onDeleteStorySuccess(position: Int) {
        storiesAdapter.removeItem(position)
    }

    override fun onHistoryEmpty() {
        ui.historyRecyclerView.gone()
        ui.emptyImageView.show()
    }

    override fun onHistoryNotEmpty(history: MutableList<WeatherStoryModel>) {
        ui.emptyImageView.gone()
        ui.historyRecyclerView.show()
        storiesAdapter.pushData(history)
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
}