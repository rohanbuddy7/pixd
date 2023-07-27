package com.rohan.pixd.ui.sticker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohan.pixd.R

class StickerAdapter(
    var context: Context,
    var stickerCallbackListener: StickerAdapter.StickerCallbackListener
    ): RecyclerView.Adapter<StickerAdapter.ViewHolder>() {

    var stickers: ArrayList<Int> = ArrayList()

    init {
        stickers.add(R.drawable.sticker_beach)
        stickers.add(R.drawable.sticker_calendar)
        stickers.add(R.drawable.sticker_gift)
        stickers.add(R.drawable.sticker_goggles)
        stickers.add(R.drawable.sticker_holiday)
        stickers.add(R.drawable.sticker_science_1)
        stickers.add(R.drawable.sticker_science_2)
        stickers.add(R.drawable.sticker_skis)
        stickers.add(R.drawable.sticker_sports)
        stickers.add(R.drawable.sticker_sports_1)
        stickers.add(R.drawable.sticker_stadium)
        stickers.add(R.drawable.sticker_sunglasses)
        stickers.add(R.drawable.sticker_tent)
        stickers.add(R.drawable.sticker_travel)
        stickers.add(R.drawable.sticker_xmas_tree)
    }

    interface StickerCallbackListener{
        fun stickerCallback(stickerDrawable: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView? = null
        init {
            imageView = itemView.findViewById(R.id.iv_sticker)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_sticker, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return stickers.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.imageView?.let {
            Glide.with(context).load(stickers[position]).into(it);
            it.setOnClickListener {
                stickerCallbackListener.stickerCallback(stickers[position])
            }
        }
    }

}