package com.rohan.pixd.ui.text

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.rohan.pixd.R

class TextColorAdapter(
    var context: Context,
    var colorCallbackListener: ColorCallbackListener
    ): RecyclerView.Adapter<TextColorAdapter.ViewHolder>() {

    var data: ArrayList<Int> = ArrayList()

    init {
        data.add(R.color.black)
        data.add(R.color.white)
        data.add(R.color.orange)
        data.add(R.color.green)
        data.add(R.color.blue)
        data.add(R.color.purple)
        data.add(R.color.yellow)
        data.add(R.color.red)
        data.add(R.color.skyblue)
        data.add(R.color.pink)
    }

    interface ColorCallbackListener{
        fun colorCallback(colorDrawable: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var frameColors: FrameLayout? = null
        init {
            frameColors = itemView.findViewById(R.id.frameColors)
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
        holder.frameColors?.let {
            it.setBackgroundColor(context.resources.getColor(data[position]));
            it.setOnClickListener {
                colorCallbackListener.colorCallback(data[position])
            }
        }
    }

}