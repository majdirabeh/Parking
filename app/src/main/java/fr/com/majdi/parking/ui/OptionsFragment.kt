package fr.com.majdi.parking.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import fr.com.majdi.parking.R
import fr.com.majdi.parking.model.Fields
import fr.com.majdi.parking.model.ResponseParking

/**
 * Created by Majdi RABEH on 09/05/2021.
 * Email m.rabeh.majdi@gmail.com
 */
class OptionsFragment : BottomSheetDialogFragment() {

    private lateinit var fields: Fields

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            fields = arguments!!.getSerializable("Fields") as Fields
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_options, container, false)
        setUpViews(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    @SuppressLint("SetTextI18n")
    private fun setUpViews(view: View) {
        // We can have cross button on the top right corner for providing elemnet to dismiss the bottom sheet
        //iv_close.setOnClickListener { dismissAllowingStateLoss() }
        view.setOnClickListener {
            dismissAllowingStateLoss()
            mListener?.onItemClick("Download")

        }
        view.findViewById<TextView>(R.id.name_parking).text = fields.name
        view.findViewById<TextView>(R.id.free_parking).text = fields.dispo.toString() + " places"
        view.findViewById<TextView>(R.id.capacity_parking).text =
            "Capacity : " + fields.total + " places"
    }

    private var mListener: ItemClickListener? = null


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ItemClickListener) {
            mListener = context as ItemClickListener
        } else {
            throw RuntimeException(
                context.toString()
                    .toString() + " must implement ItemClickListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface ItemClickListener {
        fun onItemClick(item: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(bundle: Bundle): OptionsFragment {
            val fragment = OptionsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}