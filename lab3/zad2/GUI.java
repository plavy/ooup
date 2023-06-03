import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class GUI extends JFrame implements KeyListener, TextObserver, UndoManagerObserver, ClipboardObserver {
    TextEditorModel model;
    TextEditor editor;
    UndoManager undoManager;
    ClipboardStack clipboard;
    JMenuItem undoItem;
    JMenuItem redoItem;
    JMenuItem cutItem;
    JMenuItem copyItem;
    JMenuItem pasteItem;
    JMenuItem pasteTakeItem;
    JMenuItem deleteSelItem;

    public GUI(TextEditor editor, TextEditorModel model, UndoManager undoManager, ClipboardStack clipboard) {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        
        this.editor = editor;

        this.model = model;
        model.addTextObserver(this);

        this.undoManager = undoManager;
        undoManager.addUndoObserver(this);
        
        this.clipboard = clipboard;
        clipboard.addClipboardObserver(this);

        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.PAGE_START);
        setJMenuBar(menuBar);

        // File menu
        JMenu fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        JMenuItem openItem = new JMenuItem("Open");
        fileMenu.add(openItem);
        JMenuItem saveItem = new JMenuItem("Save");
        fileMenu.add(saveItem);
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                System.exit(0);
            }
        });
        fileMenu.add(exitItem);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        menuBar.add(editMenu);
        undoItem = new JMenuItem("Undo");
        undoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_undo();
            }
        });
        editMenu.add(undoItem);

        redoItem = new JMenuItem("Redo");
        redoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_redo();
            }
        });
        editMenu.add(redoItem);

        cutItem = new JMenuItem("Cut");
        cutItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_cut();
            }
        });
        editMenu.add(cutItem);

        copyItem = new JMenuItem("Copy");
        copyItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_copy();
            }
        });
        editMenu.add(copyItem);

        pasteItem = new JMenuItem("Paste");
        pasteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_paste();
            }
        });
        editMenu.add(pasteItem);

        pasteTakeItem = new JMenuItem("Paste and Take");
        pasteTakeItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_paste_take();
            }
        });
        editMenu.add(pasteTakeItem);

        deleteSelItem = new JMenuItem("Delete selection");
        deleteSelItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                // TODO
            }
        });
        editMenu.add(deleteSelItem);
        
        updateUndoRedo();
        updateText();
        updateClipboard();

        add(editor, BorderLayout.CENTER);

        pack();
        addKeyListener(this);
        setFocusable(true);
        setLocationRelativeTo(null);

    }

    @Override
    public void keyPressed(KeyEvent event) {
        editor.keyPressed(event);
    }

    @Override
    public void keyReleased(KeyEvent event) {
        if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_W) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Q) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else {
            editor.keyReleased(event);
        }
    }

    @Override
    public void keyTyped(KeyEvent event) {
        editor.keyTyped(event);
    }

    @Override
    public void updateText() {
        if (model.getSelectionRange() != null) {
            cutItem.setEnabled(true);
            copyItem.setEnabled(true);
        } else {
            cutItem.setEnabled(false);
            copyItem.setEnabled(false);
        }
    }

    @Override
    public void updateUndoRedo() {
        if (undoManager.isUndoAvailable()) {
            undoItem.setEnabled(true);
        } else {
            undoItem.setEnabled(false);
        }
        if (undoManager.isRedoAvailable()) {
            redoItem.setEnabled(true);
        } else {
            redoItem.setEnabled(false);
        }
        
    }

    @Override
    public void updateClipboard() {
        if (clipboard.isEmpty()) {
            pasteItem.setEnabled(false);
            pasteTakeItem.setEnabled(false);
        } else {
            pasteItem.setEnabled(true);
            pasteTakeItem.setEnabled(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String initText = "Danas idem u park. Tamo nikad nema kiše, tamo je samo sunce. Volim ići u park kad god mogu.";
                UndoManager undoManager = new UndoManager();
                ClipboardStack clipboard = new ClipboardStack();
                TextEditorModel model = new TextEditorModel(initText, undoManager);
                TextEditor editor = new TextEditor(model, undoManager, clipboard);
                JFrame gui = new GUI(editor, model, undoManager, clipboard);
                gui.setVisible(true);
            }
        });
    }

}
