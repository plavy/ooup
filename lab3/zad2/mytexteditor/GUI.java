package mytexteditor;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

public class GUI extends JFrame
        implements KeyListener, TextObserver, UndoManagerObserver, ClipboardObserver, CursorObserver {
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
    JButton undoButton;
    JButton redoButton;
    JButton cutButton;
    JButton copyButton;
    JButton pasteButton;
    JLabel statusBar;
    Location cursorLocation;

    public GUI(TextEditor editor, TextEditorModel model, UndoManager undoManager, ClipboardStack clipboard) {
        super("Text Editor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());

        this.editor = editor;

        this.model = model;
        model.addTextObserver(this);
        model.addCursorObserver(this);

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
        openItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser("Open");
                int selected = fileChooser.showOpenDialog(editor);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        Path src = Paths.get(fileName);
                        model.setLines(Files.readAllLines(src));
                        int lastRow = model.getLines().size() - 1;
                        model.moveCursorTo(new Location(lastRow, model.getLine(lastRow).length()));
                        undoManager.clear();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error while opening file!");
                    }
                }
            }
        });
        fileMenu.add(openItem);

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser("Save");
                int selected = fileChooser.showSaveDialog(editor);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        Path dest = Paths.get(fileName);
                        Files.write(dest, model.getLines());
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Error while saving file!");
                    }
                }
            }
        });
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
                model.deleteRange(model.getSelectionRange());
            }
        });
        editMenu.add(deleteSelItem);

        JMenuItem clearItem = new JMenuItem("Clear document");
        clearItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                model.clear();
            }
        });
        editMenu.add(clearItem);

        // Move menu
        JMenu moveMenu = new JMenu("Move");
        menuBar.add(moveMenu);

        JMenuItem cursorStartItem = new JMenuItem("Cursor to document start");
        cursorStartItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                model.moveCursorTo(new Location(0, 0));
            }
        });
        moveMenu.add(cursorStartItem);

        JMenuItem cursorEndItem = new JMenuItem("Cursor to document end");
        cursorEndItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                int lastRow = model.getLines().size() - 1;
                model.moveCursorTo(new Location(lastRow, model.getLine(lastRow).length()));
            }
        });
        moveMenu.add(cursorEndItem);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        add(toolbar, BorderLayout.PAGE_START);

        undoButton = new JButton("Undo");
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_undo();
            }
        });
        undoButton.setFocusable(false);
        toolbar.add(undoButton);

        redoButton = new JButton("Redo");
        redoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_redo();
            }
        });
        redoButton.setFocusable(false);
        toolbar.add(redoButton);

        cutButton = new JButton("Cut");
        cutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_cut();
            }
        });
        cutButton.setFocusable(false);
        toolbar.add(cutButton);

        copyButton = new JButton("Copy");
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_copy();
            }
        });
        copyButton.setFocusable(false);
        toolbar.add(copyButton);

        pasteButton = new JButton("Paste");
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                editor.action_paste();
            }
        });
        pasteButton.setFocusable(false);
        toolbar.add(pasteButton);

        // Status bar
        statusBar = new JLabel("Status bar");
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.LIGHT_GRAY);
        updateCursorLocation(model.getCursorLocation());
        add(statusBar, BorderLayout.PAGE_END);

        // Plugins
        JMenu pluginsMenu = new JMenu("Plugins");
        menuBar.add(pluginsMenu);

        File pluginsDir = new File(getClass().getResource("plugins").getFile());
        String[] files = pluginsDir.list();
        for (String file : files) {
            String className = file.substring(0, file.length() - 6);
            try {
                @SuppressWarnings("unchecked")
                Class<Plugin> clazz = (Class<Plugin>) Class.forName("mytexteditor.plugins." + className);
                Plugin plugin = (Plugin) clazz.getConstructor().newInstance();
                System.out.println("Loaded plugin " + className);
                JMenuItem pluginItem = new JMenuItem(plugin.getName());
                pluginItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        plugin.execute(editor, model, undoManager, clipboard);
                    }
                });
                pluginsMenu.add(pluginItem);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error loading plugin " + className + "!");
            }
        }

        updateUndoRedo();
        updateText();
        updateClipboard();
        updateStatusBar();

        add(editor, BorderLayout.CENTER);

        pack();
        addKeyListener(this);
        setFocusable(true);
        setLocationRelativeTo(null);

    }

    private void updateStatusBar() {
        int n = model.getLines().size();
        int row = cursorLocation.getRow();
        int column = cursorLocation.getColumn();
        statusBar.setText("Lines: " + n + " | Row: " + row + " | Column: " + column);
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
            cutButton.setEnabled(true);
            copyButton.setEnabled(true);
            deleteSelItem.setEnabled(true);
        } else {
            cutItem.setEnabled(false);
            copyItem.setEnabled(false);
            cutButton.setEnabled(false);
            copyButton.setEnabled(false);
            deleteSelItem.setEnabled(false);
        }
    }

    @Override
    public void updateUndoRedo() {
        if (undoManager.isUndoAvailable()) {
            undoItem.setEnabled(true);
            undoButton.setEnabled(true);
            ;
        } else {
            undoItem.setEnabled(false);
            undoButton.setEnabled(false);
        }
        if (undoManager.isRedoAvailable()) {
            redoItem.setEnabled(true);
            redoButton.setEnabled(true);
        } else {
            redoItem.setEnabled(false);
            redoButton.setEnabled(false);
        }

    }

    @Override
    public void updateClipboard() {
        if (clipboard.isEmpty()) {
            pasteItem.setEnabled(false);
            pasteTakeItem.setEnabled(false);
            pasteButton.setEnabled(false);
        } else {
            pasteItem.setEnabled(true);
            pasteTakeItem.setEnabled(true);
            pasteButton.setEnabled(true);
        }
    }

    @Override
    public void updateCursorLocation(Location loc) {
        this.cursorLocation = loc;
        updateStatusBar();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                String initText = "Danas idem u park. Tamo nikad nema kiše, tamo je samo sunce. Volim ići u park kad god mogu.";
                UndoManager undoManager = UndoManager.instance();
                ClipboardStack clipboard = new ClipboardStack();
                TextEditorModel model = new TextEditorModel(initText, undoManager);
                TextEditor editor = new TextEditor(model, undoManager, clipboard);
                JFrame gui = new GUI(editor, model, undoManager, clipboard);
                gui.setVisible(true);
            }
        });
    }

}
