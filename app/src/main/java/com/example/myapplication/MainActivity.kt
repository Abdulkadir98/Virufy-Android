package com.example.virufy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.QuestionsFragment
import com.example.myapplication.RecordAudioFragment
import com.example.myapplication.RecordFragmentIntro
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), RecordAudioFragment.OnAudioSubmittedListener,
    RecordFragmentIntro.OnStartClickListener {
    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstStageIv.setColorFilter(Color.parseColor("#1890FF"))
        calibrateTv.typeface = Typeface.DEFAULT_BOLD
        calibrateTv.setTextColor(Color.parseColor("#000000"))

        val bundle = Bundle()
        bundle.putString("stage", "calibrate")
        bundle.putString("firstMessage", resources.getString(R.string.record_you_coughing_for_10_seconds))
        bundle.putString("secondMessage", resources.getString(R.string.when_you_are_done_click_stop))
        val newFragment = RecordFragmentIntro()
        newFragment.arguments = bundle
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionToRecordAccepted = if (requestCode == REQUEST_RECORD_AUDIO_PERMISSION) {
            grantResults[0] == PackageManager.PERMISSION_GRANTED

        } else {
            false
        }
        if (!permissionToRecordAccepted) finish()

    }

    override fun onAudioSubmitted(stage: String) {
        first_divider_view.setBackgroundColor(Color.parseColor("#1890FF"))
        secondStageIv.setColorFilter(Color.parseColor("#1890FF"))
        calibrateTv.typeface = Typeface.DEFAULT
        recordCoughTv.setTextColor(Color.parseColor("#000000"))
        recordCoughTv.typeface = Typeface.DEFAULT_BOLD
        calibrateTv.setTextColor(Color.parseColor("#000000"))

        if (stage.equals("calibrate")) {
            val bundle = Bundle()
            bundle.putString("stage", "record")
            bundle.putString("firstMessage", resources.getString(R.string.record_you_coughing_for_10_seconds))
            bundle.putString("secondMessage", resources.getString(R.string.when_you_are_done_click_stop))
            val newFragment = RecordFragmentIntro()
            newFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        else if(stage.equals("record")) {
            second_divider_view.setBackgroundColor(Color.parseColor("#1890FF"))
            thirdStageIv.setColorFilter(Color.parseColor("#1890FF"))
            recordCoughTv.typeface = Typeface.DEFAULT
            questionsTv.typeface = Typeface.DEFAULT_BOLD
            questionsTv.setTextColor(Color.parseColor("#000000"))
            val newFragment = QuestionsFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }

    override fun onStartClicked(stage: String) {

        if (stage.equals("calibrate")) {
            val bundle = Bundle()
            bundle.putString("stage", "calibrate")
            val newFragment = RecordAudioFragment()
            newFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
        else if (stage.equals("record")) {
            val bundle = Bundle()
            bundle.putString("stage", "record")
            val newFragment = RecordAudioFragment()
            newFragment.arguments = bundle
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container, newFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

    }
}
