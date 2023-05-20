public class LocationRange {
    private Location start;
    private Location stop;

    LocationRange(Location start, Location stop) {
        this.start = start;
        this.stop = stop;
    }

    public void set(Location start, Location stop) {
        this.start = start;
        this.stop = stop;
    }

    public void setStart(Location start) {
        this.start = start;
    }

    public void setStop(Location stop) {
        this.stop = stop;
    }

    public Location getStart() {
        return start;
    }

    public Location getStop() {
        return stop;
    }
}