package com.example.robustatask.ui.bannerImageView

import android.annotation.SuppressLint
import android.content.Context
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

    // Paint object for coloring and styling
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
            drawTemperature(
                canvas,
                this.temp.toString(),
                this.maxTemp.toString(),
                this.minTemp.toString()
            )
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
        paint.color = Color.WHITE
        paint.textSize = 40f
        drawableToBitmap(ContextCompat.getDrawable(context, R.drawable.ic_location))?.let {
            canvas.drawBitmap(it, 10f, 50f, paint)
        }
        locationNme?.let { canvas.drawText(it, 90f, 90f, paint) }
    }


    private fun drawTemperature(canvas: Canvas, temp: String, maxTem: String, minTemp: String) {
        paint.color = Color.WHITE
        weatherInfo?.let { model ->
            val drawable =
                model.tempIcon?.let { resId -> ContextCompat.getDrawable(context, resId) }
            val bitmap = drawableToBitmap(drawable)
            bitmap?.let {
                canvas.drawBitmap(it, -50f, 50f, paint)
                paint.textSize = 100f
                canvas.drawText(
                    "$temp °C",
                    390f,
                    measuredHeight.minus(measuredHeight.minus(250)).toFloat(),
                    paint
                )
            }
        }
    }

    private fun drawFeelLikeText(canvas: Canvas, feelLike: String) {
        paint.color = Color.WHITE
        paint.textSize = 40f
        canvas.drawText(
            "${context.getString(R.string.feels_like)} $feelLike °C",
            390f,
            measuredHeight.minus(measuredHeight.minus(320)).toFloat(),
            paint
        )
    }

    private fun drawWeatherDescText(canvas: Canvas, weatherDesc: String?) {
        weatherDesc?.let {
            paint.color = Color.WHITE
            paint.textSize = 40f
            canvas.drawText(
                it,
                390f,
                measuredHeight.minus(measuredHeight.minus(390)).toFloat(),
                paint
            )
        }
    }

    private fun drawUpdatedAtText(canvas: Canvas, updatedAt: String?) {
        updatedAt?.let {
            paint.color = Color.WHITE
            paint.textSize = 40f
            canvas.drawText(
                it,
                390f,
                measuredHeight.minus(measuredHeight.minus(460)).toFloat(),
                paint
            )
        }
    }

    private fun drawWindSpeedText(canvas: Canvas, updatedAt: String?) {
        updatedAt?.let {
            paint.color = Color.WHITE
            paint.textSize = 40f
            canvas.drawText(
                it,
                20f,
                measuredHeight.minus(measuredHeight.minus(560)).toFloat(),
                paint
            )
        }
    }

    private fun drawHumidityText(canvas: Canvas, humidity: String?) {
        humidity?.let {
            paint.color = Color.WHITE
            paint.textSize = 40f
            canvas.drawText(
                it,
                20f,
                measuredHeight.minus(measuredHeight.minus(630)).toFloat(),
                paint
            )
        }
    }

    private fun drawPressureText(canvas: Canvas, pressure: String?) {
        pressure?.let {
            paint.color = Color.WHITE
            paint.textSize = 40f
            canvas.drawText(
                it,
                20f,
                measuredHeight.minus(measuredHeight.minus(700)).toFloat(),
                paint
            )
        }
    }


    fun showWeatherBanner(weatherModel: WeatherModel) {
        this.weatherInfo = weatherModel
        invalidate()
    }

}