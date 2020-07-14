package com.example.virufy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.RecordAudioAdapter
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var fragmentNamesArray: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         fragmentNamesArray = resources.getStringArray(R.array.fragment_names)


        val recordAudioAdapter = RecordAudioAdapter(this, fragmentNamesArray.size)
        viewPager.adapter = recordAudioAdapter
    }
}
