import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TestManager {
    private final Perceptron englishPerceptron;
    private final Perceptron polishPerceptron;
    private final Perceptron italianPerceptron;

    public TestManager(Perceptron englishPerceptron, Perceptron polishPerceptron, Perceptron italianPerceptron) {
        this.englishPerceptron = englishPerceptron;
        this.polishPerceptron = polishPerceptron;
        this.italianPerceptron = italianPerceptron;
    }
    public TestManager(Perceptron englishPerceptron, Perceptron polishPerceptron, Perceptron italianPerceptron, String content) {
        this.englishPerceptron = englishPerceptron;
        this.polishPerceptron = polishPerceptron;
        this.italianPerceptron = italianPerceptron;
    }

    public Language detectLanguage(String contentToTest){
        Function<Character, Character> convertChar = c -> {
            switch (c) {
                case 'ą':
                case 'à': return 'a';
                case 'ć': return 'c';
                case 'è':
                case 'é':
                case 'ę': return 'e';
                case 'ł': return 'l';
                case 'ń': return 'n';
                case 'ó':
                case 'ò': return 'o';
                case 'ś': return 's';
                case 'ź':
                case 'ż': return 'z';
                case 'ì': return 'i';
                case 'ù': return 'u';
                default: return c;
            }
        };

        String content = contentToTest.toLowerCase().replaceAll("\\s+", "");
        String preparedContent = content.chars()
                .mapToObj(ch -> convertChar.apply((char) ch))
                .map(String::valueOf)
                .collect(Collectors.joining());

        // First, collect frequencies in a map (only for letters a-z)
        Map<Integer, Double> frequencyMap = preparedContent.chars()
                .mapToObj(ch -> (char) ch)
                .filter(c -> c >= 'a' && c <= 'z')
                .collect(Collectors.groupingBy(
                        Function.identity(),
                        Collectors.collectingAndThen(Collectors.counting(),
                                count -> count / (double) preparedContent.length())
                ))
                .entrySet().stream()
                // Map the Character key to an index (0 for 'a', 1 for 'b', etc.)
                .collect(Collectors.toMap(
                        entry -> entry.getKey() - 'a',
                        Map.Entry::getValue
                ));

        // Create the frequency vector with default value 0.0 for missing letters
        double[] vectorInput = new double[26];
        for (int i = 0; i < 26; i++) {
            vectorInput[i] = frequencyMap.getOrDefault(i, 0.0);
        }

        int englishResult = englishPerceptron.compute(vectorInput);
        int polishResult = polishPerceptron.compute(vectorInput);
        int italianResult = italianPerceptron.compute(vectorInput);

        if (englishResult == 1) {
            return Language.English;
        } else if (polishResult == 1) {
            return Language.Polish;
        } else if (italianResult == 1) {
            return Language.Italian;
        } else {
            return null; // No language detected
        }
    }


    public Language detectLanguage(Path pathToTest){
        try {
            String content = new String(Files.readAllBytes(pathToTest));
            return detectLanguage(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
