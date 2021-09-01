package com.example.robustatask.ui.bannerImageView

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.robustatask.R
import com.example.robustatask.domain.pojos.enums.WindDirectionEnum
import com.example.robustatask.domain.pojos.models.WeatherModel
import com.example.robustatask.utils.FULL_DATE_FORMAT
import com.example.robustatask.utils.drawableToBitmap
import com.example.robustatask.utils.longToString
import kotlin.math.min


@SuppressLint("AppCompatCustomView")
class BannerImageView(context: Context, attrs: AttributeSet) : ImageView(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private var bannerSize = 0

    private var weatherInfo: WeatherModel? = null


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            if (weatherInfo != null) {
                drawBanner(this)
                drawWeatherDetails(canvas)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        bannerSize = min(measuredWidth, measuredHeight)
    }

    private fun drawBanner(canvas: Canvas) {
        paint.color = Color.parseColor("#60000000")
        paint.style = Paint.Style.FILL
        canvas.drawRect(0f, 0f, width.toFloat(), bannerSize.toFloat(), paint)
    }

    private fun drawWeatherDetails(canvas: Canvas) {
        weatherInfo?.apply {
            drawLocation(canvas, this.cityName)
            drawTemperature(canvas, this.temp.toString())
            drawFeelLikeText(canvas, this.feelsLike.toString())
            drawWeatherDescText(canvas, this.tempDescription)
            drawUpdatedAtText(canvas, longToString(this.updatedAt ?: 0L, FULL_DATE_FORMAT))
            val strWindDirection = resources.getStringArray(R.array.WindDirections)
                .elementAt(WindDirectionEnum.values().indexOf(this.windDirection))
            drawWindSpeedText(
                canvas,
                "${context.getString(R.string.wind_speed)} ${this.windSpeed}m/s $strWindDirection"
            )
            drawHumidityText(canvas, "${context.getString(R.string.humidity)} ${this.humidity}%")
            drawPressureText(canvas, "${context.getString(R.string.pressure)} ${this.pressure}hPa")
        }
    }


    private fun drawLocation(canvas: Canvas, locationNme: String?) {
        drawBitmap(
            canvas,
            drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_location)),
            10f,
            50f
        )
        locationNme?.let { drawText(canvas, it, 90f, 90f, 40f) }
    }


    private fun drawTemperature(canvas: Canvas, temp: String) {
        weatherInfo?.let { model ->
            val drawable =
                model.tempIcon?.let { resId -> ContextCompat.getDrawable(context, resId) }
            val bitmap = drawableToBitmap(drawable)
            drawBitmap(canvas, bitmap, -50f, 50f)
            drawText(canvas, "$temp °C", 390f, 250f, 100f)
        }
    }

    private fun drawFeelLikeText(canvas: Canvas, feelLike: String) {
        drawText(canvas, "${context.getString(R.string.feels_like)} $feelLike °C", 390f, 320f, 40f)
    }

    private fun drawWeatherDescText(canvas: Canvas, weatherDesc: String?) {
        weatherDesc?.let {
            drawText(canvas, it, 390f, 390f, 40f)
        }
    }

    private fun drawUpdatedAtText(canvas: Canvas, updatedAt: String?) {
        updatedAt?.let {
            drawText(canvas, it, 390f, 460f, 40f)
        }
    }

    private fun drawWindSpeedText(canvas: Canvas, updatedAt: String?) {
        updatedAt?.let {
            drawText(canvas, it, 20f, 560f, 40f)
        }
    }

    private fun drawHumidityText(canvas: Canvas, humidity: String?) {
        humidity?.let {
            drawText(canvas, it, 20f, 630f, 40f)
        }
    }

    private fun drawPressureText(canvas: Canvas, pressure: String?) {
        pressure?.let {
            drawText(canvas, it, 20f, 700f, 40f)
        }
    }

    private fun drawText(canvas: Canvas, text: String, x: Float, y: Float, textSize: Float) {
        paint.color = Color.WHITE
        paint.textSize = textSize
        canvas.drawText(
            text,
            x,
            measuredHeight.minus(measuredHeight.minus(y)),
            paint
        )
    }

    private fun drawBitmap(canvas: Canvas, bitmap: Bitmap?, left: Float, top: Float) {
        paint.color = Color.WHITE
        bitmap?.let {
            canvas.drawBitmap(it, left, top, paint)
        }
    }


    fun showWeatherBanner(weatherModel: WeatherModel) {
        this.weatherInfo = weatherModel
        invalidate()
    }

}