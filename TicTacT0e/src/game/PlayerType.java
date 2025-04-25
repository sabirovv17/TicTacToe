package game;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public enum PlayerType {
    HUMAN,
    CHEATER,
    SEQUENTIAL,
    RANDOM;

    private static Map<String, PlayerType> typesByLiteral = new HashMap<>() {{
        put("H", HUMAN);
        put("C", CHEATER);
        put("S", SEQUENTIAL);
        put("R", RANDOM);
    }};

    public static Player createPlayerOfType(PlayerType playerType, Cell sign) {
        switch (playerType) {
            case HUMAN -> {
                return new HumanPlayer(sign, Mode.GUI);
            }
            case CHEATER -> {
                return new CheatingPlayer(sign);
            }
            case SEQUENTIAL -> {
                return new SequentialPlayer(sign);
            }
            case RANDOM -> {
                return new RandomPlayer(sign);
            }
        }
        return null;
    }

    public static Player createPlayerOfType(PlayerType playerType) {
        switch (playerType) {
            case HUMAN -> {
                return new HumanPlayer();
            }
            case CHEATER -> {
                return new CheatingPlayer();
            }
            case SEQUENTIAL -> {
                return new SequentialPlayer();
            }
            case RANDOM -> {
                return new RandomPlayer();
            }
        }
        return null;
    }

    public static Player getPlayerByLiteral(String playerLiteral, Cell sign) {
        return createPlayerOfType(typesByLiteral.get(playerLiteral), sign);
    }

    public static Player getPlayerByLiteral(String playerLiteral) {
        return createPlayerOfType(typesByLiteral.get(playerLiteral));
    }

    public static Set<String> allowedTypesLiterals() {
        return typesByLiteral.keySet();
    }
}
