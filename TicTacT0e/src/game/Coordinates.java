package game;

public class Coordinates {
    private final int rowIndex;
    private final int columnIndex;

    public Coordinates(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public int getX() {
        return rowIndex;
    }

    public int getY() {
        return columnIndex;
    }
}
