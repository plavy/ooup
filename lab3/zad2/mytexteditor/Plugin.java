package mytexteditor;

public interface Plugin {
    public String getName(); // ime plugina (za izbornicku stavku)

    public String getDescription(); // kratki opis

    public void execute(TextEditor editor, TextEditorModel model, UndoManager undoManager,
            ClipboardStack clipboardStack);
}