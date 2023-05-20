import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class ClipboardStack {
    private Stack<String> texts = new Stack<>();
    private List<ClipboardObserver> clipboardObservers = new ArrayList<>();

    public void push(String text) {
        this.texts.push(text);
    }

    public String pop() {
        return this.texts.pop();
    }

    public String peek() {
        return this.texts.peek();
    }

    public boolean isEmpty() {
        return this.texts.isEmpty();
    }

    public void clear() {
        this.texts.clear();
    }

    public void addClipboardObserver(ClipboardObserver ob) {
        clipboardObservers.add(ob);
    }

    public void removeClipboardObserver(ClipboardObserver ob) {
        clipboardObservers.remove(ob);
    }

    public void notifyClipboardObservers() {
        for (ClipboardObserver ob : clipboardObservers) {
            ob.updateClipboard();
        }
    }

}
