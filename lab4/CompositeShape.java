import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

public class CompositeShape extends AbstractGraphicalObject {
    List<GraphicalObject> objects;

    public CompositeShape() {
        super(new Point[] {});
        this.objects = new ArrayList<>();
    }

    public CompositeShape(List<GraphicalObject> objects) {
        super(new Point[] {});
        this.objects = objects;

    }

    public List<GraphicalObject> getObjects() {
        return objects;
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        Double minDistance = null;
        for (GraphicalObject o : objects) {
            double distance = o.selectionDistance(mousePoint);
            if (minDistance == null || distance < minDistance) {
                minDistance = distance;
            }
        }
        return minDistance;
    }

    @Override
    public Rectangle getBoundingBox() {
        Integer minX = null;
        Integer maxX = null;
        Integer minY = null;
        Integer maxY = null;
        for (GraphicalObject o : objects) {
            int lowX = o.getBoundingBox().getX();
            int highX = o.getBoundingBox().getX() + o.getBoundingBox().getWidth();
            int lowY = o.getBoundingBox().getY();
            int highY = o.getBoundingBox().getY() + o.getBoundingBox().getHeight();
            if (minX == null || lowX < minX) {
                minX = lowX;
            }
            if (maxX == null || highX > maxX) {
                maxX = highX;
            }
            if (minY == null || lowY < minY) {
                minY = lowY;
            }
            if (maxY == null || highY > maxY) {
                maxY = highY;
            }
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);

    }

    @Override
    public GraphicalObject duplicate() {
        return null;
    }

    @Override
    public String getShapeName() {
        return null;
    }

    @Override
    public void render(Renderer r) {
        for (GraphicalObject o : objects) {
            o.render(r);
        }
    }

    @Override
    public void translate(Point delta) {
        for(GraphicalObject o : objects) {
            o.translate(delta);
        }
    }

    @Override
    public String getShapeID() {
        return "@COMP";
    }

    @Override
    public void save(List<String> rows) {
        for (GraphicalObject o : objects) {
            o.save(rows);
        }
        StringBuilder builder = new StringBuilder(getShapeID());
        builder.append(" " + objects.size());
        rows.add(builder.toString());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        int n = Integer.valueOf(data);
        List<GraphicalObject> objects = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            objects.add(stack.pop());
        }
        Collections.reverse(objects);
        stack.push(new CompositeShape(objects));
    }
}
