import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TextEditor extends JFrame implements KeyListener, CursorObserver, TextObserver {
    TextEditorModel model;
    Location cursorLocation;
    ClipboardStack clipboard;

    public TextEditor(TextEditorModel model) {
        super("Text Editor");
        cursorLocation = new Location(0, 0);
        clipboard = new ClipboardStack();
        this.model = model;
        model.addCursorObserver(this);
        model.addTextObserver(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        pack();
        addKeyListener(this);
        setFocusable(true);
        setLocationRelativeTo(null);
    }

    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        Font font = new Font("Courier", Font.PLAIN, 20);
        g2d.setFont(font);
        int fontHeight = g2d.getFontMetrics().getHeight();
        int y_offset = 60;
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

    @Override
    public void keyReleased(KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.VK_ENTER) {
            model.insert("\n");
        } else if (event.getKeyCode() == KeyEvent.VK_LEFT) {
            moveCursor(() -> model.moveCursorLeft(), event.isShiftDown());
        } else if (event.getKeyCode() == KeyEvent.VK_RIGHT) {
            moveCursor(() -> model.moveCursorRight(), event.isShiftDown());
        } else if (event.getKeyCode() == KeyEvent.VK_UP) {
            moveCursor(() -> model.moveCursorUp(), event.isShiftDown());
        } else if (event.getKeyCode() == KeyEvent.VK_DOWN) {
            moveCursor(() -> model.moveCursorDown(), event.isShiftDown());
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
        } else if ((event.getKeyChar() >= 32 && event.getKeyChar() <= 126) // ASCII codes of alphanumeric, operators,
                || (event.getKeyChar() >= 128 && event.getKeyChar() <= 1524)) { // interpunction, including HR
                                                                                // characters...
            model.insert(event.getKeyChar());
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_W) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Q) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_C) {
            if (model.getSelectionRange() != null) {
                clipboard.push(getSelectedString());
            }
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_X) {
            if (model.getSelectionRange() != null) {
                clipboard.push(getSelectedString());
                model.deleteRange(model.getSelectionRange());
            }
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_V) {
            if (!clipboard.isEmpty()) {
                if (event.isShiftDown()) {
                    model.insert(clipboard.pop());
                } else {
                    model.insert(clipboard.peek());
                }
            }
        }

        if (!event.isShiftDown() && event.getKeyCode() != KeyEvent.VK_SHIFT) {
            model.setSelectionRange(null);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String init_text = "Danas idem u park. Tamo nikad nema kiše, tamo je samo sunce. Volim ići u park kad god mogu.";
                TextEditorModel model = new TextEditorModel(init_text);
                JFrame editor = new TextEditor(model);
                editor.setVisible(true);
            }
        });
    }
}