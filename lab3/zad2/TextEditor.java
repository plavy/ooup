import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
 
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
 
/**
 * This program demonstrates how to draw lines using Graphics2D object.
 * @author www.codejava.net
 *
 */
public class TextEditor extends JFrame {
 
    // public TextEditor() {
    //     super("Text Editor");
 
    //     setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //     setLocationRelativeTo(null);
    //     repaint();
    // }
    
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.RED);
        g.drawLine(0, getHeight()/2, getWidth(), getHeight()/2);
        g.drawLine(getWidth()/2, 0, getWidth()/2, getHeight());
        g.setColor(Color.black);
        g.setFont(new Font("Courier", Font.PLAIN, 12));
        g.drawString("lala!", 0, 50);
    }
 
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame editor = new TextEditor();
                editor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                editor.setTitle("Text Editor");
                editor.setPreferredSize(new Dimension(800, 600));
                editor.pack();
                editor.setLocationRelativeTo(null);
                editor.setVisible(true);
            }
        });
    }
}