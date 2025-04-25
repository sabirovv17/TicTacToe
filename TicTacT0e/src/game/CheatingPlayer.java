package game;

public class CheatingPlayer implements Player {
    private Cell sign;

    public CheatingPlayer(Cell sign) {
        this.sign = sign;
    }

    public CheatingPlayer() {}

    @Override
    public Cell getSign() {
        return sign;
    }

    @Override
    public void setSign(Cell sign) {
        this.sign = sign;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.CHEATER;
    }

    @Override
    public String toString() {
        return "Cheating Player";
    }

    @Override
    public Move prepareMove(Position position, Cell activePlayerSign) {
        final TicTacToeBoard board = (TicTacToeBoard) position;
        Coordinates fieldDimensions = position.getMaxValues();
        Move first = null;
        for (int r = 0; r < fieldDimensions.getX(); r++) {
            for (int c = 0; c < fieldDimensions.getY(); c++) {
                final Move move = new Move(r, c, sign);
                if (position.isValid(move)) {
                    if (first == null) {
                        first = move;
                    } else {
                        board.makeMove(move);
                        }
                    }
                }
            }
        return first;
        }
    }
