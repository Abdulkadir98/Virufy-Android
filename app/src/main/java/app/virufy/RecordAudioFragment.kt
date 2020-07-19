package app.virufy


import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.virufy.R
import kotlinx.android.synthetic.main.fragment_calibrate_audio.view.*
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */

private const val LOG_TAG = "AudioRecordTest"

enum class RecordStates {
    START, RECORD, RETAKE
}

class RecordAudioFragment : Fragment() {

    private lateinit var rootView: View

    private var fileName: String = ""

    private var recorder: MediaRecorder? = null

    private var player: MediaPlayer? = null

    private var recordState = RecordStates.START

    var listener: OnAudioSubmittedListener? = null



    // Container Activity must implement this interface
    interface OnAudioSubmittedListener {
        fun onAudioSubmitted(stage: String)
    }

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(): Fragment {
            val calibrateAudioFragment =
                RecordAudioFragment()
            val bundle = Bundle()
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnAudioSubmittedListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
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
        player?.setOnCompletionListener {
            rootView.chronometer.stop()
        }
    }

    private fun stopPlaying() {
        player?.release()
        player = null
    }


    fun updateUi(state: RecordStates) {
        when(state) {
            RecordStates.START -> {

                showViews(rootView.recordBtn)
                hideViews(rootView.pauseBtn, rootView.stopBtn, rootView.playbackBtn,
                    rootView.retakeBtn)

                rootView.submitBtn.isEnabled = false
                rootView.chronometer.base = SystemClock.elapsedRealtime()

                rootView.recordBtn.setOnClickListener {
                    startRecording()
                    rootView.chronometer.base = SystemClock.elapsedRealtime()
                    rootView.chronometer.start()
                    recordState = RecordStates.RECORD
                    updateUi(recordState)
                }
            }

            RecordStates.RECORD -> {

                hideViews(rootView.recordBtn, rootView.playbackBtn, rootView.retakeBtn)
                showViews(rootView.pauseBtn, rootView.stopBtn)

                rootView.stopBtn.setOnClickListener {
                    stopRecording()
                    rootView.chronometer.stop()
                    recordState = RecordStates.RETAKE
                    updateUi(recordState)
                }
            }

            RecordStates.RETAKE -> {

                hideViews(rootView.recordBtn, rootView.pauseBtn, rootView.stopBtn)
                showViews(rootView.playbackBtn, rootView.retakeBtn)

                rootView.submitBtn.isEnabled = true

                rootView.playbackBtn.setOnClickListener {
                    startPlaying()
                    rootView.chronometer.base = SystemClock.elapsedRealtime()
                    rootView.chronometer.start()

                }

                rootView.retakeBtn.setOnClickListener {
                    recordState = RecordStates.START
                    updateUi(recordState)
                }

                rootView.submitBtn.setOnClickListener {
                    stopPlaying()
                    listener?.onAudioSubmitted(arguments?.getString("stage")!!)
                }
            }
        }
    }

    private fun hideViews(vararg  views: View) {

        for (view in views) {
            view.visibility = View.INVISIBLE
        }

    }

    private fun showViews(vararg views: View) {
        for (view in views) {
            view.visibility = View.VISIBLE
        }
    }



    override fun onStop() {
        super.onStop()
        recorder?.release()
        recorder = null
        player?.release()
        player = null
    }
}
