import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TextEditorModel {
    private List<String> lines = new ArrayList<>();
    private Location cursorLocation = null;
    private LocationRange selectionRange = null;
    private List<CursorObserver> cursorObservers = new ArrayList<>();
    private List<TextObserver> textObservers = new ArrayList<>();
    private UndoManager undoManager;

    TextEditorModel(String text, UndoManager undoManager) {
        this.undoManager = undoManager;

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

    public List<String> getLines() {
        return this.lines;
    }

    public void setLines(List<String> lines) {
        this.lines = lines;
        notifyTextObservers();
    }

    public String getLine(int i) {
        return this.lines.get(i);
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
        int cursorRow = cursorLocation.getRow();
        int cursorColumn = cursorLocation.getColumn();
        if (cursorColumn > 0) {
            cursorLocation.setColumn(cursorColumn - 1);
        } else if (cursorRow > 0 && cursorColumn == 0) {
            cursorLocation.setRow(cursorRow - 1);
            cursorLocation.setColumn(getLine(cursorRow - 1).length());
        }
        notifyCursorObservers();
    }

    public void moveCursorRight() {
        int currentRow = cursorLocation.getRow();
        int cursorColumn = cursorLocation.getColumn();
        int currentLineLen = lines.get(currentRow).length();
        if (cursorColumn < currentLineLen) {
            cursorLocation.setColumn(cursorColumn + 1);
        } else if (currentRow < getLines().size() - 1 && cursorColumn == currentLineLen) {
            cursorLocation.setRow(currentRow + 1);
            cursorLocation.setColumn(0);
        }
        notifyCursorObservers();
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

    public void moveCursorTo(Location new_location) {
        cursorLocation.setRow(new_location.getRow());
        cursorLocation.setColumn(new_location.getColumn());
        notifyCursorObservers();
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

    private class DeleteAction extends EditAction {
        LocationRange range;
        Location start;
        String deleted_text;

        public DeleteAction(LocationRange range) {
            this.range = range;
        }

        @Override
        public void execute_do() {
            start = range.getStart();
            Location stop = range.getStop();
            String text = LinesUtil.linesToString(lines);
            int startIndex = LinesUtil.locationToIndex(lines, text, start);
            int stopIndex = LinesUtil.locationToIndex(lines, text, stop);
            deleted_text = text.substring(startIndex, stopIndex);
            String final_text = new StringBuilder(text).delete(startIndex, stopIndex).toString();
            setLines(LinesUtil.stringToLines(final_text));
            moveCursorTo(LinesUtil.indexToLocation(lines, final_text, startIndex));
        }

        @Override
        public void execute_undo() {
            moveCursorTo(start);
            new InsertAction(deleted_text).execute_do();
        }
    }

    public void deleteRange(LocationRange range) {
        DeleteAction action = new DeleteAction(range);
        undoManager.push(action);
        action.execute_do();
    }

    public void deleteBefore() {
        Location stop = new Location(cursorLocation);
        moveCursorLeft();
        Location start = new Location(cursorLocation);
        deleteRange(new LocationRange(start, stop));
    }

    public void deleteAfter() {
        Location start = new Location(cursorLocation);
        moveCursorRight();
        Location stop = new Location(cursorLocation);
        deleteRange(new LocationRange(start, stop));
    }

    // Select text

    public LocationRange getSelectionRange() {
        return this.selectionRange;
    }

    public void setSelectionRange(LocationRange range) {
        this.selectionRange = range;
        notifyTextObservers();
    }

    public void setSelectionStart(Location start) {
        this.selectionRange.setStart(start);
        notifyTextObservers();
    }

    public void setSelectionStop(Location stop) {
        this.selectionRange.setStop(stop);
        notifyTextObservers();
    }

    // Insert text

    public class InsertAction extends EditAction {
        String new_text;
        Location start;
        Location stop;

        public InsertAction(String new_text) {
            this.new_text = new_text;
        }

        @Override
        public void execute_do() {
            if (selectionRange != null) {
                deleteRange(selectionRange);
            }
            String text = LinesUtil.linesToString(lines);
            StringBuilder builder = new StringBuilder(text);
            start = new Location(cursorLocation);
            int index = LinesUtil.locationToIndex(lines, text, start);
            builder.insert(index, new_text);
            setLines(LinesUtil.stringToLines(builder.toString()));
            stop = LinesUtil.indexToLocation(lines, builder.toString(), index + new_text.length());
            moveCursorTo(stop);
        }

        @Override
        public void execute_undo() {
            new DeleteAction(new LocationRange(start, stop)).execute_do();
        }
    }

    public void insert(String new_text) {
        InsertAction action = new InsertAction(new_text);
        undoManager.push(action);
        action.execute_do();
    }

    public void insert(char c) {
        insert(String.valueOf(c));
    }

    // Undo manager

    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }
}