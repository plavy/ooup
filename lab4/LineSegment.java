import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject {
    
    public LineSegment() {
        super(new Point[] {
            new Point(0, 0),
            new Point(50, 30),
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
        int y = Math.min(getHotPoint(0).getY(), getHotPoint(1).getY());
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

    @Override
    public String getShapeID() {
        return "@LINE";
    }

    @Override
    public void save(List<String> rows) {
        StringBuilder builder = new StringBuilder(getShapeID());
        builder.append(" " + getHotPoint(0).getX());
        builder.append(" " + getHotPoint(0).getY());
        builder.append(" " + getHotPoint(1).getX());
        builder.append(" " + getHotPoint(1).getY());
        rows.add(builder.toString());
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        String[] data_list = data.split(" ");
        int x0 = Integer.valueOf(data_list[0]);
        int y0 = Integer.valueOf(data_list[1]);
        int x1 = Integer.valueOf(data_list[2]);
        int y1 = Integer.valueOf(data_list[3]);
        stack.push(new LineSegment(new Point(x0, y0), new Point(x1, y1)));
    }
}