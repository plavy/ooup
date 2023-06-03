package mytexteditor.plugins;

import java.util.ArrayList;
import java.util.List;

import mytexteditor.ClipboardStack;
import mytexteditor.LinesUtil;
import mytexteditor.Plugin;
import mytexteditor.TextEditor;
import mytexteditor.TextEditorModel;
import mytexteditor.UndoManager;

public class Capitalization implements Plugin{
    @Override
    public String getName() {
        return "Capitalize";
    }

    @Override
    public String getDescription() {
        return "Capitalizes first character of each word.";
    }

    @Override
    public void execute(TextEditor editor, TextEditorModel model, UndoManager undoManager,
            ClipboardStack clipboardStack) {
        String text = LinesUtil.linesToString(model.getLines());
        List<Integer> indexx = new ArrayList<>();
        indexx.add(0);
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i) == ' ' || text.charAt(i) == '\n') {
                indexx.add(i + 1);
            }
        }
        StringBuilder builder = new StringBuilder(text);
        for (int i : indexx ) {
            builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
        }
        model.setLines(LinesUtil.stringToLines(builder.toString()));   
    }
}
