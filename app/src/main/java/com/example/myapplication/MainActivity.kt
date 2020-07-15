package com.example.virufy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.CalibrateAudioFragment
import com.example.myapplication.RecordCoughFragment
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

class MainActivity : AppCompatActivity(), CalibrateAudioFragment.OnAudioSubmittedListener {
    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firstStageIv.setColorFilter(Color.parseColor("#1890FF"))
        calibrateTv.typeface = Typeface.DEFAULT_BOLD
        calibrateTv.setTextColor(Color.parseColor("#000000"))
        val newFragment = CalibrateAudioFragment()
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

    override fun onAudioSubmitted() {
        first_divider_view.setBackgroundColor(Color.parseColor("#1890FF"))
        secondStageIv.setColorFilter(Color.parseColor("#1890FF"))
        calibrateTv.typeface = Typeface.DEFAULT
        val newFragment = RecordCoughFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
