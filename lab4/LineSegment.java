public class LineSegment extends AbstractGraphicalObject {
    
    public LineSegment() {
        super(new Point[] {
            new Point(0, 0),
            new Point(10, 0),
        });
    }

    public LineSegment(Point s, Point e) {
        super(new Point[] {
            s,
            e,
        });
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(getHotPoint(0), getHotPoint(1), mousePoint);
    }

    @Override
    public Rectangle getBoundingBox() {
        int x = Math.min(getHotPoint(0).getX(), getHotPoint(1).getX());
        int y = Math.max(getHotPoint(0).getY(), getHotPoint(1).getY());
        int width = Math.abs(getHotPoint(0).getX() - getHotPoint(1).getX());
        int height = Math.abs(getHotPoint(0).getY() - getHotPoint(1).getY());
        return new Rectangle(x, y, width, height);
    }

    @Override
    public GraphicalObject duplicate() {
        LineSegment duplicated = new LineSegment(getHotPoint(0), getHotPoint(1));
        return duplicated;
    }

    @Override
    public String getShapeName() {
        return "Linija";
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(getHotPoint(0), getHotPoint(1));
    }

}