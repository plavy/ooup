import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JToolBar;

public class GUI extends JFrame implements DocumentModelListener {
    private DocumentModel model;
    private Canvas canvas;

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
        addKeyListener(canvas);
        
        // Toolbar
        JToolBar toolbar = new JToolBar();
        /// Prototypes
        for (GraphicalObject o : objects) {
            JButton button = new JButton(o.getShapeName());
            toolbar.add(button);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent event) {
                    canvas.setCurrentState(new AddShapeState(model, o));
                }
            });
            button.addKeyListener(canvas);
        }
        /// Select
        JButton button = new JButton("Selektiraj");
        toolbar.add(button);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                canvas.setCurrentState(new SelectShapeState(model));
            }
        });
        button.addKeyListener(canvas);

        add(toolbar, BorderLayout.PAGE_START);      
        
        pack();
        setLocationRelativeTo(null);
    }

    @Override
    public void documentChange() {
        repaint();
    }
}
