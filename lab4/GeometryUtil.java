public class GeometryUtil {

    public static double distanceFromPoint(Point point1, Point point2) {
        int deltaX = point1.getX() - point2.getX();
        int deltaY = point1.getY() - point2.getY();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    public static double distanceFromLineSegment(Point s, Point e, Point p) {
        double segmentLengthSquared = Math.pow(e.getX() - s.getX(), 2) + Math.pow(e.getY() - s.getY(), 2);

        // Calculate the parameter along the line segment where the projection of p
        // falls
        double t = ((p.getX() - s.getX()) * (e.getX() - s.getX()) + (p.getY() - s.getY()) * (e.getY() - s.getY()))
                / segmentLengthSquared;

        if (t < 0) {
            // The projection of p falls before the start point of the segment
            return distanceFromPoint(p, s);
        }

        if (t > 1) {
            // The projection of p falls after the end point of the segment
            return distanceFromPoint(p, e);
        }

        // Calculate the closest point on the line segment to p
        int closestX = (int) (s.getX() + t * (e.getX() - s.getX()));
        int closestY = (int) (s.getY() + t * (e.getY() - s.getY()));

        // Calculate the distance between p and the closest point on the line segment
        return distanceFromPoint(p, new Point(closestX, closestY));
    }
}