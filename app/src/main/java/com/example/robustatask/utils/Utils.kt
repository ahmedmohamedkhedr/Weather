package com.example.robustatask.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.robustatask.R
import com.example.robustatask.databinding.ShareBottomSheetLayoutBinding
import com.example.robustatask.ui.preview_image.PreviewActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
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


fun createFileFromBitmap(context: Context, bitmap: Bitmap): File {
    val fileFormat = "yyyy-MM-dd-HH-mm-ss-SSS"
    val timeStamp: String = SimpleDateFormat(fileFormat, Locale.US).format(Date())
    val path: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    val file = File(path, "WeatherStatus$timeStamp.jpg")
    var fileOutPutStream: FileOutputStream? = null
    try {
        fileOutPutStream = FileOutputStream(file)
        bitmap.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            fileOutPutStream
        )
    } catch (e: FileNotFoundException) {
        e.printStackTrace()

    } finally {
        if (fileOutPutStream != null) {
            try {
                fileOutPutStream.flush()
                fileOutPutStream.getFD().sync()
                fileOutPutStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
    MediaStore.Images.Media.insertImage(
        context.contentResolver,
        file.absolutePath,
        file.name,
        file.name
    )
    return file
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


fun ImageView.loadCircularImage(src: Any?, placeholderDrawable: Drawable? = null) {
    Glide.with(this).load(src)
        .transform(MultiTransformation<Bitmap>(CenterCrop(), CircleCrop()))
        .placeholder(placeholderDrawable)
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

fun drawableToBitmap(drawable: Drawable?): Bitmap? {
    drawable?.let {
        var bitmap: Bitmap? = null
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        bitmap = if (drawable.intrinsicWidth <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(
                1,
                1,
                Bitmap.Config.ARGB_8888
            )
        } else {
            Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }
    return null
}


fun PreviewActivity.showShareBottomSheet(): ShareBottomSheetLayoutBinding {
    val bottomSheet = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
    val binding = ShareBottomSheetLayoutBinding.inflate(LayoutInflater.from(this))
    bottomSheet.setContentView(binding.root)
    bottomSheet.show()
    return binding
}


fun ImageView.generateBitmap(): Bitmap {
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.RGB_565)
    val canvas = Canvas(bitmap)
    this.draw(canvas)
    return bitmap
}
