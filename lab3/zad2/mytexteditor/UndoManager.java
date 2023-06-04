package mytexteditor;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class UndoManager {
    private static UndoManager singleton;
    private Stack<EditAction> undoStack = new Stack<>();
    private Stack<EditAction> redoStack = new Stack<>();
    private List<UndoManagerObserver> undoObservers = new ArrayList<>();

    private UndoManager() {
    }

    public static UndoManager instance() {
        if (singleton == null) {
            singleton = new UndoManager();
        }
        return singleton;
    }

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
        notifyUndoObservers();
    }

    public void redo() {
        EditAction action = redoStack.pop();
        undoStack.push(action);
        action.execute_do();
        notifyUndoObservers();
    }

    public void push(EditAction c) {
        redoStack.clear();
        undoStack.push(c);
        notifyUndoObservers();
    }

    public void clear() {
        redoStack.clear();
        undoStack.clear();
        notifyUndoObservers();
    }

    public void addUndoObserver(UndoManagerObserver ob) {
        undoObservers.add(ob);
    }

    public void removeUndoObserver(UndoManagerObserver ob) {
        undoObservers.remove(ob);
    }

    public void notifyUndoObservers() {
        for (UndoManagerObserver ob : undoObservers) {
            ob.updateUndoRedo();
        }
    }
}
