import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class GUI extends JFrame implements DocumentModelListener, KeyListener {
    private DocumentModel model;
    private Canvas canvas;

    private JButton load_button;
    private JButton save_button;

    public GUI(List<GraphicalObject> objects) {
        // Window
        super("Vector Draw");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        setFocusable(true);

        // Model
        this.model = new DocumentModel();
        model.addDocumentModelListener(this);

        // Canvas
        this.canvas = new Canvas(model);
        add(this.canvas, BorderLayout.CENTER);
        addKeyListener(this);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setFocusable(false);
        /// Load
        load_button = new JButton("Učitaj");
        load_button.setFocusable(false);
        toolbar.add(load_button);
        load_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser("Load");
                int selected = fileChooser.showOpenDialog(canvas);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    try {
                        Path src = Paths.get(fileName);
                        List<String> lines = Files.readAllLines(src);

                        Map<String, GraphicalObject> id_map = new HashMap<>();
                        for (GraphicalObject o : objects) {
                            id_map.put(o.getShapeID(), o);
                        }

                        Stack<GraphicalObject> stack = new Stack<>();
                        for (String line : lines) {
                            String id = line.substring(0, line.indexOf(" "));
                            GraphicalObject obj = id_map.get(id);
                            String data = line.substring(line.indexOf(" "), line.length()).trim();
                            obj.load(stack, data);
                        }
                        model.clear();
                        model.addGraphicalObjects(stack);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Unable to open file!");
                    }
                }
            }
        });
        /// Save
        save_button = new JButton("Pohrani");
        save_button.setFocusable(false);
        toolbar.add(save_button);
        save_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser("Save");
                int selected = fileChooser.showSaveDialog(canvas);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    List<String> rows = new ArrayList<>();
                    for (GraphicalObject o : model.list()) {
                        o.save(rows);
                    }
                    try {
                        Path dest = Paths.get(fileName);
                        Files.write(dest, rows);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Unable to save file!");
                    }
                }
            }
        });
        /// SVG export
        JButton svg_button = new JButton("SVG export");
        svg_button.setFocusable(false);
        toolbar.add(svg_button);
        svg_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = new JFileChooser("Export as SVG");
                int selected = fileChooser.showSaveDialog(canvas);
                if (selected == JFileChooser.APPROVE_OPTION) {
                    String fileName = fileChooser.getSelectedFile().getAbsolutePath();
                    SVGRendererImpl r = new SVGRendererImpl(fileName);
                    for (GraphicalObject o : model.list()) {
                        o.render(r);
                    }
                    try {
                        r.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.err.println("Unable to save file!");
                    }
                }
            }
        });
        /// Prototypes
        for (GraphicalObject o : objects) {
            if (o.getShapeName() != null) {
                JButton button = new JButton(o.getShapeName());
                button.setFocusable(false);
                toolbar.add(button);
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent event) {
                        canvas.setCurrentState(new AddShapeState(model, o));
                    }
                });
            }
        }
        /// Select
        JButton select_button = new JButton("Selektiraj");
        select_button.setFocusable(false);
        toolbar.add(select_button);
        select_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                canvas.setCurrentState(new SelectShapeState(model));
            }
        });
        /// Delete
        JButton del_button = new JButton("Briši");
        del_button.setFocusable(false);
        toolbar.add(del_button);
        del_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                canvas.setCurrentState(new EraserState(model));
            }
        });

        add(toolbar, BorderLayout.PAGE_START);

        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void documentChange() {
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_W) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_Q) {
            dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_S) {
            save_button.doClick();
        } else if (event.isControlDown() && event.getKeyCode() == KeyEvent.VK_O) {
            load_button.doClick();
        } else {
            canvas.keyPressed(event);
        }
    }

    @Override
    public void keyReleased(KeyEvent event) {
        canvas.keyReleased(event);
    }

    @Override
    public void keyTyped(KeyEvent event) {
        canvas.keyTyped(event);
    }
}
