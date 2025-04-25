package game;

import java.util.Optional;
import java.util.stream.Stream;

public class Tournament {
    private int targetScore;
    private Statistics player1Stats;
    private Statistics player2Stats;

    public Tournament(int targetScore, Player player1, Player player2) {
        this.targetScore = targetScore;
        this.player1Stats = new Statistics(player1);
        this.player2Stats = new Statistics(player2);
    }

    public void incrementScore(Player winner) {
        Stream.of(player1Stats, player2Stats)
                .filter(stats -> stats.getPlayer() == winner)
                .forEach(Statistics::upscore);
    }

    public void incrementScoreToEnemy(Player loser) {
        Stream.of(player1Stats, player2Stats)
                .filter(stats -> stats.getPlayer() != loser)
                .forEach(Statistics::upscore);
    }

    public boolean isFinished() {
        return getWinner().isPresent();
    }

    public Optional<Player> getWinner() {
        int currentScorePlayer1 = player1Stats.getScore();
        int currentScorePlayer2 = player2Stats.getScore();
        if (currentScorePlayer1 >= targetScore) {
            return Optional.of(player1Stats.getPlayer());
        } else if (currentScorePlayer2 >= targetScore) {
            return Optional.of(player2Stats.getPlayer());
        }
        return Optional.empty();
//        return Stream.of(player1Statistics, player2Statistics).anyMatch(stats -> stats.getScore() >= targetScore);
    }

    public String getTotalScore() {
        return player1Stats.getScore() + " : " + player2Stats.getScore();
    }

    private static class Statistics {
        private Player player;
        private int score;

        public Statistics(Player player) {
            this.player = player;
            this.score = 0;
        }

        public Player getPlayer() {
            return player;
        }

        public int getScore() {
            return score;
        }

        public void upscore() {
            this.score++;
        }
    }
}
