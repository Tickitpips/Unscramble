//This represents the ViewModel in the MMVM architecture

package com.example.unscramble.ui                 //File created in the ui package

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.update
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.MAX_NO_OF_WORDS

class GameViewModel: ViewModel() {                 //Created a Kotlin class file called GameViewModel and extended from ViewModel() class

    // Backing property to avoid state updates from other classes
    private val _uiState = MutableStateFlow(GameUiState())            //Can only be modified within GameViewModel class
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()      //Access value of _uiState and returns it. Since it is a val, it cant be modified
    private lateinit var currentWord: String                          //
    private var usedWords: MutableSet<String> = mutableSetOf()
    var userGuess by mutableStateOf("")
        private set

    private fun pickRandomWordAndShuffle(): String {
        // Continue picking up a new random word until you get one that hasn't been used before
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {           //If the set of usedWords contains the currentWord,
            return pickRandomWordAndShuffle()            //return the method pickRandomWordAndShuffle()
        } else {                                         //Else add the currentWord to the set of usedWords because now we are gonna use it
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)       //Then return the shuffleCurrentWord() method on the currentWord
        }
    }

    private fun shuffleCurrentWord(word: String): String {  //Method to scramble the letters of currentWord
        val tempWord = word.toCharArray()                   //Converting the word to an array of characters so that we can shuffle the characters
        // Scramble the word
        tempWord.shuffle()
        while (String(tempWord).equals(word)) {
            tempWord.shuffle()
        }
        return String(tempWord)
    }

    fun resetGame() {                           //resets and restarts the game
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomWordAndShuffle())
    }

    init {
        resetGame()
    }

    /*The resetGame() method clears the usedWords set and picks a newWord for currentScrambledWord in GameUiState
     using pickRandomWordAndShuffle() as pickRandomWordAndShuffle() returns a string of a shuffled word
     therefore reinitializing the game
    */

    fun updateUserGuess(guessedWord: String){
        userGuess = guessedWord
    }

    fun checkUserGuess() {

        if (userGuess.equals(currentWord, ignoreCase = true)) {
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {                                               //User's guess is wrong
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
            // Reset user guess
            updateUserGuess("")
        }
    }

    private fun updateGameState(updatedScore: Int) {
        if (usedWords.size == MAX_NO_OF_WORDS) {
            _uiState.update { currentState ->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score = updatedScore,
                    isGameOver = true
                )
            }
        }
            else{
                // Normal round in the game
                _uiState.update { currentState ->
                    currentState.copy(
                        isGuessedWordWrong = false,
                        currentScrambledWord = pickRandomWordAndShuffle(),
                        currentWordCount = currentState.currentWordCount.inc(),
                        score = updatedScore
                    )
                }
            // Reset user guess
            updateUserGuess("")
            }
        }


    fun skipWord() {
        updateGameState(_uiState.value.score)
    }
}