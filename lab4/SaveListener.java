import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class SaveListener implements ActionListener {
    Canvas canvas;
    DocumentModel model;

    public SaveListener(Canvas canvas, DocumentModel model) {
        this.canvas = canvas;
        this.model = model;
    }

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
}
