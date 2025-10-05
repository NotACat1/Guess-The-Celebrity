package com.example.lr3_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.lr3_1.model.Celebrity
import com.example.lr3_1.network.CelebrityRepository
import com.example.lr3_1.ui.screens.CelebrityGameScreen
import com.example.lr3_1.ui.theme.AppTheme
import kotlinx.coroutines.launch

/**
 * Главная Activity приложения.
 *
 * Отвечает за:
 * - загрузку списка знаменитостей;
 * - инициализацию UI с помощью Jetpack Compose;
 * - передачу данных в экран [CelebrityGameScreen].
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Локальный список знаменитостей (загружается асинхронно)
        var celebrities: List<Celebrity> = emptyList()

        // Загружаем данные в фоновом потоке
        lifecycleScope.launch {
            celebrities = CelebrityRepository.loadCelebrities()

            // После загрузки — строим UI
            setContent {
                AppTheme {
                    CelebrityGameScreen(
                        celebrities = celebrities,
                        onNextCelebrity = {}
                    )
                }
            }
        }
    }
}
