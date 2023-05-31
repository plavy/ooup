import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFileChooser;

public class SVGExportListener implements ActionListener {
    Canvas canvas;
    DocumentModel model;

    public SVGExportListener(Canvas canvas, DocumentModel model) {
        this.canvas = canvas;
        this.model = model;
    }

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
}
