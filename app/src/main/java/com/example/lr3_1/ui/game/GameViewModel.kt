package com.example.lr3_1.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lr3_1.data.model.Celebrity
import com.example.lr3_1.data.repository.CelebrityRepository
import com.example.lr3_1.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Класс, описывающий состояние игрового экрана.
 *
 * Содержит все данные, необходимые для отображения UI и логики игры.
 */
data class GameUiState(
    val isLoading: Boolean = true,
    val currentCelebrity: Celebrity? = null,
    val userInput: String = "",
    val score: Int = 0,
    val attempts: Int = 0,
    val showAnswer: Boolean = false,
    val guessResult: GuessResult? = null,
    val suggestions: List<String> = emptyList(),
    val error: String? = null
)

/**
 * Перечисление результатов попытки угадать.
 */
enum class GuessResult {
    CORRECT,
    WRONG
}

/**
 * ViewModel, управляющая логикой игры.
 *
 * Отвечает за:
 * - загрузку данных из репозитория,
 * - обработку пользовательских действий,
 * - обновление состояния [GameUiState].
 *
 * @property repository Репозиторий для получения списка знаменитостей.
 */
@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: CelebrityRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()

    private var allCelebrities: List<Celebrity> = emptyList()
    private var allNames: List<String> = emptyList()
    private var usedCelebrityIds: MutableSet<String> = mutableSetOf()

    init {
        loadCelebrities()
    }

    /**
     * Загружает список знаменитостей из репозитория.
     * При успехе — инициализирует первую игру.
     */
    private fun loadCelebrities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val resource = repository.getCelebrities()) {
                is Resource.Success -> {
                    allCelebrities = resource.data ?: emptyList()
                    allNames = allCelebrities.map { it.name }
                    usedCelebrityIds.clear()
                    loadNextCelebrity()
                }
                is Resource.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = resource.message) }
                }
                is Resource.Loading -> {
                    // Уже обработано
                }
            }
        }
    }

    /**
     * Загружает следующую знаменитость для угадывания.
     * Если все угаданы — завершает игру.
     */
    private fun loadNextCelebrity() {
        val availableCelebrities = allCelebrities.filterNot { it.id in usedCelebrityIds }
        val celebrity = availableCelebrities.randomOrNull()

        if (celebrity == null) {
            _uiState.update {
                it.copy(
                    isLoading = false,
                    error = "Congratulations! You guessed them all!",
                    currentCelebrity = null
                )
            }
            return
        }

        usedCelebrityIds.add(celebrity.id)
        _uiState.update {
            it.copy(
                isLoading = false,
                currentCelebrity = celebrity,
                userInput = "",
                showAnswer = false,
                guessResult = null,
                suggestions = emptyList(),
                error = null
            )
        }
    }

    /**
     * Обновляет введённый пользователем текст и формирует подсказки.
     */
    fun onInputChanged(input: String) {
        _uiState.update {
            it.copy(
                userInput = input,
                suggestions = if (input.isBlank()) {
                    emptyList()
                } else {
                    allNames.filter { name ->
                        name.contains(input, ignoreCase = true)
                    }.take(5)
                }
            )
        }
    }

    /**
     * Проверяет догадку пользователя.
     * Если верно — увеличивает счёт и через задержку загружает следующую знаменитость.
     */
    fun onGuess(guess: String) {
        val correctName = _uiState.value.currentCelebrity?.name
        if (guess.equals(correctName, ignoreCase = true)) {
            _uiState.update {
                it.copy(
                    score = it.score + 1,
                    guessResult = GuessResult.CORRECT,
                    userInput = ""
                )
            }
            viewModelScope.launch {
                kotlinx.coroutines.delay(1500)
                loadNextCelebrity()
            }
        } else {
            _uiState.update {
                it.copy(
                    guessResult = GuessResult.WRONG,
                    attempts = it.attempts + 1,
                    userInput = ""
                )
            }
        }
    }

    /**
     * Отображает правильный ответ и через короткую паузу показывает следующую знаменитость.
     */
    fun giveUp() {
        _uiState.update { it.copy(showAnswer = true, userInput = "") }
        viewModelScope.launch {
            kotlinx.coroutines.delay(2500)
            loadNextCelebrity()
        }
    }

    /**
     * Повторяет попытку загрузки данных при ошибке.
     */
    fun retryLoad() {
        loadCelebrities()
    }
}
