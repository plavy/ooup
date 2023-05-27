import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JComponent;

public class Canvas extends JComponent implements KeyListener, MouseListener{
    private DocumentModel model;
    private State currentState;

    public Canvas(DocumentModel model, State state) {
        this.model = model;
        this.currentState = state;
        addKeyListener(this);
        setFocusable(true);
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);
        for (GraphicalObject o : model.list())
            o.render(r);
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
        System.out.println("Key pressed");
        
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        System.out.println("Key released");
        
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        System.out.println("Mouse pressed" + isFocusable());
        
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
        
        
    }

    @Override
    public void mouseEntered(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void mouseClicked(MouseEvent arg0) {
        // TODO Auto-generated method stub
        
    }
}
