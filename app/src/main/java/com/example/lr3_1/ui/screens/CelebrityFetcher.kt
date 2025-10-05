package com.example.lr3_1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.lr3_1.model.Celebrity
import com.example.lr3_1.ui.theme.AppTypography
import kotlinx.coroutines.launch

/**
 * Экран игры "Угадай знаменитость".
 *
 * Отображает:
 * - случайное изображение знаменитости;
 * - поле ввода для имени;
 * - кнопки "Give up" и "Next";
 * - обратную связь (правильно/неправильно) через текст и Snackbar.
 *
 * @param celebrities список всех доступных знаменитостей.
 * @param onNextCelebrity callback при переходе к следующей знаменитости (например, для логирования или статистики).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CelebrityGameScreen(
    celebrities: List<Celebrity>,
    onNextCelebrity: () -> Unit
) {
    // Текущая выбранная знаменитость
    var currentCelebrity by remember { mutableStateOf<Celebrity?>(celebrities.randomOrNull()) }

    // Введённое пользователем имя
    var userGuess by remember { mutableStateOf("") }

    // Показывать ли правильный ответ
    var showAnswer by remember { mutableStateOf(false) }

    // Сообщение обратной связи: "Правильно!" или "Попробуй ещё раз"
    var feedback by remember { mutableStateOf("") }

    // Для показа Snackbar
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Основная Scaffold с верхней панелью и контейнером
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Guess the Celebrity",
                        style = AppTypography.titleLarge
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            // Если список пуст или не удалось выбрать знаменитость
            if (currentCelebrity == null) {
                Text(
                    text = "No celebrities found 😢",
                    style = AppTypography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Изображение знаменитости с загрузкой через Coil
                    AsyncImage(
                        model = currentCelebrity!!.imageUrl,
                        contentDescription = currentCelebrity!!.name,
                        modifier = Modifier
                            .size(200.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Поле ввода имени
                    OutlinedTextField(
                        value = userGuess,
                        onValueChange = { userGuess = it },
                        label = { Text("Enter celebrity name") },
                        singleLine = true,
                        textStyle = AppTypography.bodyLarge,
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (userGuess.isNotBlank()) {
                                    // Проверка введённого имени с учётом регистра
                                    if (userGuess.equals(currentCelebrity!!.name, ignoreCase = true)) {
                                        feedback = "🎉 Correct!"
                                        scope.launch { snackbarHostState.showSnackbar("Correct!") }
                                    } else {
                                        feedback = "❌ Try again!"
                                        scope.launch { snackbarHostState.showSnackbar("Wrong guess") }
                                    }
                                }
                            }
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Вывод обратной связи
                    if (feedback.isNotEmpty()) {
                        Text(
                            text = feedback,
                            style = AppTypography.titleMedium,
                            color = if (feedback.startsWith("🎉"))
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Показываем правильный ответ, если пользователь сдался
                    if (showAnswer) {
                        Text(
                            text = "Answer: ${currentCelebrity!!.name}",
                            style = AppTypography.titleMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            textAlign = TextAlign.Center
                        )
                    }

                    // Кнопки действий: "Give up" и "Next"
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Показывает правильный ответ
                        Button(
                            onClick = {
                                showAnswer = true
                                feedback = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                            )
                        ) {
                            Text("Give up", color = MaterialTheme.colorScheme.onSecondaryContainer)
                        }

                        // Переход к следующей знаменитости
                        Button(
                            onClick = {
                                onNextCelebrity()
                                currentCelebrity = celebrities.randomOrNull()
                                userGuess = ""
                                showAnswer = false
                                feedback = ""
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Next", color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }
    }
}
