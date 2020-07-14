package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class RecordAudioAdapter(activity: AppCompatActivity, val itemsCount: Int) :
    FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return itemsCount
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> CalibrateAudioFragment.getInstance(position)
            1 -> RecordAudioFragment.getInstance(position)
            2 -> QuestionsFragment.getInstance(position)
            else -> {
                Fragment()
            }
        }
    }
}