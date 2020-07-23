package app.virufy

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_RECORD_AUDIO_PERMISSION = 200

// model for survey questions
@Parcelize
data class SurveyQuestion (
    @SerializedName("question") val question : String,
    @SerializedName("answers") val answers : List<String>,
    @SerializedName("type") val type : String
): Parcelable

class MainActivity : AppCompatActivity(), RecordAudioFragment.OnAudioSubmittedListener,
    RecordFragmentIntro.OnStartClickListener, QuestionsIntroFragment.OnNextClickedListener {
    // Requesting permission to RECORD_AUDIO
    private var permissionToRecordAccepted = false
    private var permissions: Array<String> = arrayOf(Manifest.permission.RECORD_AUDIO)

    private var jsonString: String = ""
    private var questions: Array<SurveyQuestion> = arrayOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         jsonString = resources.openRawResource(R.raw.questions)
            .bufferedReader().use { it.readText() }
         questions = Gson().fromJson(jsonString, Array<SurveyQuestion>::class.java)

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
            val newFragment = QuestionsIntroFragment()
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

    override fun onNextClicked() {
        val newFragment = QuestionsChoiceFragment.newInstance(questions[0])
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, newFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
