package com.do_not_douch.antitheifproject.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.ItemDetectionSoundGridBinding
import com.do_not_douch.antitheifproject.model.SoundModel

class SoundSelectGridAdapter (
    private var clickItem: ((Int) -> Unit),private var soundData: ArrayList<SoundModel>
) : RecyclerView.Adapter<SoundSelectGridAdapter.ViewHolder>() {
    class ViewHolder(val binding: ItemDetectionSoundGridBinding) : RecyclerView.ViewHolder(binding.root)

    var context: Context? = null
    private var playingPosition: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemDetectionSoundGridBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return soundData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.countryName.text = soundData[position].soundName
        holder.binding.radioButton.isChecked = soundData[position].isCheck

        holder.binding.mainItem.setOnClickListener {
            if (playingPosition == position) {
                // If the current item is playing, stop it
                playingPosition = -1
                notifyItemChanged(position) // Update UI
            } else {
                // Play the new item
                val oldPosition = playingPosition
                playingPosition = position
                notifyItemChanged(oldPosition) // Update the previous playing item
                notifyItemChanged(playingPosition) // Update the new playing item
            }
            checkSingleBox(position)
            clickItem.invoke(position)
        }
        holder.binding.radioButton.setOnClickListener {
            if (playingPosition == position) {
                // If the current item is playing, stop it
                playingPosition = -1
                notifyItemChanged(position) // Update UI
            } else {
                // Play the new item
                val oldPosition = playingPosition
                playingPosition = position
                notifyItemChanged(oldPosition) // Update the previous playing item
                notifyItemChanged(playingPosition) // Update the new playing item
            }
            checkSingleBox(position)
            clickItem.invoke(position)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun selectSound(langPositionSelected: Int) {
        for (i in soundData.indices) {
            soundData[i].isCheck = i == langPositionSelected
        }

        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun checkSingleBox(pos: Int) {
        for (i in soundData.indices) {
            soundData[i].isCheck = i == pos
        }
        notifyDataSetChanged()
    }
    fun onSoundComplete() {
        val oldPosition = playingPosition
        playingPosition = -1
        notifyItemChanged(oldPosition) // Reset the icon of the item that was playing
    }

}