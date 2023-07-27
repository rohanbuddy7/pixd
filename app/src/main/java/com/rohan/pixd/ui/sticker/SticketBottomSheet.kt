package com.rohan.pixd.ui.sticker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rohan.pixd.R


class SticketBottomSheet: BottomSheetDialogFragment(), StickerAdapter.StickerCallbackListener {

    var rv_sticker: RecyclerView? = null;
    var stickerAdapter: StickerAdapter? = null;
    private var mListener: BottomSheetListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rv_sticker = view.findViewById<RecyclerView>(R.id.rv_sticker)

        stickerAdapter = StickerAdapter(requireContext(),this);
        rv_sticker?.adapter = stickerAdapter;

    }

    override fun stickerCallback(stickerDrawable: Int) {
        mListener?.onStickerDataReceived(stickerDrawable)
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
        fun onStickerDataReceived(int: Int)
    }
}
