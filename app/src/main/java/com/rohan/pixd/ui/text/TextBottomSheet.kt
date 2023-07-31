package com.rohan.pixd.ui.text

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohan.pixd.R


class TextBottomSheet: BottomSheetDialogFragment() {

    var edittext: EditText? = null;
    var buttonAdd: Button? = null;
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

        buttonAdd?.setOnClickListener {
            mListener?.onTextDataReceived(edittext?.text.toString())
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
        fun onTextDataReceived(string: String)
    }
}
