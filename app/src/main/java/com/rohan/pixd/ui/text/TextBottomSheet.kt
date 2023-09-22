package com.rohan.pixd.ui.text

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohan.pixd.R


class TextBottomSheet(
    var string: String?, var fonts: Int?, var color: Int?, var bgcolor: Int?
): BottomSheetDialogFragment(),
    TextFontsAdapter.FontsCallbackListener,
    TextColorAdapter.ColorCallbackListener, TextBackgroundAdapter.BackgroundColorCallbackListener {

    var rvColor: RecyclerView? = null;
    var rvBgColor: RecyclerView? = null;
    var rvFonts: RecyclerView? = null;
    var edittext: EditText? = null;
    var buttonAdd: Button? = null;
    /*var fonts: Int? = null;
    var color: Int? = null;
    var bgcolor: Int? = null;*/
    private var mListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_text, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        edittext = view.findViewById(R.id.edittext)
        buttonAdd = view.findViewById(R.id.buttonAdd)
        rvColor = view.findViewById(R.id.rvColor)
        rvBgColor = view.findViewById(R.id.rvBgColor)
        rvFonts = view.findViewById(R.id.rvFonts)

        val bgAdapter = TextBackgroundAdapter(requireContext(), this)
        rvBgColor?.adapter = bgAdapter;

        val colorAdapter = TextColorAdapter(requireContext(), this)
        rvColor?.adapter = colorAdapter;

        val fontAdapter = TextFontsAdapter(requireContext(), this)
        val manager = GridLayoutManager(requireContext(), 2)
        rvFonts?.layoutManager = manager;
        rvFonts?.adapter = fontAdapter;

        buttonAdd?.setOnClickListener {
            mListener?.onTextDataReceived(edittext?.text.toString(), fonts, color, bgcolor)
        }

        edittext?.setText(string);
        fonts?.let {
            edittext?.typeface = ResourcesCompat.getFont(requireContext(), fonts!!)
        }
        color?.let {
            edittext?.setTextColor(requireContext().resources.getColor(color!!))
        }
        bgcolor?.let {
            edittext?.setBackgroundColor(resources.getColor(it))
        }

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            mListener = context as BottomSheetListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement BottomSheetListener")
        }
    }


    interface BottomSheetListener {
        fun onTextDataReceived(string: String, font: Int?, color: Int?, bgColor: Int?)
    }

    override fun fontsCallback(fontsDrawable: Int) {
        fonts = fontsDrawable;
        edittext?.typeface = ResourcesCompat.getFont(requireContext(), fontsDrawable)
    }

    override fun colorCallback(colorDrawable: Int) {
        color = colorDrawable;
        edittext?.setTextColor(requireContext().resources.getColor(colorDrawable))
    }

    override fun bgColorCallback(color: Int?) {
        bgcolor = color;
        bgcolor?.let {
            edittext?.setBackgroundColor(resources.getColor(it))
        }?: kotlin.run {
            edittext?.setBackgroundColor(Color.TRANSPARENT);
        }
    }

    /*fun provideData(string: String?, font: Int?, color: Int?, bgColor: Int?){
        this.fonts = font;
        this.color = color;
        this.bgcolor = bgColor;

        edittext?.setText(string);
        font?.let {
            edittext?.typeface = ResourcesCompat.getFont(requireContext(), font)
        }
        color?.let {
            edittext?.setTextColor(requireContext().resources.getColor(color))
        }
        bgcolor?.let {
            edittext?.setBackgroundColor(resources.getColor(it))
        }
    }*/

}
