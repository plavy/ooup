package mytexteditor;

public class Location {
    private int row = 0;
    private int column = 0;

    public Location(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Location(Location location) {
        this.row = location.row;
        this.column = location.column;
    }

    public void setRow(int row) {
        this.row = row;
    }
    
    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isLowerThan(Location another) {
        if (this.row < another.row) {
            return true;
        } else if (this.row > another.row){
            return false;
        } else {
            return this.column < another.column;
        }
    }
}