import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class DocumentModel {

    public final static double SELECTION_PROXIMITY = 10;

    // Kolekcija svih grafičkih objekata:
    private List<GraphicalObject> objects = new ArrayList<>();
    // Read-Only proxy oko kolekcije grafičkih objekata:
    private List<GraphicalObject> roObjects = Collections.unmodifiableList(objects);
    // Kolekcija prijavljenih promatrača:
    private List<DocumentModelListener> listeners = new ArrayList<>();
    // Kolekcija selektiranih objekata:
    private List<GraphicalObject> selectedObjects = new ArrayList<>();
    // Read-Only proxy oko kolekcije selektiranih objekata:
    private List<GraphicalObject> roSelectedObjects = Collections.unmodifiableList(selectedObjects);

    // Promatrač koji će biti registriran nad svim objektima crteža...
    private final GraphicalObjectListener goListener = new GraphicalObjectListener() {
        public void graphicalObjectChanged(GraphicalObject go) {
            notifyListeners();
        }

        public void graphicalObjectSelectionChanged(GraphicalObject go) {
            if (go.isSelected()) {
                selectedObjects.add(go);
            } else {
                selectedObjects.remove(go);
            }
            notifyListeners();
        }
    };

    public DocumentModel() {
    }

    // Brisanje svih objekata iz modela (pazite da se sve potrebno odregistrira)
    // i potom obavijeste svi promatrači modela
    public void clear() {
        while (objects.size() > 0) {
            GraphicalObject pop = objects.remove(0);
            pop.removeGraphicalObjectListener(goListener);
        }
        notifyListeners();
    }

    // Dodavanje objekta u dokument (pazite je li već selektiran; registrirajte
    // model kao promatrača)
    public void addGraphicalObject(GraphicalObject obj) {
        objects.add(obj);
        obj.addGraphicalObjectListener(goListener);
        if (obj.isSelected()) {
            obj.setSelected(true);
        }
        notifyListeners();
    }

    // Dodavanje objekta na poziciju
    public void addGraphicalObject(int index, GraphicalObject obj) {
        objects.add(index, obj);
        obj.addGraphicalObjectListener(goListener);
        if (obj.isSelected()) {
            obj.setSelected(true);
        }
        notifyListeners();
    }

    public void addGraphicalObjects(Collection<GraphicalObject> coll) {
        for (GraphicalObject o : coll) {
            addGraphicalObject(o);
        }
    }

    // Uklanjanje objekta iz dokumenta (pazite je li već selektiran; odregistrirajte
    // model kao promatrača)
    public void removeGraphicalObject(GraphicalObject obj) {
        if (obj.isSelected()) {
            obj.setSelected(false);
        }
        obj.removeGraphicalObjectListener(goListener);
        objects.remove(obj);
        notifyListeners();
    }

    // Vrati nepromjenjivu listu postojećih objekata (izmjene smiju ići samo kroz
    // metode modela)
    public List<GraphicalObject> list() {
        return roObjects;
    }

    // Prijava...
    public void addDocumentModelListener(DocumentModelListener l) {
        listeners.add(l);
    }

    // Odjava...
    public void removeDocumentModelListener(DocumentModelListener l) {
        listeners.remove(l);
    }

    // Obavještavanje...
    public void notifyListeners() {
        for (DocumentModelListener l : listeners) {
            l.documentChange();
        }
    }

    // Vrati nepromjenjivu listu selektiranih objekata
    public List<GraphicalObject> getSelectedObjects() {
        return roSelectedObjects;
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto kasnije...
    // Time će se on iscrtati kasnije (pa će time možda veći dio biti vidljiv)
    public void increaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index >= 0 && index < objects.size() - 1) {
            Collections.swap(objects, index, index + 1);
        }
        notifyListeners();
    }

    // Pomakni predani objekt u listi objekata na jedno mjesto ranije...
    public void decreaseZ(GraphicalObject go) {
        int index = objects.indexOf(go);
        if (index > 0) {
            Collections.swap(objects, index, index - 1);
        }
        notifyListeners();
    }

    // Pronađi postoji li u modelu neki objekt koji klik na točku koja je
    // predana kao argument selektira i vrati ga ili vrati null. Točka selektira
    // objekt kojemu je najbliža uz uvjet da ta udaljenost nije veća od
    // SELECTION_PROXIMITY. Status selektiranosti objekta ova metoda NE dira.
    public GraphicalObject findSelectedGraphicalObject(Point mousePoint) {
        GraphicalObject objectWithMin = null;
        double minDistance = SELECTION_PROXIMITY;
        for (GraphicalObject go : objects) {
            double distance = go.selectionDistance(mousePoint);
            if (distance <= minDistance) {
                objectWithMin = go;
                minDistance = distance;
            }
        }
        return objectWithMin;
    }

    // Pronađi da li u predanom objektu predana točka miša selektira neki hot-point.
    // Točka miša selektira onaj hot-point objekta kojemu je najbliža uz uvjet da ta
    // udaljenost nije veća od SELECTION_PROXIMITY. Vraća se indeks hot-pointa
    // kojeg bi predana točka selektirala ili -1 ako takve nema. Status selekcije
    // se pri tome NE dira.
    public int findSelectedHotPoint(GraphicalObject object, Point mousePoint) {
        int indexWithMin = -1;
        double minDistance = SELECTION_PROXIMITY;
        for (int i = 0; i < object.getNumberOfHotPoints(); i++) {
            double distance = object.getHotPointDistance(i, mousePoint);
            if (distance <= minDistance) {
                indexWithMin = i;
                minDistance = distance;
            }
        }
        return indexWithMin;
    }
}