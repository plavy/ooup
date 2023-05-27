import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<GraphicalObject> objects = new ArrayList<>();
    
        objects.add(new LineSegment());
        objects.add(new Oval());
    
        GUI gui = new GUI(objects);
        gui.setVisible(true);
    }
}
