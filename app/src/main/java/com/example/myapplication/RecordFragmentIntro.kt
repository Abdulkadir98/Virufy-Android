package com.example.myapplication


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.virufy.R
import kotlinx.android.synthetic.main.fragment_record_cough_fragment_intro.view.*

/**
 * A simple [Fragment] subclass.
 */
class RecordFragmentIntro : Fragment() {
    private lateinit var rootView: View

    var listener: OnStartClickListener? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_record_cough_fragment_intro, container, false)
        rootView.startBtn.setOnClickListener {
            listener?.onStartClicked(arguments?.getString("stage")!!)
        }
        return rootView
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnStartClickListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
        }
    }

    interface OnStartClickListener {
        fun onStartClicked(stage: String)
    }

}
