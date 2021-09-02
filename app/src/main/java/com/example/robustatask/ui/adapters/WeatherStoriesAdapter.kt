package com.example.robustatask.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.robustatask.databinding.ItemWeatherHistoryLayoutBinding
import com.example.robustatask.domain.pojos.models.WeatherStoryModel
import com.example.robustatask.utils.FULL_DATE_FORMAT
import com.example.robustatask.utils.loadCircularImage
import com.example.robustatask.utils.longToString

class WeatherStoriesAdapter(private val listener: StoryListener) :
    RecyclerView.Adapter<WeatherStoriesAdapter.ViewHolder>() {

    private var storiesList = mutableListOf<WeatherStoryModel>()

    inner class ViewHolder(private val viewBinding: ItemWeatherHistoryLayoutBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            viewBinding.deleteStoryBtn.setOnClickListener {
                listener.onRemoveStory(storiesList[adapterPosition].ID, adapterPosition)
            }
        }


        fun bind(story: WeatherStoryModel) = with(viewBinding) {
            storyImageView.loadCircularImage(story.thumbnail)
            tempTextView.text = story.temp
            weatherDesc.text = story.weatherDesc
            location.text = story.location
            storyCreatedAtTextView.text = longToString(story.createdAt, FULL_DATE_FORMAT)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WeatherStoriesAdapter.ViewHolder {
        val viewBinding = ItemWeatherHistoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: WeatherStoriesAdapter.ViewHolder, position: Int) {
        holder.bind(storiesList[position])
    }

    override fun getItemCount(): Int = storiesList.size

    fun pushData(data: MutableList<WeatherStoryModel>) {
        storiesList = data
        notifyDataSetChanged()
    }

    fun removeItem(position: Int) {
        storiesList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun getData(): MutableList<WeatherStoryModel> = storiesList

    interface StoryListener {
        fun onRemoveStory(storyId: String, position: Int)
    }
}