package com.rohan.pixd.ui.text

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.rohan.pixd.R

class TextFontsAdapter(
    var context: Context,
    var fontsCallbackListener: FontsCallbackListener
    ): RecyclerView.Adapter<TextFontsAdapter.ViewHolder>() {

    var data: ArrayList<Fonts> = ArrayList()
    var selected = 0

    init {
        /*data.add(R.font.abrilfatface_regular)
        data.add(R.font.bigshoulders_regular)
        data.add(R.font.lato_regular)
        data.add(R.font.manrope_regular)
        data.add(R.font.montserrat_regular)
        data.add(R.font.librebaskerville_regular)
        //data.add(R.font.notosansjp_regular)
        data.add(R.font.playfairdisplay_regular)
        data.add(R.font.roboto_regular)
        data.add(R.font.rubik_regular)*/
        data.add(Fonts.ABRIL)
        data.add(Fonts.BIGSHOULDER)
        data.add(Fonts.LATO)
        data.add(Fonts.MANROPE)
        data.add(Fonts.MONTSERRAT)
        data.add(Fonts.LIBRE)
        data.add(Fonts.PLAY)
        data.add(Fonts.ROBOTO)
        data.add(Fonts.RUBIK)
    }

    enum class Fonts(val font: Int, val _name: String) {
        ABRIL(R.font.abrilfatface_regular, "abril"),
        BIGSHOULDER(R.font.bigshoulders_regular, "big shoulder"),
        LATO(R.font.lato_regular, "lato"),
        MANROPE(R.font.manrope_regular, "manrope"),
        MONTSERRAT(R.font.montserrat_regular, "montserrat"),
        LIBRE(R.font.librebaskerville_regular, "librebaskerville"),
        PLAY(R.font.playfairdisplay_regular, "playfairdisplay"),
        ROBOTO(R.font.roboto_regular, "roboto"),
        RUBIK(R.font.rubik_regular, "rubik");
    }

    interface FontsCallbackListener{
        fun fontsCallback(fontsDrawable: Int)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textFonts: TextView? = null
        var imageDone: ImageView? = null
        init {
            textFonts = itemView.findViewById(R.id.textFonts)
            imageDone = itemView.findViewById(R.id.imageDone)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_fonts, parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return data.size;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*if(selected == position){
            holder.imageDone?.visibility = View.VISIBLE
        } else {
            holder.imageDone?.visibility = View.INVISIBLE
        }*/

        holder.textFonts?.let {
            it.text = data[position]._name
            it.typeface = ResourcesCompat.getFont(context, data[position].font)
            it.setOnClickListener {
                selected = position;
                fontsCallbackListener.fontsCallback(data[position].font)
                //notifyDataSetChanged()
            }
        }
    }

}