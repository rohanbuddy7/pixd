package com.rohan.pixd.ui.text

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rohan.pixd.R

class TextBackgroundAdapter(
    var context: Context,
    var bgcolorCallbackListener: BackgroundColorCallbackListener
    ): RecyclerView.Adapter<TextBackgroundAdapter.ViewHolder>() {

    var data: ArrayList<Int> = ArrayList()

    init {
        data.add(R.drawable.ic_none)
        data.add(R.color.black)
        data.add(R.color.white)
    }

    interface BackgroundColorCallbackListener{
        fun bgColorCallback(colorDrawable: Int?)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var frameBg: FrameLayout? = null
        var imageBg: ImageView? = null
        init {
            frameBg = itemView.findViewById(R.id.frameColors)
            imageBg = itemView.findViewById(R.id.imageColors)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_colors, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(position == 0){
            holder.imageBg?.visibility = View.VISIBLE;
            holder.frameBg?.visibility = View.GONE;

            holder.imageBg?.let {
                Glide.with(context).load(data[position]).into(it)
                it.setOnClickListener {
                    bgcolorCallbackListener.bgColorCallback(null)
                }
            }
        } else {
            holder.imageBg?.visibility = View.GONE;
            holder.frameBg?.visibility = View.VISIBLE;

            holder.frameBg?.let {
                it.setBackgroundColor(context.resources.getColor(data[position]));
                it.setOnClickListener {
                    bgcolorCallbackListener.bgColorCallback(data[position])
                }
            }
        }
    }

}