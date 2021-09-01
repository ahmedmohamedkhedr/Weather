package com.example.robustatask.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.bumptech.glide.Glide
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


private const val PERMISSION_REQUEST_CODE = 0
const val CAMERA_REQUEST_CODE = 1
const val FULL_DATE_FORMAT = "dd-MMM-yy hh.mm aa"


fun createImageFile(context: Context): File? {
    val fileFormat = "yyyy-MM-dd-HH-mm-ss-SSS"
    val fileExtension = "jpg"
    val timeStamp: String = SimpleDateFormat(fileFormat, Locale.US).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile("JPEG_${timeStamp}_", ".$fileExtension", storageDir)
}


@SuppressLint("QueryPermissionsNeeded")
fun openCamera(context: Activity): String? {
    var pickedPhotoPath: String? = null
    Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
        takePictureIntent.resolveActivity(context.packageManager)?.also {
            createImageFile(context)?.also {
                pickedPhotoPath = it.absolutePath
                val photoUri: Uri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    it
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                context.startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE)
            }

        }
    }
    return pickedPhotoPath
}

private fun isPermissionGranted(context: Context, permissions: Array<String>): Boolean {
    var isGranted = false
    permissions.forEach {
        isGranted = ContextCompat.checkSelfPermission(
            context,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }
    return isGranted
}


fun isCameraPermissionGranted(context: Context): Boolean =
    isPermissionGranted(context, arrayOf(Manifest.permission.CAMERA))


fun isLocationPermissionGranted(context: Context): Boolean = isPermissionGranted(
    context,
    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
)

private fun requestPermission(context: Activity, permissions: Array<String>) {
    ActivityCompat.requestPermissions(
        context, permissions,
        PERMISSION_REQUEST_CODE
    )
}


fun requestCameraPermission(context: Activity) {
    requestPermission(context, arrayOf(Manifest.permission.CAMERA))
}


fun requestLocationPermission(context: Activity) {
    requestPermission(
        context,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}


fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT)
        .show()
}


fun ImageView.loadImage(src: Any?) {
    Glide.with(this).load(src)
        .into(this)
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}


fun Activity.setFullScreen() {
    this.requestWindowFeature(Window.FEATURE_NO_TITLE)
    this.window.setFlags(
        WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
    )
}


@SuppressLint("MissingPermission")
fun Activity.getLocation(): Location? {
    val lm =
        getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
}


fun longToString(timeInMillis: Long, dateFormat: String): String {
    val simpleDatFormat = SimpleDateFormat(dateFormat, Locale.US)
    return simpleDatFormat.format(Date(timeInMillis)).toString()
}

@SuppressLint("QueryPermissionsNeeded")
fun shareToFaceBook(activity: Activity, path: String) {
    val imageUri: Uri = FileProvider.getUriForFile(
        activity,
        "${activity.packageName}.provider",
        File(path)
    )
    val sendIntent = Intent(Intent.ACTION_VIEW)
    sendIntent.type = "image/*"
    sendIntent.action = Intent.ACTION_SEND
    sendIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
    sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

    val matches: List<ResolveInfo> = activity.packageManager
        .queryIntentActivities(sendIntent, PackageManager.MATCH_DEFAULT_ONLY)
    var faceBookExist = false
    for (info in matches) {
        if (info.activityInfo.name.contains("facebook")) {
            sendIntent.setPackage(info.activityInfo.packageName)
            faceBookExist = true
            break
        }
    }
    if (faceBookExist) {
        activity.startActivity(sendIntent)
    } else {
        openGooglePlayLink(activity, "com.facebook.katana")
    }
}

@SuppressLint("QueryPermissionsNeeded")
fun shareToTwitter(activity: Activity, path: String) {
    val imageUri: Uri = FileProvider.getUriForFile(
        activity,
        "${activity.packageName}.provider",
        File(path)
    )
    val tweetIntent = Intent(Intent.ACTION_SEND)
    tweetIntent.putExtra(Intent.EXTRA_STREAM, imageUri)
    tweetIntent.type = "image/jpeg"

    val matches: List<ResolveInfo> = activity.packageManager
        .queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY)
    var twitterExist = false
    for (info in matches) {
        if (info.activityInfo.name.contains("twitter")) {
            tweetIntent.setPackage(info.activityInfo.packageName)
            twitterExist = true
            break
        }
    }
    if (twitterExist) {
        activity.startActivity(tweetIntent)
    } else {
        openGooglePlayLink(activity, "com.twitter.android")
    }
}

private fun openGooglePlayLink(activity: Activity, packageName: String) {
    try {
        activity.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=$packageName")
            )
        )
    } catch (anfe: ActivityNotFoundException) {
        activity.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
            )
        )
    }
}
