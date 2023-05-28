import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;

public class Canvas extends JComponent implements KeyListener, MouseListener, MouseMotionListener {
    private DocumentModel model;
    private State currentState;

    public Canvas(DocumentModel model) {
        this.model = model;
        this.currentState = new IdleState();
        addKeyListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Renderer r = new G2DRendererImpl(g2d);
        for (GraphicalObject o : model.list()) {
            o.render(r);
            currentState.afterDraw(r, o);
        }
        currentState.afterDraw(r);
    }

    public void setCurrentState(State state) {
        currentState.onLeaving();
        currentState = state;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        currentState.mouseDown(new Point(event.getX(), event.getY()), event.isShiftDown(), event.isControlDown());
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        currentState.mouseUp(new Point(event.getX(), event.getY()), event.isShiftDown(), event.isControlDown());
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        currentState.mouseDragged(new Point(event.getX(), event.getY()));
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent event) {
        // Change to Idle state
        if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
            setCurrentState(new IdleState());
        }
        currentState.keyPressed(event.getKeyCode());
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent event) {
    }

    @Override
    public void keyTyped(KeyEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent event) {
    }

    @Override
    public void mouseExited(MouseEvent event) {
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mouseMoved(MouseEvent event) {
    }
}
