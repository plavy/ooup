package mytexteditor.plugins;

import java.util.ArrayList;
import java.util.List;

import mytexteditor.ClipboardStack;
import mytexteditor.EditAction;
import mytexteditor.LinesUtil;
import mytexteditor.Plugin;
import mytexteditor.TextEditor;
import mytexteditor.TextEditorModel;
import mytexteditor.UndoManager;

public class Capitalization extends EditAction implements Plugin {
    private String prevText;
    private TextEditorModel model;

    @Override
    public String getName() {
        return "Capitalize";
    }

    @Override
    public String getDescription() {
        return "Capitalizes first character of each word.";
    }

    @Override
    public void execute_do() {
        prevText = LinesUtil.linesToString(model.getLines());
        List<Integer> indexx = new ArrayList<>();
        indexx.add(0);
        for (int i = 0; i < prevText.length() - 1; i++) {
            if (prevText.charAt(i) == ' ' || prevText.charAt(i) == '\n') {
                indexx.add(i + 1);
            }
        }
        StringBuilder builder = new StringBuilder(prevText);
        for (int i : indexx) {
            builder.setCharAt(i, Character.toUpperCase(builder.charAt(i)));
        }
        model.setLines(LinesUtil.stringToLines(builder.toString()));

    }

    @Override
    public void execute_undo() {
        model.setLines(LinesUtil.stringToLines(prevText));

    }

    @Override
    public void execute(TextEditor editor, TextEditorModel model, UndoManager undoManager,
            ClipboardStack clipboardStack) {
        Capitalization action = new Capitalization();
        action.model = model;
        action.execute_do();
        undoManager.push(action);
    }
}
