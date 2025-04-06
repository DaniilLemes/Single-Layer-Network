# Language Detection using a Single-Layer Perceptron

## Overview

This project implements a single-layer perceptron network designed to detect the language of a text based on letter frequency analysis. The system supports three languages: **English**, **Polish**, and **Italian**. By processing training texts, the program calculates a normalized frequency vector for the 26 letters of the English alphabet and trains a separate perceptron for each language. At runtime, the network classifies an input text (either passed as a file or via the console) into one of the supported languages.


## Project Structure

```
Project Root
│
├── Training
│   ├── English
│   │   ├── 1.txt
│   │   ├── 2.txt
│   │   └── 3.txt
│   ├── Italian
│   │   ├── 1.txt
│   │   ├── 2.txt
│   │   └── 3.txt
│   └── Polish
│       ├── 1.txt
│       ├── 2.txt
│       └── 3.txt
│
├── Test
│   ├── English
│   ├── Italian
│   └── Polish
│
└── src
    ├── Main.java
    ├── DataPrepareManager.java
    ├── Perceptron.java
    ├── TestManager.java
    └── Language.java
```

## How It Works

### Data Preparation

- **Text Processing:**  
  The input text is converted to lowercase, and whitespace is removed. Language-specific characters (e.g., accented letters in Italian or diacritical marks in Polish) are mapped to their base English equivalents using a conversion lambda function.

- **Frequency Vector Creation:**  
  The processed text is used to calculate a frequency map for letters 'a' to 'z'. A 26-element array is then generated, where each index represents the normalized frequency of a specific letter. This ensures that each text is represented by a fixed-size input vector.

### Single-Layer Perceptron Network

- **Network Architecture:**  
  The network consists of three perceptrons—one for each language (English, Polish, Italian). Each perceptron has:
    - 26 input weights (one per letter).
    - A bias term.
    - A binary output indicating whether the text is recognized as the corresponding language.

- **Training:**  
  The perceptrons are trained using the frequency vectors computed from the training texts. For each training sample, the expected output is 1 for the perceptron corresponding to the sample’s language and 0 for the others. The training loop runs for a configurable number of epochs.

- **Language Detection:**  
  After training, the network can classify new texts. When a new text is provided, its frequency vector is computed and fed to each perceptron. The perceptron with an output of 1 indicates the detected language.

### Accuracy Calculation

A helper function is implemented to evaluate the model's performance on test data by:
- Iterating over test files for each language.
- Using the trained perceptrons to detect the language.
- Comparing predictions with expected results and calculating the percentage of correct predictions.

## Running the Program

1. **Compile the Project:**

   Ensure you have a Java compiler installed. Compile the project from the project root:

   ```bash
   javac -d out src/*.java
   ```

2. **Run the Program:**

    - **Using Command-Line Input:**  
      To classify text passed as command-line arguments (enclosed in quotes):

      ```bash
      java -cp out Main "Your text input here"
      ```

    - **Using Test Files:**  
      If no command-line input is provided, the program automatically uses the test files located in the `Test` directory.

3. **File Paths:**

   The project expects the `Training` and `Test` directories to be present in the project root. If you run the program from an IDE, verify that the working directory is set to the project root, or adjust the relative paths accordingly.

## Customization

- **Training Parameters:**  
  Modify the number of epochs and the number of samples per language by adjusting variables in the `Main.java` file.

- **Character Mapping:**  
  You can extend or adjust the character conversion functions in `DataPrepareManager.java` and `TestManager.java` if additional language-specific processing is required.

- **Accuracy Function:**  
  The method for calculating accuracy can be further customized to suit your evaluation needs.

## Conclusion

This project showcases a practical implementation of a single-layer perceptron for language detection based on letter frequency. It is a modular and extensible solution that allows for:
- Easy addition of new languages.
- Flexible input methods (file-based or console input).
- Clear separation of training, testing, and evaluation phases.

Feel free to enhance and customize the solution further. Happy coding!

---