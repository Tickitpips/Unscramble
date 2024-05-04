# Unscramble
This is a game of guessing English words



This application is a word game where the user is challenged to unscramble English words. Each word is one point. Adapted from:    https://developer.android.com/codelabs/basic-android-kotlin-compose-viewmodel-and-state?continue=https%3A%2F%2Fdeveloper.android.com%2Fcourses%2Fpathways%2Fandroid-basics-compose-unit-4-pathway-1%23codelab-https%3A%2F%2Fdeveloper.android.com%2Fcodelabs%2Fbasic-android-kotlin-compose-viewmodel-and-state#4

Algorithm

Data source (Model)

In this case, you build the app with the following format:

1.	Create a data package and create a kotlin file where you define two constants: the maximum number of words and the score increase.
2.	Create a set of strings and assign it to a variable (allWords). This set will store the words to be used in playing the game. Include the words of the game in the set.
   
Using a data class, define the types of data the app will be passing around. Use constants as variables may be changed and cause the app to misbehave. 

1.	Create a constant ‘currentScrambledWord’ of type String and set it to empty (“ “). This will represent the scrambled word. 
2.	Create a constant ‘isGuessedWordWrong’ of type Boolean and set it to False. This will represent the Boolean state of the correctness of the user’s guess. 
3.	Create a constant ‘score’ of type integer and set it to 0. This will represent the score.
4.	Create a constant ‘currentWordCount’ of type integer and set it to 0. This will represent the number of words played. 
5.	Create a constant ‘isGameOver’ of type Boolean and set it to False. This will represent the Boolean state of whether the game is still on. The game ends after the tenth round and the final score dialogue is displayed.
   
MainActivity

The MainActivity.kt contains the normal code to initialize the application and calls the GameScreen() function. 


Game UI logic

The UI (View) for this game is a composable named GameScreen. 

You begin by including a Column composable and aligning and padding it as required. Also include the remember scroll state. In the Column, 

1.	Start by displaying the name of the application through a Text composable. 
2.	Call the GameLayout function to display the Card composable containing the area of gameplay that consists of the word count, the current unscrambled word, the text field for input, and the instruction.  
3.	Include another Column composable and specify the padding and alignment parameters. In this Column scope, include a Button to submit and check a guess. Include another Outlined Button to skip the word.
   
In the GameLayout function definition,

1.	Include a Card composable to wrap the play area. Under the Card composable, include a Colum composable and specify the required parameters.
2.	Under the Column, include a Text composable to show the word count. This composable should have a background color, clipped, padded, and aligned to the end. Add a Column and under it include a Text composable to display the scrambled word. 
3.	Under the Column in step one; include an Outlined Text Field to capture user input. The label of this Outlined Text Field should have a conditional clause to display ‘Enter your guess’ and when the user’s guess is wrong after checking it should display ‘Wrong guess!’. Set the composable to be singleLine and set the colours and the keyboard actions. 
4.	The parameters should include a variable ‘wordCount’ of Integer type to pass down the word count data from GameUiState, a variable ‘isGuessWrong’ of Boolean type to pass down the Boolean state of isGuessWrong from GameUiState, a variable ‘userGuess’ of type String to pass up the String user input from the Text Field composable, a variable ‘currentScrambledWord’ of type String to pass down the currentWord data from GameUiState, a variable ‘onUserGuessChanged’ lambda of type string that returns a unit to change the UI state of the Text Field on every input of the user, a variable ‘onKeyboardDone’ lambda that returns a unit and calls a function when user presses ‘Done’ on the keyboard.  Lastly, pass modifier: Modifier = Modifier for purposes of modifying composables.
   
After the user has gone through 10 attempts, the game is over. The app needs to display the score and ask the user to quit or play again. The UI for this is a dialogue composable. 
In the FinalScoreDialogue definition, 

Include an AlertDialogue composable, specify the title and pass in a Text composable to display the word ‘Congratulations!’ Specify the text by passing a Text composable to show the final score. Under the dismiss button parameter, call the activity.finish() function that kills the activity. For the confirm button parameter, call the onPlayAgain function.


MVVM Architecture logic

Under the theme directory, create two kotlin class files to represent the ViewModel. Name them GameUiState and GameViewModel. The GameUiState holds the flow of the application data as it is transformed by the ViewModel. The ViewModel component holds and exposes the state the UI consumes. The UI state is application data transformed by ViewModel. ViewModel lets your app follow the architecture principle of driving the UI from the model.

Create a class named GameViewModel that extends the ViewModel() class. Inside the GameViewModel, 

