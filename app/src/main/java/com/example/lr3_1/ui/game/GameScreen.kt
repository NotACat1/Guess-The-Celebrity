package com.example.lr3_1.ui.game

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest

/**
 * Основной экран игры "Угадай знаменитость".
 *
 * @param viewModel ViewModel, управляющий состоянием UI для игрового процесса.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    viewModel: GameViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Guess The Celebrity 🤔", style = MaterialTheme.typography.titleLarge)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    ErrorState(
                        message = uiState.error!!,
                        onRetry = viewModel::retryLoad,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                uiState.currentCelebrity != null -> {
                    GameContent(
                        state = uiState,
                        onInputChanged = viewModel::onInputChanged,
                        onGuess = viewModel::onGuess,
                        onGiveUp = viewModel::giveUp
                    )
                }
            }
        }
    }
}

/**
 * Контент игрового экрана.
 *
 * @param state Состояние UI текущей игры.
 * @param onInputChanged Лямбда для обработки изменения текста ввода пользователя.
 * @param onGuess Лямбда для обработки попытки угадать знаменитость.
 * @param onGiveUp Лямбда для обработки действия "Сдаться".
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameContent(
    state: GameUiState,
    onInputChanged: (String) -> Unit,
    onGuess: (String) -> Unit,
    onGiveUp: () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        // Карточка с текущим уровнем и очками
        ElevatedCard(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Level: ${state.score / 10 + 1}", style = MaterialTheme.typography.bodyLarge)
                Text("Score: ${state.score}", fontWeight = FontWeight.Bold)
            }
        }

        // Секция с изображением знаменитости
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(16.dp))
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(state.currentCelebrity?.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Celebrity photo",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Поле ввода имени знаменитости
        OutlinedTextField(
            value = state.userInput,
            onValueChange = {
                onInputChanged(it)
                isDropdownExpanded = it.isNotBlank()
            },
            label = { Text("Type celebrity name...") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            readOnly = state.showAnswer,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = { onGuess(state.userInput) }) {
                    Icon(Icons.Default.Check, contentDescription = "Guess")
                }
            }
        )

        // Дропдаун с подсказками
        AnimatedVisibility(visible = isDropdownExpanded && state.suggestions.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column {
                    state.suggestions.forEach { suggestion ->
                        TextButton(
                            onClick = {
                                keyboardController?.hide()
                                isDropdownExpanded = false
                                onGuess(suggestion)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(suggestion, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }

        // Отображение результата угадывания
        Crossfade(targetState = state.guessResult) { result ->
            when (result) {
                GuessResult.CORRECT -> ResultChip("✅ Correct!", MaterialTheme.colorScheme.primary)
                GuessResult.WRONG -> ResultChip("❌ Wrong, try again!", MaterialTheme.colorScheme.error)
                else -> {}
            }
        }

        // Кнопка "Сдаться"
        Button(
            onClick = onGiveUp,
            enabled = !state.showAnswer,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Give Up 😅", style = MaterialTheme.typography.titleMedium)
        }

        // Отображение правильного ответа
        AnimatedVisibility(visible = state.showAnswer) {
            Text(
                text = "The correct answer: ${state.currentCelebrity?.name}",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
        }
    }
}

/**
 * Чип для отображения результата угадывания.
 *
 * @param text Текст результата.
 * @param color Цвет, ассоциированный с результатом.
 */
@Composable
fun ResultChip(text: String, color: Color) {
    Surface(
        color = color.copy(alpha = 0.15f),
        contentColor = color,
        shape = RoundedCornerShape(50),
        modifier = Modifier
            .padding(top = 8.dp)
            .animateContentSize()
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Компонент для отображения состояния ошибки с возможностью повторной загрузки.
 *
 * @param message Сообщение об ошибке.
 * @param onRetry Лямбда для повторной попытки загрузки.
 * @param modifier Модификатор для кастомизации внешнего вида компонента.
 */
@Composable
private fun ErrorState(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "😕 Oops!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        ElevatedButton(onClick = onRetry) {
            Text("Try Again")
        }
    }
}
