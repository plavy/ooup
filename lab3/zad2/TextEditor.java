import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

public class TextEditor extends JComponent implements KeyListener, CursorObserver, TextObserver {
    private TextEditorModel model;
    private Location cursorLocation;
    private ClipboardStack clipboard;
    private UndoManager undoManager;

    public TextEditor(TextEditorModel model, UndoManager undoManager, ClipboardStack clipboard) {
        cursorLocation = new Location(0, 0);
        this.model = model;
        this.undoManager = undoManager;
        this.clipboard = clipboard;
        model.addCursorObserver(this);
        model.addTextObserver(this);
        addKeyListener(this);
        setFocusable(true);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Courier", Font.PLAIN, 20);
        g2d.setFont(font);
        int fontHeight = g2d.getFontMetrics().getHeight();
        int y_offset = 20;
        Iterator<String> allLines = model.allLines();

        // Draw selection
        if (model.getSelectionRange() != null) {
            Location start = model.getSelectionRange().getStart();
            Location stop = model.getSelectionRange().getStop();
            for (int i = start.getRow(); i <= stop.getRow(); i++) {
                int startColumn = i == start.getRow() ? start.getColumn() : 0;
                int stopColumn = i == stop.getRow() ? stop.getColumn() : model.getLine(i).length();
                int stringWidthBefore = g.getFontMetrics()
                        .stringWidth(model.getLine(i).substring(0, startColumn));
                int stringWidth = g.getFontMetrics()
                        .stringWidth(model.getLine(i).substring(startColumn, stopColumn));
                g2d.setColor(Color.YELLOW);
                g2d.fillRect(stringWidthBefore, fontHeight * (i - 1) + y_offset, stringWidth, fontHeight);
            }
        }

        // Draw text
        g2d.setColor(Color.BLACK);
        int i = 0;
        while (allLines.hasNext()) {
            String line = allLines.next();
            g2d.drawString(line, 0, fontHeight * i + y_offset);
            i++;
        }

        // Draw cursor
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(1.0f));
        int stringWidthBefore = g.getFontMetrics()
                .stringWidth(model.getLine(cursorLocation.getRow()).substring(0, cursorLocation.getColumn()));
        g2d.drawLine(stringWidthBefore, fontHeight * (cursorLocation.getRow() - 1) + y_offset + 2, stringWidthBefore,
                fontHeight * (cursorLocation.getRow()) + y_offset);

    }

    @Override
    public void updateCursorLocation(Location cursorLocation) {
        this.cursorLocation = cursorLocation;
        repaint();
    }

    @Override
    public void updateText() {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
    }

    private void moveCursor(Runnable method, boolean isShiftDown) {
        if (isShiftDown) {
            Location before = new Location(cursorLocation);
            method.run();
            Location after = new Location(cursorLocation);
            if (model.getSelectionRange() == null) {
                if (before.isLowerThan(after)) {
                    model.setSelectionRange(new LocationRange(before, after));
                } else {
                    model.setSelectionRange(new LocationRange(after, before));
                }
            } else {
                Location another = after.isLowerThan(before) ? model.getSelectionRange().getStart()
                        : model.getSelectionRange().getStop();
                if (cursorLocation.isLowerThan(another)) {
                    model.setSelectionStart(after);
                } else {
                    model.setSelectionStop(after);
                }
            }
        } else {
            method.run();
        }
    }

    private String getSelectedString() {
        Location start = model.getSelectionRange().getStart();
        Location stop = model.getSelectionRange().getStop();
        List<String> new_lines = new ArrayList<>();
        for (int i = start.getRow(); i <= stop.getRow(); i++) {
            int startColumn = i == start.getRow() ? start.getColumn() : 0;
            int stopColumn = i == stop.getRow() ? stop.getColumn() : model.getLine(i).length();
            new_lines.add(model.getLine(i).substring(startColumn, stopColumn));
        }
        return String.join("\n", new_lines);
    }

    public void action_undo() {
        if (undoManager.isUndoAvailable()) {
            undoManager.undo();
        }
    }

    public void action_redo() {
        if (undoManager.isRedoAvailable()) {
            undoManager.redo();
        }
    }

    public void action_cut() {
        if (model.getSelectionRange() != null) {
            clipboard.push(getSelectedString());
            model.deleteRange(model.getSelectionRange());
        }

    }

    public void action_copy() {
        if (model.getSelectionRange() != null) {
            clipboard.push(getSelectedString());
        }

    }

    public void action_paste() {
        if (!clipboard.isEmpty()) {
            model.insert(clipboard.peek());
        }
    }

    public void action_paste_take() {
        if (!clipboard.isEmpty()) {
            model.insert(clipboard.pop());
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            model.insert("\n");

            // Move cursor
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            moveCursor(() -> model.moveCursorLeft(), event.isShiftDown());
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveCursor(() -> model.moveCursorRight(), event.isShiftDown());
        } else if (event.getKeyCode() == KeyEvent.VK_UP) {
            moveCursor(() -> model.moveCursorUp(), event.isShiftDown());
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
            moveCursor(() -> model.moveCursorDown(), event.isShiftDown());

            // Delete text
        } else if (event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            if (model.getSelectionRange() == null) {
                model.deleteBefore();
            } else {
                model.deleteRange(model.getSelectionRange());
            }
        } else if (event.getKeyCode() == KeyEvent.VK_DELETE) {
            if (model.getSelectionRange() == null) {
                model.deleteAfter();
            } else {
                model.deleteRange(model.getSelectionRange());
            }

            // Insert text
        } else if ((event.getKeyChar() >= 32 && event.getKeyChar() <= 126) // ASCII codes of alphanumeric, operators,
                || (event.getKeyChar() >= 128 && event.getKeyChar() <= 1524)) { // interpunction, including HR
                                                                                // characters...
            model.insert(event.getKeyChar());
            // Clipboard
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_C) {
            action_copy();
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_X) {
            action_cut();
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_V) {
            if (!event.isShiftDown()) {
                action_paste();
            } else {
                action_paste_take();
            }

            // Undo, redo
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Z) {
            action_undo();
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Y) {
            action_redo();
        }

        // Unselect
        if (!event.isShiftDown() && event.getKeyCode() != KeyEvent.VK_SHIFT) {
            model.setSelectionRange(null);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }
}