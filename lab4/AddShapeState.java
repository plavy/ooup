public class AddShapeState implements State {
	
	private DocumentModel model;
	private GraphicalObject prototype;
	
	public AddShapeState(DocumentModel model, GraphicalObject prototype) {
		this.model = model;
        this.prototype = prototype;
	}

	@Override
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
		// dupliciraj zapamćeni prototip, pomakni ga na poziciju miša i dodaj u model
        GraphicalObject obj = prototype.duplicate();
        obj.translate(mousePoint);
        model.addGraphicalObject(obj);
	}

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
    }

    @Override
    public void mouseDragged(Point mousePoint) {
    }

    @Override
    public void keyPressed(int keyCode) {
    }

    @Override
    public void onLeaving() {
    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {
    }

    @Override
    public void afterDraw(Renderer r) {
    }
}
