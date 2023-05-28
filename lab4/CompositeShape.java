import java.util.List;

public class CompositeShape extends AbstractGraphicalObject {
    List<GraphicalObject> objects;

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
}
