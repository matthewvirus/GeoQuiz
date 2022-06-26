package by.matthewvirus.geoquiz.view

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import by.matthewvirus.geoquiz.R
import by.matthewvirus.geoquiz.viewModel.QuizViewModel
import by.matthewvirus.geoquiz.model.Cheat.hints

private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: Button
    private lateinit var cheatButton: Button
    private lateinit var questionTextView: TextView
    private lateinit var hintsNumberTextView: TextView

    private var answers = 0
    private var rightAnswers = 0
    private var cheatUsedQuestions = emptyArray<Int>()

    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        cheatButton = findViewById(R.id.cheat_button)
        questionTextView = findViewById(R.id.question_text_view)
        hintsNumberTextView = findViewById(R.id.hints_number_text)

        quizViewModel.currentIndex = savedInstanceState?.getInt(KEY_INDEX, 0) ?: 0

        trueButton.setOnClickListener {
            checkAnswer(true)
            changeButtonState(trueButton, falseButton, false)
        }

        falseButton.setOnClickListener {
            checkAnswer(false)
            changeButtonState(trueButton, falseButton, false)
        }

        nextButton.setOnClickListener {
            quizViewModel.nextQuestion()
            updateQuestion()
            changeButtonState(trueButton, falseButton, true)
            answers++
            if (answers == quizViewModel.questionBankSize)
                getResult()
        }

        cheatButton.setOnClickListener { view ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this, answerIsTrue)
            val options = ActivityOptionsCompat.makeClipRevealAnimation(view, 0, 0, view.width, view.height)
            getResult.launch(intent, options)
        }

        updateQuestion()
        updateHintsNumber()
    }

    private val getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater = it.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            cheatUsedQuestions += quizViewModel.currentIndex
            hints--
            updateHintsNumber()
        }
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    private fun updateHintsNumber() {
        hintsNumberTextView.setText(hints.toString())
        if (hints == 0)
            changeButtonState(cheatButton, false)
    }

    private fun updateQuestion() {
        questionTextView.setText(quizViewModel.currentQuestionText)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId: Int
        when {
            quizViewModel.isCheater && cheatUsedQuestions.contains(quizViewModel.currentIndex) -> {
                messageResId = R.string.judgement_toast
            }
            userAnswer == correctAnswer && !cheatUsedQuestions.contains(quizViewModel.currentIndex) -> {
                messageResId = R.string.correct_toast
                rightAnswers++
            }
            else -> {
                messageResId = R.string.incorrect_toast
            }
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
    }

    private fun getResult() {
        val result = ((rightAnswers.toDouble() / quizViewModel.questionBankSize.toDouble()) * 100)
        answers = 0
        rightAnswers = 0
        Toast.makeText(this, "Your score is ${result.toInt()}%", Toast.LENGTH_SHORT).show()
    }

    private fun changeButtonState(button: Button, flag: Boolean) {
        button.isEnabled = flag
    }

    private fun changeButtonState(button: Button, button1: Button, flag: Boolean) {
        button.isEnabled = flag
        button1.isEnabled = flag
    }
}