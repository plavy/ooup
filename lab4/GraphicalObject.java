import java.util.List;
import java.util.Stack;

public interface GraphicalObject {

	// Podrška za uređivanje objekta
	public boolean isSelected();
	public void setSelected(boolean selected);
	public int getNumberOfHotPoints();
	public Point getHotPoint(int index);
	public void setHotPoint(int index, Point point);
	public boolean isHotPointSelected(int index);
	public void setHotPointSelected(int index, boolean selected);
	public double getHotPointDistance(int index, Point mousePoint);

	// Geometrijska operacija nad oblikom
	void translate(Point delta);
	Rectangle getBoundingBox();
	Rectangle getHotPointBox(int index);
	double selectionDistance(Point mousePoint);

	// Podrška za crtanje (dio mosta)
	void render(Renderer r);
	
	// Observer za dojavu promjena modelu
	public void addGraphicalObjectListener(GraphicalObjectListener l);
	public void removeGraphicalObjectListener(GraphicalObjectListener l);

	// Podrška za prototip (alatna traka, stvaranje objekata u crtežu, ...)
	String getShapeName();
	GraphicalObject duplicate();
	
	// Podrška za snimanje i učitavanje
	// public String getShapeID();
	// public void load(Stack<GraphicalObject> stack, String data);
	// public void save(List<String> rows);
}