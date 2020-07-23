package app.virufy


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_questions_intro.view.*

/**
 * A simple [Fragment] subclass.
 */
class QuestionsIntroFragment : Fragment() {

    var listener: OnNextClickedListener? = null


    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int): Fragment {
            val questionsFragment = QuestionsIntroFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            questionsFragment.arguments = bundle
            return questionsFragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnNextClickedListener
        if (listener == null) {
            throw ClassCastException("$context must implement OnArticleSelectedListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_questions_intro, container, false)

        rootView.nextBtn.setOnClickListener {
            listener?.onNextClicked()
        }
        return rootView
    }

    // Container Activity must implement this interface
    interface OnNextClickedListener {
        fun onNextClicked()
    }


}
