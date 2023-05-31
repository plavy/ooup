import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class SVGRendererImpl implements Renderer {

    private List<String> lines = new ArrayList<>();
    private String fileName;

    public SVGRendererImpl(String fileName) {
        // zapamti fileName; u lines dodaj zaglavlje SVG dokumenta:
        // <svg xmlns=... >
        this.fileName = fileName;
        lines.add("<svg xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
        // add white background
        lines.add("<rect width='100%' height='100%' fill='white'/>");
    }

    public void close() throws IOException {
        // u lines još dodaj završni tag SVG dokumenta: </svg>
        // sve retke u listi lines zapiši na disk u datoteku
        lines.add("</svg>");
        Path dest = Paths.get(fileName);
        Files.write(dest, lines);
    }

    @Override
    public void drawLine(Point s, Point e) {
        // Dodaj u lines redak koji definira linijski segment:
        // <line ... />
        lines.add("<line x1='" + s.getX() + "'  y1='" + s.getY() + "' x2='" + e.getX() + "'   y2='" + e.getY()
                + "' style=\"stroke:#0000FF;\"/>");
    }

    @Override
    public void fillPolygon(Point[] points) {
        // Dodaj u lines redak koji definira popunjeni poligon:
        // <polygon points="..." style="stroke: ...; fill: ...;" />
        StringBuilder pointsList = new StringBuilder();
        for (Point p : points) {
            pointsList.append(p.getX() + "," + p.getY() + " ");
        }
        lines.add("<polygon points=\"" + pointsList.toString() + "\" style=\"stroke:#FF0000; fill:#0000FF;\"/>");
    }

}