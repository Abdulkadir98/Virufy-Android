package app.virufy

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_questions_choice.view.*


/**
 * A simple [Fragment] subclass.
 * Use the [QuestionsChoiceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionsChoiceFragment : Fragment(), AdapterView.OnItemSelectedListener {

    // array for saving user's answer for choice-type questions
    private var chosenArray = booleanArrayOf()
    private var choiceTextViews = arrayListOf<TextView>()
    private var choiceMadeForSingleChoiceView = false
    private var choiceMadeForDropDown = 0 // default choice
    private lateinit var rootView: View

    var listener: OnQuestionAnsweredListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_questions_choice, container, false)
        val question: SurveyQuestion? = arguments?.getParcelable(QUESTION)
        if (question != null) {
            chosenArray = BooleanArray(question.answers.size) { false }
            rootView.questionTv.setText(question.question)

            when (question.type) {
                "choice" -> {
                    question.answers.forEachIndexed { index, str ->
                        rootView.choicesContainer.apply {
                            addView(addAnswerChoices(index, str))

                        }
                    }
                }

                "dropdown" -> {
                    val spinner = Spinner(activity)
                    val arrayAdapter = ArrayAdapter<String>(
                        context!!,
                        android.R.layout.simple_spinner_item, question.answers
                    )

                    arrayAdapter.also { adapter ->
                        // Specify the layout to use when the list of choices appears
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        // Apply the adapter to the spinner
                        spinner.adapter = adapter
                    }
                    spinner.onItemSelectedListener = this
                    rootView.choicesContainer.addView(spinner)
                }

                "multiplechoice" -> {
                    question.answers.forEachIndexed { index, str ->
                        rootView.choicesContainer.apply {
                            addView(addAnswerChoicesMultiple(index, str))

                        }
                    }
                }
            }

//            rootView.choicesContainer.apply {
//                addView(addNextButton())
//            }

        }
        rootView.nextBtn.setOnClickListener {
            // pass the next question from the main activity using next position in questions array
            listener?.onNextClicked(arguments?.getInt(POSITION)!! + 1)
        }

        return rootView
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun addNextButton(): Button {
        return Button(activity).apply {
            text = resources.getString(R.string.next_btn_txt)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(24, 0, 24, 0)
            }
            val density = context.getResources().getDisplayMetrics().density
            val paddingDp = 16
            val paddingPx = paddingDp * density
            setPadding(paddingPx.toInt(), paddingPx.toInt(), paddingPx.toInt(), paddingPx.toInt())
            background = ContextCompat.getDrawable(context, R.drawable.submit_btn_background)
        }
    }

    // For drop-down question, selecting item from Spinner
    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        choiceMadeForDropDown = position
        rootView.nextBtn.isEnabled = true
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun addAnswerChoicesMultiple(idx: Int, answer: String): TextView {
        return TextView(activity).apply {
            text = answer
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

                // At least one choice must be selected after user makes a choice
                // if user tries to un-select the only choice selected, nothing happens.
                if (choiceMadeForSingleChoiceView) {
                    if (chosenArray.filter { it == true }.size == 0) {
                        chosenArray[idx] = !chosenArray[idx]
                    }
                }
                updateUiForChoiceViews(chosenArray, choiceTextViews)
            }
            choiceTextViews.add(this)
        }
    }


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    fun addAnswerChoices(idx: Int, answer: String): TextView {
        return TextView(activity).apply {
            text = answer
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

                // At least one choice must be selected after user makes a choice
                // if user tries to un-select the only choice selected, nothing happens.
                if (choiceMadeForSingleChoiceView) {
                    if (chosenArray.filter { it == true }.size == 0) {
                        chosenArray[idx] = !chosenArray[idx]
                    }
                }

                // un-selected other selected choices for view that accepts only single choice from user
                for (i in chosenArray.indices) {
                    if (i == idx) continue
                    chosenArray[i] = false
                }
                updateUiForChoiceViews(chosenArray, choiceTextViews)
            }
            choiceTextViews.add(this)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun updateUiForChoiceViews(
        chosenArray: BooleanArray,
        choiceTextViews: ArrayList<TextView>
    ) {

        // Mark user's first choice
        choiceMadeForSingleChoiceView = true
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnQuestionAnsweredListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
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
        const val POSITION = "position"

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(question: SurveyQuestion, position: Int) =
            QuestionsChoiceFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(QUESTION, question)
                    putInt(POSITION, position)
                }
            }
    }

    // Container Activity must implement this interface
    interface OnQuestionAnsweredListener {
        fun onNextClicked(position: Int)
    }


}