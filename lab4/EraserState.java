import java.util.ArrayList;
import java.util.List;

public class EraserState implements State {
    DocumentModel model;
    List<Point> points;

    public EraserState(DocumentModel model) {
        this.model = model;
        this.points = new ArrayList<>();
    }

    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
    }

    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        for (Point p : points) {
            GraphicalObject o = model.findSelectedGraphicalObject(p);
            if (o != null) {
                model.removeGraphicalObject(o);
            }
        }
        points.clear();
    }

    public void mouseDragged(Point mousePoint) {
        points.add(mousePoint);
    }

    public void keyPressed(int keyCode) {
    }

    public void afterDraw(Renderer r, GraphicalObject go) {
    }

    public void afterDraw(Renderer r) {
        for (int i = 0; i < points.size() - 1; i++) {
            r.drawLine(points.get(i), points.get(i + 1));
        }
    }

    public void onLeaving() {
        points.clear();
    }
}
