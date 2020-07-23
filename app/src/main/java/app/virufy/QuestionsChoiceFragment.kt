package app.virufy

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_questions_choice.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [QuestionsChoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionsChoiceFragment : Fragment() {


    private var chosenArray = booleanArrayOf()
    private var choiceTextViews = arrayListOf<TextView>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_questions_choice, container, false)
        val question: SurveyQuestion? = arguments?.getParcelable(QUESTION)
        if (question != null) {
            chosenArray = BooleanArray(question.answers.size) { false }
            rootView.questionTv.setText(question.question)
            question.answers.forEachIndexed { index, str ->
                rootView.choicesContainer.apply {
                    addView(addAnswerChoices(index, str))
                }
            }
        }

        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun addAnswerChoices(idx: Int, answer: String): TextView {
        return TextView(activity).apply {
            setText(answer)
            id = idx
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 12, 0, 12)
            }
            // conversion from dp to pixels
            val density = context.getResources().getDisplayMetrics().density
            val paddingTopAndBottomDp = 16
            val paddingLeftAndRightDp = 16
            val paddingTopPx = paddingTopAndBottomDp * density
            val paddingBottomPx = paddingTopAndBottomDp * density
            val paddingLeftPx = paddingLeftAndRightDp * density
            val paddingRightPx = paddingLeftAndRightDp * density
            setPadding(
                paddingLeftPx.toInt(),
                paddingTopPx.toInt(),
                paddingRightPx.toInt(),
                paddingBottomPx.toInt()
            )
            background = ContextCompat.getDrawable(context, R.drawable.choice_tv_border_unselected)
            setTextColor(Color.parseColor("#000000"))
            setTextSize(16f)
            setOnClickListener {
                rootView.nextBtn.isEnabled = true
                chosenArray[idx] = !chosenArray[idx]
                for (i in chosenArray.indices) {
                    if (i == idx) continue
                    chosenArray[i] = false
                }
                updateUi(chosenArray, choiceTextViews)
            }
            choiceTextViews.add(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun updateUi(chosenArray: BooleanArray, choiceTextViews: ArrayList<TextView>) {
        chosenArray.forEachIndexed { idx, isChosen ->
            if (isChosen) {
                choiceTextViews[idx].setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.drawable.ic_tick,
                    0
                )
                choiceTextViews[idx].background =
                    ContextCompat.getDrawable(context!!, R.drawable.choice_tv_border_selected)
            } else {
                choiceTextViews[idx].setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    0,
                    0
                )
                choiceTextViews[idx].background =
                    ContextCompat.getDrawable(context!!, R.drawable.choice_tv_border_unselected)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuestionsChoiceFragment.
         */

        const val QUESTION = "question"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(question: SurveyQuestion) =
            QuestionsChoiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(QUESTION, question)
                }
            }
    }
}