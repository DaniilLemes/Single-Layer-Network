import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static double calculateAccuracy(String testPath, int samplesPerLanguage,
                                           Perceptron englishPerceptron,
                                           Perceptron polishPerceptron,
                                           Perceptron italianPerceptron) {
        int totalSamples = 0;
        int correctPredictions = 0;

        // Create a TestManager instance for language detection from file path
        TestManager testManager = new TestManager(englishPerceptron, polishPerceptron, italianPerceptron);

        // Iterate over each language folder and sample file
        for (Language language : Language.values()) {
            for (int i = 1; i <= samplesPerLanguage; i++) {
                String filePath = testPath + "/" + language.toString() + "/" + i + ".txt";
                // Use the detectLanguage(Path) method to get the predicted language
                Language detectedLanguage = testManager.detectLanguage(Paths.get(filePath));
                totalSamples++;
                if (detectedLanguage == language) {
                    correctPredictions++;
                }
            }
        }

        // Calculate accuracy as a percentage
        return (correctPredictions / (double) totalSamples) * 100;
    }


    private static void trainPerceptron(Perceptron englishPerceptron,
                                        Perceptron polishPerceptron,
                                        Perceptron italianPerceptron,
                                        String trainingPath) {
        int epochs = 1000;
        int samplesPerLanguage = 3;

        for(int epoch = 0; epoch < epochs; epoch++){
            //System.out.println("Epoch: " + epoch);
            for (Language language : Language.values()){
                for(int  i = 1; i <= samplesPerLanguage; i++){
                    String filePath = trainingPath + "/" + language.toString() + "/" + i + ".txt";
                    double[] vectorInput = DataPrepareManager.getFrequencyVector(filePath, language);

                    englishPerceptron.learn(vectorInput, language == Language.English ? 1 : 0);
                    polishPerceptron.learn(vectorInput, language == Language.Polish ? 1 : 0);
                    italianPerceptron.learn(vectorInput, language == Language.Italian ? 1 : 0);
                }
            }
        }
    }

    public static void main(String[] args) {
        Perceptron englishPerceptron = new Perceptron(26, 1, 0.5);
        Perceptron polishPerceptron = new Perceptron(26, 1, 0.5);
        Perceptron italianPerceptron = new Perceptron(26, 1, 0.5);

        Path trainigPath = Paths.get("Training");
        trainPerceptron(englishPerceptron, polishPerceptron, italianPerceptron, trainigPath.toString());

        Path testPath = Paths.get("Test");
        if (args.length > 0) {
            String inputText = String.join(" ", args);
            TestManager testManager = new TestManager(englishPerceptron, polishPerceptron, italianPerceptron, inputText);
            Language detectedLanguage = testManager.detectLanguage(inputText);
            System.out.println("Detected language: " + detectedLanguage);
        }
        else {
            System.out.println("Using default test files.");
            for (int i = 1; i < 3; i++){
                for (Language language : Language.values()) {
                    String filePath = testPath + "/" + language.toString() + "/" + i + ".txt";
                    double[] vectorInput = DataPrepareManager.getFrequencyVector(filePath, language);
                    int englishResult = englishPerceptron.compute(vectorInput);
                    int polishResult = polishPerceptron.compute(vectorInput);
                    int italianResult = italianPerceptron.compute(vectorInput);

                    System.out.println("File: " + filePath);
                    System.out.println("English: " + englishResult);
                    System.out.println("Polish: " + polishResult);
                    System.out.println("Italian: " + italianResult);
                }
            }

        }

        double accuracy = calculateAccuracy(testPath.toString(), 2, englishPerceptron, polishPerceptron, italianPerceptron);
        System.out.println("Test accuracy: " + accuracy + "%");
    }
}