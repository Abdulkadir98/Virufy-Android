package com.example.myapplication


import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.virufy.R
import kotlinx.android.synthetic.main.fragment_calibrate_audio.view.*
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */

private const val LOG_TAG = "AudioRecordTest"

enum class RecordStates {
    START, RECORD, RETAKE
}

class CalibrateAudioFragment : Fragment() {

    private lateinit var rootView: View

    private var fileName: String = ""

    private var recorder: MediaRecorder? = null

    private var player: MediaPlayer? = null

    private var recordState = RecordStates.START

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int): Fragment {
            val calibrateAudioFragment = CalibrateAudioFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            calibrateAudioFragment.arguments = bundle
            return calibrateAudioFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_calibrate_audio, container, false)
        fileName = "${activity?.externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        updateUi(recordState)
        return rootView
    }

    private fun onRecord(start: Boolean) = if (start) {
        rootView.chronometer.start()
        rootView.recordBtn.visibility = View.INVISIBLE
        rootView.pauseBtn.visibility = View.VISIBLE
        rootView.stopBtn.visibility = View.VISIBLE
        startRecording()
    } else {
        stopRecording()
        rootView.chronometer.base = SystemClock.elapsedRealtime()
        rootView.chronometer.stop()
    }

    private fun onPlay(start: Boolean) = if (start) {
        startPlaying()
    } else {
        stopPlaying()
    }

    private fun startPlaying() {
        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
                start()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }

    fun updateUi(state: RecordStates) {
        when(state) {
            RecordStates.START -> {
                rootView.recordBtn.visibility = View.VISIBLE
                rootView.pauseBtn.visibility = View.GONE
                rootView.stopBtn.visibility = View.GONE
                rootView.playbackBtn.visibility = View.GONE
                rootView.retakeBtn.visibility = View.GONE

                rootView.submitBtn.isEnabled = false

                rootView.recordBtn.setOnClickListener {
                    startRecording()
                    rootView.chronometer.base = SystemClock.elapsedRealtime()
                    rootView.chronometer.start()
                    recordState = RecordStates.RECORD
                    updateUi(recordState)
                }
            }

            RecordStates.RECORD -> {
                rootView.recordBtn.visibility = View.GONE
                rootView.playbackBtn.visibility = View.GONE
                rootView.retakeBtn.visibility = View.GONE

                rootView.pauseBtn.visibility = View.VISIBLE
                rootView.stopBtn.visibility = View.VISIBLE

                rootView.stopBtn.setOnClickListener {
                    stopRecording()
                    rootView.chronometer.stop()
                    rootView.chronometer.base = SystemClock.elapsedRealtime()
                    recordState = RecordStates.RETAKE
                    updateUi(recordState)
                }
            }

            RecordStates.RETAKE -> {
                rootView.recordBtn.visibility = View.GONE
                rootView.pauseBtn.visibility = View.GONE
                rootView.stopBtn.visibility = View.GONE

                rootView.playbackBtn.visibility = View.VISIBLE
                rootView.retakeBtn.visibility = View.VISIBLE

                rootView.submitBtn.isEnabled = true

                rootView.playbackBtn.setOnClickListener {
                    startPlaying()
                }

                rootView.retakeBtn.setOnClickListener {
                    recordState = RecordStates.START
                    updateUi(recordState)
                }
            }
        }
    }

    private fun startRecording() {
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            setOutputFile(fileName)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            try {
                prepare()
            } catch (e: IOException) {
                Log.e(LOG_TAG, "prepare() failed")
            }

            start()
        }
    }

    private fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}
