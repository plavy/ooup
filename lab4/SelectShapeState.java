import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectShapeState implements State {
    private DocumentModel model;
    private GraphicalObject selectedAlone = null;

    public SelectShapeState(DocumentModel model) {
        this.model = model;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (!ctrlDown) {
            for (GraphicalObject o : new ArrayList<>(model.getSelectedObjects())) {
                o.setSelected(false);
            }
        }
        GraphicalObject found = model.findSelectedGraphicalObject(mousePoint);
        if (found != null) {
            if (!ctrlDown) {
                found.setSelected(true);
            } else {
                found.setSelected(!found.isSelected());
            }
        }
        if (model.getSelectedObjects().size() == 1) {
            GraphicalObject selected = model.getSelectedObjects().get(0);
            selectedAlone = selected;
            int i = model.findSelectedHotPoint(selected, mousePoint);
            if (i >= 0) {
                selected.setHotPointSelected(i, true);
            }
        } else {
            selectedAlone = null;
        }
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        if (model.getSelectedObjects().size() == 1) {
            GraphicalObject selected = model.getSelectedObjects().get(0);
            int n = selected.getNumberOfHotPoints();
            for (int i = 0; i < n; i++) {
                selected.setHotPointSelected(i, false);
            }
        }
    }

    @Override
    public void mouseDragged(Point mousePoint) {
        if (model.getSelectedObjects().size() == 1) {
            GraphicalObject selected = model.getSelectedObjects().get(0);
            int n = selected.getNumberOfHotPoints();
            int i;
            for (i = 0; i < n; i++) {
                if (selected.isHotPointSelected(i)) {
                    break;
                }
            }
            if (i < n) {
                Point hp = selected.getHotPoint(i);
                selected.setHotPoint(i, hp.translate(mousePoint.difference(hp)));
            }
        }
    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyEvent.VK_ADD) {
            for (GraphicalObject o : model.getSelectedObjects()) {
                model.increaseZ(o);
            }
        } else if (keyCode == KeyEvent.VK_SUBTRACT) {
            for (GraphicalObject o : model.getSelectedObjects()) {
                model.decreaseZ(o);
            }
        } else if (keyCode == KeyEvent.VK_UP) {
            for (GraphicalObject o : model.getSelectedObjects()) {
                o.translate(new Point(0, -1));
            }
        } else if (keyCode == KeyEvent.VK_DOWN) {
            for (GraphicalObject o : model.getSelectedObjects()) {
                o.translate(new Point(0, 1));
            }
        } else if (keyCode == KeyEvent.VK_LEFT) {
            for (GraphicalObject o : model.getSelectedObjects()) {
                o.translate(new Point(-1, 0));
            }
        } else if (keyCode == KeyEvent.VK_RIGHT) {
            for (GraphicalObject o : model.getSelectedObjects()) {
                o.translate(new Point(1, 0));
            }
        } else if (keyCode == KeyEvent.VK_G) {
            List<GraphicalObject> selected = new ArrayList<>(model.getSelectedObjects());
            if (selected.size() > 1) {
                // Sort by z axis
                Collections.sort(selected, (a, b) -> Integer.compare(model.list().indexOf(a), model.list().indexOf(b)));
                CompositeShape composite = new CompositeShape(selected);
                int index = model.list().indexOf(selected.get(0));
                composite.setSelected(true);
                model.addGraphicalObject(index, composite);
                for (GraphicalObject o : selected) {
                    model.removeGraphicalObject(o);
                }
            }
        } else if (keyCode == KeyEvent.VK_U) {
            List<GraphicalObject> selected = new ArrayList<>(model.getSelectedObjects());
            if (selected.size() == 1 && selected.get(0) instanceof CompositeShape) {
                CompositeShape composite = (CompositeShape) selected.get(0);
                int index = model.list().indexOf(composite);
                List<GraphicalObject> objects = new ArrayList<>(composite.getObjects());
                model.removeGraphicalObject(composite);
                for (int i = 0; i < objects.size(); i++) {
                    objects.get(i).setSelected(true);
                    model.addGraphicalObject(index + i, objects.get(i));
                }
            }
        }
    }

    @Override
    public void onLeaving() {
        for (GraphicalObject o : new ArrayList<>(model.getSelectedObjects())) {
            o.setSelected(false);
        }
        selectedAlone = null;
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
        if (go.isSelected()) {
            Rectangle boundingBox = go.getBoundingBox();
            Point[] points = GeometryUtil.rectangleToPoints(boundingBox);
            r.drawLine(points[0], points[1]);
            r.drawLine(points[1], points[2]);
            r.drawLine(points[2], points[3]);
            r.drawLine(points[3], points[0]);
            if (go == selectedAlone) {
                for (int i = 0; i < go.getNumberOfHotPoints(); i++) {
                    Rectangle hotPointBox = go.getHotPointBox(i);
                    points = GeometryUtil.rectangleToPoints(hotPointBox);
                    r.drawLine(points[0], points[1]);
                    r.drawLine(points[1], points[2]);
                    r.drawLine(points[2], points[3]);
                    r.drawLine(points[3], points[0]);
                }
            }
        }
    }

    @Override
    public void afterDraw(Renderer r) {
    }
}
