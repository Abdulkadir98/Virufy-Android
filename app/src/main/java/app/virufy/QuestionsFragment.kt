package app.virufy


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import app.virufy.R

/**
 * A simple [Fragment] subclass.
 */
class QuestionsFragment : Fragment() {

    companion object {
        const val ARG_POSITION = "position"

        fun getInstance(position: Int): Fragment {
            val questionsFragment = QuestionsFragment()
            val bundle = Bundle()
            bundle.putInt(ARG_POSITION, position)
            questionsFragment.arguments = bundle
            return questionsFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_questions, container, false)
    }


}
