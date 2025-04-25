package game;

public class HumanPlayer implements Player {
    private Coordinates coordinates;
    private Mode mode;

    public HumanPlayer(Cell sign) {
        this(sign, Mode.CMD);
    }

    public HumanPlayer(Cell sign, Mode mode) {
        this.sign = sign;
        this.mode = mode;
    }

    public HumanPlayer() {}

    private Cell sign;

    @Override
    public Move prepareMove(Position position, Cell activePlayerSign) {
       if(Mode.CMD.equals(mode)) {
           System.out.println();
           System.out.println("Current Position:");
           System.out.println(position);
           System.out.println("Enter your move for " + activePlayerSign);
       }
       return new Move(coordinates.getX(), coordinates.getY(), activePlayerSign);
    }

    @Override
    public Cell getSign() {
        return sign;
    }

    public void setSign(Cell sign) {
        this.sign = sign;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.HUMAN;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String toString() {
        return "Human Player";
    }
}
