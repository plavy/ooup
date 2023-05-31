import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.swing.JFileChooser;

public class LoadListener implements ActionListener {
    Canvas canvas;
    List<GraphicalObject> objects;
    DocumentModel model;

    public LoadListener(Canvas canvas, List<GraphicalObject> objects, DocumentModel model) {
        this.canvas = canvas;
        this.objects = objects;
        this.model = model;
    }

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
}
