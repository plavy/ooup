import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class GUI extends JFrame {
    private DocumentModel model;
    private Canvas canvas;
    private State currentState = new IdleState();

    public GUI(List<GraphicalObject> objects) {
        // Window
        super("Vector Draw");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(800, 600));
        setLayout(new BorderLayout());
        pack();
        setLocationRelativeTo(null);

        // Toolbar
        JToolBar toolbar = new JToolBar();
        for (GraphicalObject o : objects) {
            JButton button = new JButton(o.getShapeName());
            toolbar.add(button);
        }
        add(toolbar, BorderLayout.PAGE_START);

        // Model
        this.model = new DocumentModel();

        // Canvas
        this.canvas = new Canvas(model, currentState);
        this.canvas.setFocusable(true);
        add(this.canvas, BorderLayout.CENTER);
    }
}
