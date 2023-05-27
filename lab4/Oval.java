public class Oval extends AbstractGraphicalObject {
    // private Point center;

    public Oval() {
        super(new Point[] {
            new Point(10, 0),
            new Point(0, 10),
        });
    }

    public Oval(Point right, Point bottom) {
        super(new Point[] {
            right,
            bottom,
        });
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        int centerX = getHotPoint(1).getX();
        int centerY = getHotPoint(0).getY();
        int radiusX = getHotPoint(0).getX() - getHotPoint(1).getY();
        int radiusY = getHotPoint(0).getY() - getHotPoint(1).getY();

        // Normalized distance to center
        int deltaX = (mousePoint.getX() - centerX) / radiusX;
        int deltaY = (mousePoint.getY() - centerY) / radiusY;

        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    @Override
    public Rectangle getBoundingBox() {
        int x = getHotPoint(1).getX() * 2 - getHotPoint(0).getX();
        int y = getHotPoint(1).getY() * 2 - getHotPoint(0).getY();
        int width = (getHotPoint(0).getX() - getHotPoint(1).getX()) * 2;
        int height = (getHotPoint(0).getY() - getHotPoint(1).getY()) * 2;
        return new Rectangle(x, y, width, height);
    }

    @Override
    public GraphicalObject duplicate() {
        Oval duplicated = new Oval(getHotPoint(0), getHotPoint(1));
        return duplicated;
    }

    @Override
    public String getShapeName() {
        return "Oval";
    }

    @Override
    public void render(Renderer r) {
        Point[] points = new Point[4];
        points[0] = new Point(getHotPoint(0).getX(), getHotPoint(0).getY());
        points[1] = new Point(getHotPoint(1).getX(), getHotPoint(1).getY());
        points[2] = new Point(getHotPoint(1).getX() * 2 - getHotPoint(0).getX(), getHotPoint(0).getY());
        points[3] = new Point(getHotPoint(1).getX(), getHotPoint(0).getY() * 2 - getHotPoint(1).getY());
        r.fillPolygon(points);
    }
}
