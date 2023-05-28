import java.util.ArrayList;
import java.util.List;

public abstract class AbstractGraphicalObject implements GraphicalObject {
    private Point[] hotPoints;
    private boolean[] hotPointSelected;
    private boolean selected;
    protected List<GraphicalObjectListener> listeners = new ArrayList<>();

    protected AbstractGraphicalObject(Point[] points) {
        this.hotPoints = points;
        this.hotPointSelected = new boolean[points.length];
        this.selected = false;
    }

    @Override
    public Point getHotPoint(int index) {
        return hotPoints[index];
    }

    @Override
    public void setHotPoint(int index, Point point) {
        hotPoints[index] = point;
    }

    @Override
    public int getNumberOfHotPoints() {
        return hotPoints.length;
    }

    @Override
    public double getHotPointDistance(int index, Point mousePoint) {
        return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
    }

    @Override
    public boolean isHotPointSelected(int index) {
        return hotPointSelected[index];
    }

    @Override
    public void setHotPointSelected(int index, boolean selected) {
        hotPointSelected[index] = selected;
        notifyListeners();
    }

    @Override
    public Rectangle getHotPointBox(int index) {
        int x = getHotPoint(index).getX() - 5;
        int y = getHotPoint(index).getY() - 5;
        int width = 10;
        int height = 10;
        return new Rectangle(x, y, width, height);

    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        notifySelectionListeners();
    }

    @Override
    public void translate(Point delta) {
        for(int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(delta);
        }
        notifyListeners();
    }

    @Override
    public void addGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.add(l);       
    }

    @Override
    public void removeGraphicalObjectListener(GraphicalObjectListener l) {
        listeners.remove(l);
    }

    protected void notifyListeners() {
        for (GraphicalObjectListener l : listeners) {
            l.graphicalObjectChanged(this);
        }
    }

    protected void notifySelectionListeners() {
        for (GraphicalObjectListener l : listeners) {
            l.graphicalObjectSelectionChanged(this);
        }
    }
}
