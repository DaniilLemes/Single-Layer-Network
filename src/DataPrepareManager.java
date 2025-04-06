import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataPrepareManager {
    public static double[] getFrequencyVector(String path, Language language) {
        // Prepare the conversion function based on language
        Function<Character, Character> convertChar;
        switch (language) {
            case Polish:
                convertChar = c -> {
                    switch (c) {
                        case 'ą': return 'a';
                        case 'ć': return 'c';
                        case 'ę': return 'e';
                        case 'ł': return 'l';
                        case 'ń': return 'n';
                        case 'ó': return 'o';
                        case 'ś': return 's';
                        case 'ź':
                        case 'ż': return 'z';
                        default: return c;
                    }
                };
                break;
            case Italian:
                convertChar = c -> {
                    switch (c) {
                        case 'à': return 'a';
                        case 'è':
                        case 'é': return 'e';
                        case 'ì': return 'i';
                        case 'ò': return 'o';
                        case 'ù': return 'u';
                        default: return c;
                    }
                };
                break;
            case English:
            default:
                // For English, no special conversion is needed.
                convertChar = c -> c;
                break;
        }

        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            // Convert to lower case and remove whitespace
            content = content.toLowerCase().replaceAll("\\s+", "");

            // Apply language-specific character conversion
            String preparedContent = content.chars()
                    .mapToObj(ch -> convertChar.apply((char) ch))
                    .map(String::valueOf)
                    .collect(Collectors.joining());

            int totalLength = preparedContent.length();

            // Create a frequency map for characters 'a' to 'z'
            LinkedHashMap<Character, Double> frequencyMap = preparedContent.chars()
                    .mapToObj(ch -> (char) ch)
                    .filter(c -> c >= 'a' && c <= 'z') // Ensure only letters a-z are included
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            LinkedHashMap::new,
                            Collectors.collectingAndThen(Collectors.counting(),
                                    count -> count / (double) totalLength)));

            // Create an array of length 26 with frequencies for letters a-z (0 if absent)
            double[] frequencyVector = new double[26];
            for (int i = 0; i < 26; i++) {
                char letter = (char) ('a' + i);
                frequencyVector[i] = frequencyMap.getOrDefault(letter, 0.0);
            }
            return frequencyVector;
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
            // Return a zero-filled vector in case of error
            return new double[26];
        }
    }
}
