import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static void trainPerceptron(Perceptron englishPerceptron,
                                        Perceptron polishPerceptron,
                                        Perceptron italianPerceptron,
                                        String trainingPath) {
        int epochs = 50;
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
    }
}