1.	Create a private constant ‘_uiState’ and assign it the value of the mutable flow state of GameUiState. This means that the value of _uiState cannot be changed by other classes because it is private to GameViewModel class; it can only be set within GameViewModel. 
2.	Create a constant ‘uiState’ that accesses the value of _uiState and returns it. This means the value of uiState can’t change but can be accessed only. Since it is not private to GameViewModel, the data will be accessed by other classes. uiState stores the collective values of the state to be displayed in the UI. 
3.	Create a variable (‘currentWord’) to store the current word
4.	Create a set (‘userGuess’) to store used words. 
5.	Define a method pickRandomWordAndShuffle() that returns a string. Set the current word to a randomized pick from ‘allWords’ variable. We need to check if the currentWord has been used before during the game. If it has not been used, we add the word to the set of usedWords and call a method to shuffle the word.  Define a conditional for the logic: if the set usedWords contains currentWord, return pickRandomWordAndShuffle(), which means we rerun the same process of picking a random word from allWords. Otherwise, (on the else statement) we return the shuffleCurrentWord() method and pass currentWord. The shuffleCurrentWord() method takes a String argument, shuffles the characters of the currentWord, and returns a String of a scrambled word which would in turn be returned by pickRandomWordAndShuffle() . 
6.	Define shuffleCurrentWord() method with a ‘word’ parameter, which is of String type, and returns a String. Define a constant ‘tempWord’ and assign it a method that converts the argument of the method shuffleCurrentWord() to an array of characters. Call the shuffle method on tempWord to shuffle the array of Characters. There is a probability that the result of the shuffle will result in an arrangement equal to the original word. Just to make sure, create a while loop: while String of tempWord is the same as word (in the argument), shuffle the tempWord array. 
7.	Define a resetGame() method that resets and restarts the game. When resetting, the app needs to clear the set of usedWords. Thereafter, we pass the pickRandomWordAndShuffle() method to the argument currentScrambledWord. This means that pickRandomWordAndShuffle() method will return a scrambled word and pass it to the currentScrambledWord in the GameUiState.  	
8.	Define updateUserGuess() method that takes a String argument. This function should assign a string passed from the argument to the userGuess variable (declared in step 4). 
9.	Define a checkUserGuess() method that processes the user’s input after the submit button is hit. This takes a conditional logic. If the user’s guess is the same as the current word, the score should be increased otherwise, the app needs to show the user the guessed word is wrong and should therefore try again or skip the word. Define the conditional logic: if the userGuess equals currentWord (we ignore the case of the input), create a new constant ‘updatedScore’ and assign it the value of _uiState plus score increase. Call the updateGameState() method and pass the updatedScore as its argument. Otherwise, update the uiState with a copy of the current state except isGuessedWordWrong becomes true.
10.	Define a updateGameState() method to update the state of the game after the app checks for the correctness of the user’s input. There is a conditional here. If the maximum number of rounds is reached, the app needs to display a dialogue to show the score and ask the user to play again or quit otherwise, the app should update the uiState with a blank textfield prepared for a new input, a new word, and an increased word count. Define the conditional logic: usedWords.size is equal to the maximum number of words, update uiState with a copy of its current state except isGuessedWordWrong = false, score = updatedScore and isGameOver = true. Otherwise update uiState with a copy of its current state except isGuessedWordWrong = false, currentScrambledWord = pickRandomWordAndShuffle(),currentWordCount = currentState.currentWordCount.inc() and score = updatedScore. 
11.	Define a skipWord() method to allow the user to skip a word if they are unable to provide a correct guess. In the definition, call the updateGameState() method and pass the score value of the uiState to maintain the value of the user’s score. This method should check if the maximum number of rounds is reached. 

To pass GameUiState to UI composables,

1.	Under GameScreen(), create a constant ‘gameUiState’ and use the delegate ‘by’ and assign gameViewModel.uiState.collectAsState(). This means that we are accessing the collective state of the UI from the ViewModel class through the uiState constant that returns the state as transformed by the ViewModel. 
2.	When calling the GameLayout() function, set the currentScrambledWord argument to gameUiState.currentScrambledWord, set the onUserGuessChanged argument to gameViewModel.updateUserGuess(it), set the onKeyboardDone to gameViewModel.checkUserGuess(), set the userGuess to gameViewModel.userGuess, set the isGuessWrong to gameUiState.isGuessedWordWrong, and set the wordCount to gameUiState.currentWordCount. 
3.	The value of the currentScrambledWord argument is passed down to a Text composable to display the scrambled word. In the OutlinedTextField composable we have onValueChange set to onUserGuessChanged. When the user inputs anything, there is an onValueChange callback that is set to onUserGuessChange. onUserGuessChange passes the data to updateUserGuess() method in the ViewModel, which takes an argument ‘it’ (The value change). Recall that the function sets the value passed down from the argument to a userGuess variable. The value of the userGuess variable is accessed by the userGuess argument of the GameLayout() function and is passed down to the value callback for displaying by the OutlinedTextField composable. Furthermore, the userGuess variable in the ViewModel will be later used to perform a logic comparison in the checkUserGuess() method. The onDone gives a callback to onKeyBoardDone which is a lambda that calls the function checkUserGuess(). The isGuessWrong state is passed down to the label parameter of the OutlinnedTextField. 
4.	The GameStatus composable has an integer parameter and receives score argument from GameUiState. Here there is the logic to check if isGameOver is true. If it is, the app calls the FinalScoreDialogue() function with the score argument set to the score of GameUiState, and onPlayAgain is a lambda callback to the resetgame() method. 
