# DO NOT TAKE THIS SERIOUSLY
this is an "anti-code golfed" solution to a funny uni problem for a uni I'm not even in, hi pascal btw
## we love jit we love jit we love jit


# Assignment 1 - Hangman game

Hangman is a guessing game for two players. The first player thinks of a word and the second player tries to guess it by consecutively suggesting letters with only a fixed amount of wrong guesses allowed.

## Implementation

Implement hangman as a console application. Note the following key points:
* The first player types the secret word in the console.
* Scan the word and store it letter by letter in an array.
* The second player repeatedly guesses a letter by typing it in the console.
* Scan each letter and compare it to the letters in the array.
* Reveal the correct letters at their positions within the word.
* The game is over if all letters are correctly guessed or after 6 failed guesses.

**Important:**
The file `src/test/java/HangmanGameTest.java` contains automated tests that 
GitHub runs automatically. Make sure of the following:

* If the player guesses the secret word, print the keyword "Congratulations!".
* If the player fails to guess the word, print the keyword "Game over!".

This allows the automatic tests to detect whether the player has won or lost.

## Example run

```
Enter the secret word:
java

Current progress: 
_ _ _ _ 
You have 6 wrong guesses left.
Guess a letter: j

Current progress: 
j _ _ _ 
You have 6 wrong guesses left.
Guess a letter: w
Wrong guess!

Current progress: 
j _ _ _ 
You have 5 wrong guesses left.
Guess a letter: a

Current progress: 
j a _ a 
You have 5 wrong guesses left.
Guess a letter: v
Congratulations! You've guessed the word: java
```
