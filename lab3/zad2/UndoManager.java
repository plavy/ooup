import java.util.Stack;

public class UndoManager {
    private Stack<EditAction> undoStack = new Stack<>();
    private Stack<EditAction> redoStack = new Stack<>();

    public boolean isUndoAvailable() {
        return !undoStack.isEmpty();
    }
    
    public boolean isRedoAvailable() {
        return !redoStack.isEmpty();
    }

    public void undo() {
        EditAction action = undoStack.pop();
        redoStack.push(action);
        action.execute_undo();
    }

    public void redo() {
        EditAction action = redoStack.pop();
        undoStack.push(action);
        action.execute_do();
    }

    public void push(EditAction c) {
        redoStack.clear();
        undoStack.push(c);
    }
}
