package mytexteditor.plugins;

import javax.swing.JOptionPane;

import mytexteditor.ClipboardStack;
import mytexteditor.LinesUtil;
import mytexteditor.Plugin;
import mytexteditor.TextEditor;
import mytexteditor.TextEditorModel;
import mytexteditor.UndoManager;

public class Statistics implements Plugin {
    @Override
    public String getName() {
        return "Statistics";
    }

    @Override
    public String getDescription() {
        return "Calculates number of rows, words, and letters";
    }

    @Override
    public void execute(TextEditor editor, TextEditorModel model, UndoManager undoManager,
            ClipboardStack clipboardStack) {
                String text = LinesUtil.linesToString(model.getLines());
                int rows = model.getLines().size();
                int words = text.split("\\s+").length;
                int letters = 0;
                for (int i = 0; i < text.length(); i++) {
                    if(Character.isLetter(text.charAt(i))) {
                        letters++;
                    }
                }
                String message = "Rows " + rows + "\nWords: " + words + "\nLetters: " + letters;
        JOptionPane.showMessageDialog(editor, message , getName(), JOptionPane.INFORMATION_MESSAGE);
    }
}
