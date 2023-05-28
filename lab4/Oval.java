public class Oval extends AbstractGraphicalObject {

    public Oval() {
        super(new Point[] {
            new Point(50, 0),
            new Point(0, 30),
        });
    }

    public Oval(Point right, Point bottom) {
        super(new Point[] {
            right,
            bottom,
        });
    }

    private Point getCenter() {
        return new Point(getHotPoint(1).getX(), getHotPoint(0).getY());
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        int centerX = getCenter().getX();
        int centerY = getCenter().getY();
        double radiusX = getHotPoint(0).getX() - getHotPoint(1).getX();
        double radiusY = getHotPoint(1).getY() - getHotPoint(0).getY();

        // Normalized distance to center
        double deltaX = (mousePoint.getX() - centerX) / radiusX;
        double deltaY = (mousePoint.getY() - centerY) / radiusY;

        double distance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
        // TODO: Fix
        distance = distance - 1;
        if (distance < 0) {
            return 0;
        }
        return distance * radiusY;
    }

    @Override
    public Rectangle getBoundingBox() {
        int x = getHotPoint(1).getX() * 2 - getHotPoint(0).getX();
        int y = getHotPoint(0).getY() * 2 - getHotPoint(1).getY();
        int width = (getHotPoint(0).getX() - getHotPoint(1).getX()) * 2;
        int height = (getHotPoint(1).getY() - getHotPoint(0).getY()) * 2;
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
        int NUMBER_OF_POINTS = 200;
        NUMBER_OF_POINTS = NUMBER_OF_POINTS / 2 * 2; // ensure even number
        Point[] points = new Point[NUMBER_OF_POINTS];
        int p = getCenter().getX();
        int q = getCenter().getY();
        int a = getHotPoint(0).getX() - getHotPoint(1).getX();
        int b = getHotPoint(1).getY() - getHotPoint(0).getY();
        for (int i = 0; i < NUMBER_OF_POINTS / 2; i++){
            double x = getHotPoint(1).getX() * 2 - getHotPoint(0).getX() + i * (getHotPoint(0).getX() - getHotPoint(1).getX()) / (NUMBER_OF_POINTS / 2 - 1) * 2;
            double tmp = Math.sqrt((1 - (Math.pow((x - p), 2) / Math.pow(a, 2))) * Math.pow(b, 2));
            double y1 = tmp + q;
            double y2 = - tmp + q;
            points[i] = new Point((int)x, (int)y1);
            points[NUMBER_OF_POINTS - i - 1] = new Point((int)x, (int)y2);
        }
        r.fillPolygon(points);
    }
}
