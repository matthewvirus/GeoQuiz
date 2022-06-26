package by.matthewvirus.geoquiz.viewModel

import androidx.lifecycle.ViewModel
import by.matthewvirus.geoquiz.model.Question
import by.matthewvirus.geoquiz.R

class QuizViewModel: ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_first, false),
        Question(R.string.question_second, false),
        Question(R.string.question_third, true),
        Question(R.string.question_fourth, true),
        Question(R.string.question_fifth, true)
    )

    var currentIndex = 0
    var isCheater = false

    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer
    val currentQuestionText: Int get() = questionBank[currentIndex].textResId
    val questionBankSize = questionBank.size

    fun nextQuestion() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}