package by.matthewvirus.geoquiz.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.geoquiz.R
import by.matthewvirus.geoquiz.viewModel.CheatViewModel

const val EXTRA_ANSWER_SHOWN = "by.matthewvirus.geoquiz.answer_shown"
private const val EXTRA_ANSWER_IS_TRUE = "by.matthewvirus.geoquiz.answer_is_true"

class CheatActivity : AppCompatActivity() {

    private lateinit var answerTextView: TextView
    private lateinit var apiVersionTextView: TextView
    private lateinit var showAnswerButton: Button

    private var answerIsTrue = false

    private val cheatViewModel by lazy {
        ViewModelProvider(this).get(CheatViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        answerIsTrue = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)

        answerTextView = findViewById(R.id.right_answer_text)
        showAnswerButton = findViewById(R.id.show_answer_button)
        apiVersionTextView = findViewById(R.id.api_version_text)

        apiVersionTextView.text = resources.getString(R.string.api_text, cheatViewModel.apiVersion)

        showAnswerButton.setOnClickListener {
            answerTextViewSetText()
            cheatViewModel.isCheater = true
        }

        if (cheatViewModel.isCheater)
            answerTextViewSetText()
    }

    private val answerTextViewSetText = {
        val answerText = when {
            answerIsTrue -> R.string.true_button
            else -> R.string.false_button
        }
        answerTextView.setText(answerText)
        setAnswerShownResult(true)
    }

    private fun setAnswerShownResult(isAnswerShown: Boolean) {
        val data = Intent().apply {
            putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown)
        }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}