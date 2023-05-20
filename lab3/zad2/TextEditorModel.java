import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TextEditorModel {
    private List<String> lines = new ArrayList<>();
    private Location cursorLocation = null;
    private LocationRange selectionRange = null;
    private List<CursorObserver> cursorObservers = new ArrayList<>();
    private List<TextObserver> textObservers = new ArrayList<>();

    TextEditorModel(String text) {

        int i = 0;
        int maxLineLen = 40;
        while (text.length() - i > maxLineLen) {
            String subtext = text.substring(i, i + maxLineLen);
            int lastSpace = subtext.lastIndexOf(" ") + text.substring(0, i).length() + 1;
            lines.add(text.substring(i, lastSpace));
            i = lastSpace;
        }
        if (!text.substring(i, text.length()).isEmpty()) {
            lines.add(text.substring(i, text.length()));
        }
        cursorLocation = new Location(lines.size() - 1, lines.get(lines.size() - 1).length());
    }

    // public List<String> getLines() {
    // return this.lines;
    // }

    public String getLine(int i) {
        return this.lines.get(i);
    }

    private void setLine(int i, String line) {
        this.lines.set(i, line);
    }

    private void insertLine(int i, String line) {
        this.lines.add(i, line);
    }

    private void insertLines(int i, List<String> lines) {
        this.lines.addAll(i, lines);
    }

    private void removeLine(int i) {
        this.lines.remove(i);
    }

    // Iterators

    Iterator<String> allLines() {
        Iterator<String> it = new Iterator<String>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < lines.size();
            }

            @Override
            public String next() {
                return lines.get(index++);
            }

        };
        return it;
    }

    Iterator<String> linesRange(int start, int end) {
        Iterator<String> it = new Iterator<String>() {
            private int index = start;

            @Override
            public boolean hasNext() {
                return index < end;
            }

            @Override
            public String next() {
                return lines.get(index++);
            }

        };
        return it;
    }

    // CursorObserver

    public void addCursorObserver(CursorObserver ob) {
        cursorObservers.add(ob);
        notifyCursorObservers();
    }

    public void removeCursorObserver(CursorObserver ob) {
        cursorObservers.remove(ob);
    }

    public void notifyCursorObservers() {
        for (CursorObserver ob : cursorObservers) {
            ob.updateCursorLocation(cursorLocation);
        }
    }

    // Move cursor

    public void moveCursorLeft() {
        int cursorColumn = cursorLocation.getColumn();
        if (cursorColumn > 0) {
            cursorLocation.setColumn(cursorColumn - 1);
            notifyCursorObservers();
        }
    }

    public void moveCursorRight() {
        int cursorColumn = cursorLocation.getColumn();
        if (cursorColumn < lines.get(cursorLocation.getRow()).length()) {
            cursorLocation.setColumn(cursorColumn + 1);
            notifyCursorObservers();
        }
    }

    public void moveCursorUp() {
        int cursorRow = cursorLocation.getRow();
        if (cursorRow > 0) {
            cursorLocation.setRow(cursorRow - 1);
            int currentLineLen = lines.get(cursorRow - 1).length();
            if (cursorLocation.getColumn() > currentLineLen) {
                cursorLocation.setColumn(currentLineLen);
            }
            notifyCursorObservers();
        }
    }

    public void moveCursorDown() {
        int cursorRow = cursorLocation.getRow();
        if (cursorRow + 1 < lines.size()) {
            cursorLocation.setRow(cursorRow + 1);
            int currentLineLen = lines.get(cursorRow + 1).length();
            if (cursorLocation.getColumn() > currentLineLen) {
                cursorLocation.setColumn(currentLineLen);
            }
            notifyCursorObservers();
        }
    }

    // TextObserver

    public void addTextObserver(TextObserver ob) {
        textObservers.add(ob);
    }

    public void removeTextObserver(TextObserver ob) {
        textObservers.remove(ob);
    }

    public void notifyTextObservers() {
        for (TextObserver ob : textObservers) {
            ob.updateText();
        }
    }

    // Delete text

    public void deleteOneChar(int i) {
        int row = cursorLocation.getRow();
        int column = cursorLocation.getColumn();
        StringBuilder lineBuilder = new StringBuilder(lines.get(row));
        if (0 <= column + i && column + i < lineBuilder.length()) {
            lineBuilder.deleteCharAt(column + i);
            if (i < 0) {
                moveCursorLeft();
            }
            setLine(row, lineBuilder.toString());
            notifyTextObservers();
        } else if (getLine(row).isEmpty()) {
            removeLine(row);
            moveCursorUp();
            notifyTextObservers();
        }
    }

    public void deleteBefore() {
        deleteOneChar(-1);
    }

    public void deleteAfter() {
        deleteOneChar(0);
    }

    public void deleteRange(LocationRange range) {
        int row = range.getStart().getRow();
        int column1 = range.getStart().getColumn();
        int column2 = range.getStop().getColumn();
        StringBuilder lineBuilder = new StringBuilder(lines.get(row));
        lineBuilder.delete(column1, column2);
        if (range.getStart().isLowerThan(cursorLocation)) {
            for (int i = column1; i < column2; i++) {
                moveCursorLeft();
            }
        }
        setLine(row, lineBuilder.toString());
        notifyTextObservers();
    }

    // Select text

    public LocationRange getSelectionRange() {
        return this.selectionRange;
    }

    public void setSelectionRange(LocationRange range) {
        this.selectionRange = range;
    }

    public void setSelectionStart(Location start) {
        this.selectionRange.setStart(start);
    }

    public void setSelectionStop(Location stop) {
        this.selectionRange.setStop(stop);
    }

    // Insert text

    public void insert(char c) {
        int row = cursorLocation.getRow();
        int column = cursorLocation.getColumn();
        StringBuilder lineBuilder = new StringBuilder(lines.get(row));
        lineBuilder.insert(column, c);
        setLine(row, lineBuilder.toString());
        moveCursorRight();
        notifyTextObservers();
    }

    public void insert(String text) {
        int row = cursorLocation.getRow();
        int column = cursorLocation.getColumn();
        StringBuilder lineBuilder = new StringBuilder(lines.get(row));
        lineBuilder.insert(column, text);
        List<String> new_lines = Arrays.asList(lineBuilder.toString().split("\n", -1));
        removeLine(row);
        insertLines(row, new_lines);
        for (int i = 0; i < new_lines.size() - 1; i++) {
            moveCursorDown();
        }
        while(cursorLocation.getColumn() > 0) {
            moveCursorLeft();
        }
        for (int i = 0; i < text.split("\n", -1)[text.split("\n", -1).length - 1].length(); i++) {
            moveCursorRight();
        }
        notifyTextObservers();
    }
}