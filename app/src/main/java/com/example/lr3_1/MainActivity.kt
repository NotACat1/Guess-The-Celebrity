package com.example.lr3_1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.lr3_1.ui.game.GameScreen
import com.example.lr3_1.ui.theme.AppTheme
import dagger.hilt.android.AndroidEntryPoint
import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Класс приложения, инициализирующий Dagger Hilt.
 *
 * Аннотируется [HiltAndroidApp], чтобы включить автоматическую
 * генерацию графа зависимостей.
 */
@HiltAndroidApp
class MyApplication : Application()

/**
 * Главная активность приложения.
 *
 * Отображает экран [GameScreen] внутри Compose UI
 * с применением [AppTheme] и Material 3.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Инициализирует Compose UI при создании активности.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    GameScreen()
                }
            }
        }
    }
}
