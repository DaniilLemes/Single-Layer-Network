import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DataPrepareManager {
    public static HashMap<Character, Double> getFrequencyMap(String path, Language language){

        Function<Character, Character> convertCharFromPolish = c -> {
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

        Function<Character, Character> convertCharFromItalian = c -> {
            switch (c) {
                case 'à': return 'a';
                case 'è':
                case 'é': return 'e';
                case 'ì': return 'i';
                case 'ò': return 'o';
                case 'ù': return 'u';
                default:  return c;
            }
        };

        try {
            String content = new String(Files.readAllBytes(Paths.get(path)));
            content = content.toLowerCase().replaceAll("\\s+", "");

            String preparedContent;

            switch (language) {
                case English:
                    preparedContent = content;
                    break;
                case Polish:
                    preparedContent = content.chars()
                            .mapToObj(ch -> convertCharFromPolish.apply((char) ch))
                            .map(String::valueOf)
                            .collect(Collectors.joining());
                    break;
                case Italian:
                    preparedContent = content.chars()
                            .mapToObj(ch -> convertCharFromItalian.apply((char) ch))
                            .map(String::valueOf)
                            .collect(Collectors.joining());
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported language: " + language);
            }

            int totalLength = preparedContent.length();

            return new HashMap<>(preparedContent.chars()
                    .mapToObj(c -> (char) c)
                    .collect(Collectors.groupingBy(
                            Function.identity(),
                            Collectors.collectingAndThen(
                                    Collectors.counting(),
                                    count -> count / (double) totalLength))));
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return new HashMap<>();
    }
}
