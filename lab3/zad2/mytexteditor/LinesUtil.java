package mytexteditor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LinesUtil {

    public static int locationToIndex(List<String> lines, String string, Location location) {
        int index = -1;
        int currentRow = 0;
        int currentColumn = 0;

        for (int i = 0; i <= string.length(); i++) {
            if (currentRow == location.getRow() && currentColumn == location.getColumn()) {
                index = i;
                break;
            }
            currentColumn++;
            if (string.charAt(i) == '\n') {
                currentRow++;
                currentColumn = 0;
            }
        }
        return index;
    }

    public static Location indexToLocation(List<String> lines, String string, int index) {
        Location location = null;
        int currentRow = 0;
        int currentColumn = 0;

        for (int i = 0; i <= string.length(); i++) {
            if (i == index) {
                location = new Location(currentRow, currentColumn);
                break;
            }
            currentColumn++;
            if (string.charAt(i) == '\n') {
                currentRow++;
                currentColumn = 0;
            }
        }
        return location;
    }

    public static String linesToString(List<String> lines) {
        return lines.stream().collect(Collectors.joining("\n"));
    }

    public static List<String> stringToLines(String string) {
        return Arrays.asList(string.split("\n", -1));
    }
}
