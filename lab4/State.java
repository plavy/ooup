public interface State {
	// poziva se kad progam registrira da je pritisnuta lijeva tipka miša
	public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown);
	// poziva se kad progam registrira da je otpuštena lijeva tipka miša
	public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown);
	// poziva se kad progam registrira da korisnik pomiče miš dok je tipka pritisnuta
	public void mouseDragged(Point mousePoint);
	// poziva se kad progam registrira da je korisnik pritisnuo tipku na tipkovnici
	public void keyPressed(int keyCode);

	// Poziva se nakon što je platno nacrtalo grafički objekt predan kao argument
	public void afterDraw(Renderer r, GraphicalObject go);
	// Poziva se nakon što je platno nacrtalo čitav crtež
	public void afterDraw(Renderer r);

	// Poziva se kada program napušta ovo stanje kako bi prešlo u neko drugo
	public void onLeaving();
}
