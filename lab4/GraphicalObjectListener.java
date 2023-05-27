public interface GraphicalObjectListener {

	// Poziva se kad se nad objektom promjeni bio što...
	public void graphicalObjectChanged(GraphicalObject go);
	// Poziva se isključivo ako je nad objektom promjenjen status selektiranosti
	// (baš objekta, ne njegovih hot-point-a).
	public void graphicalObjectSelectionChanged(GraphicalObject go);
	
}