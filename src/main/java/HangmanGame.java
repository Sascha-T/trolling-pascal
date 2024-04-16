// unwillingly commissioned by pascal
import lion.HangmanJIT;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

/*
 * =====================================================================
 * Write your implementation for the assignment at the "TODO" sections.
 * For more details, refer to README.md.
 * =====================================================================
 */

public class HangmanGame {
    static char[] word;
    static char[] guessed;

    // yes we are jit optimized
    static HangmanJIT jit;
    static int guesses;
    static Scanner scanner;

    public static void main(String[] args) {

        // Scanner to read the input
        try (Scanner scanner = new Scanner(System.in)) {
            HangmanGame.scanner = scanner; // trust me bro
            // Read the secret word and turn it into an array of characters
            System.out.println("Enter the secret word:");
            String china = scanner.nextLine();
            word = china.toCharArray();

            if (china.contains("_") || china.contains("\0"))
                throw new RuntimeException("我他妈吃掉你的电脑"); // wallah i shall break your kneecaps
            guessed = new char[word.length];
            jit = HangmanJIT.compile(word);

            // TODO: Initialize an array to track guessed letters, a counter for the
            // number of wrong guesses, etc.

            // TODO: While the game is not over, do the following:
            // 1. Reveal the letters correctly guessed so far.
            // 2. Read the next letter from the standard input.
            // 3. Update according to whether the letter is in the secret word or not.
            // 4. If the player has guessed the word, print "Congratulations! ...".
            // 5. If the player made too many wrong guesses, print "Game Over! ...".

            // constant 虚幻
            for (guesses = 6; guesses > 0; ) {
                char input = printState();
                if (jit.process(guessed, input)) {
                    System.out.println("Wrong guess!");
                    guesses--;
                }
                boolean yup = true;
                for (int i = 0; i < word.length; i++)
                    if (word[i] != guessed[i])
                        yup = false;
                if (yup) {
                    System.out.print("Congratulations! You've guessed the word: ");
                    System.out.printf("%s\n", china);
                    return;
                }
            }
            System.out.println("Game over!");
        }
    }

    // 我喜欢帕斯卡压力
    public static char printState() {
        try {
            // mr pascal i am begging please let me take part in your project
            try (BufferedWriter writer = new BufferedWriter(new DoNotDevice(new OutputStreamWriter(System.out)))) {
                writer.newLine();
                ((BufferedWriter) writer.append("Current progress: ")).newLine();
                for (char car : guessed)
                    writer.append(String.format("%s ", car == 0 ? "_" : car));
                writer.newLine();;
                ((BufferedWriter) writer.append("You have ").append(Integer.toString(guesses, 10)).append(" wrong guesses left.")).newLine();
                writer.append("Guess a letter: ");
                writer.flush();
            }
            return scanner.next().toCharArray()[0];
        } catch (Throwable ignored) {
            ignored.printStackTrace();
        } // _ for ignore is jdk21+ feature, i fucking hate my life
        throw new RuntimeException("j'aime brasil");
    }

    public static class DoNotDevice extends Writer {
        Writer x;
        public DoNotDevice(Writer x) {
            this.x = x;
        }
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            x.write(cbuf, off, len);
        }

        @Override
        public void flush() throws IOException {
            x.flush();
        }

        @Override
        public void close() throws IOException {}
    }
}
