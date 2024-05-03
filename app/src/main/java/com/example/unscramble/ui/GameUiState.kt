package com.example.unscramble.ui                     //File created in the ui package

data class GameUiState (                              //This is a model class
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val score: Int = 0,
    val currentWordCount: Int = 1,
    val isGameOver: Boolean = false
)