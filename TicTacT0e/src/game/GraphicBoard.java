package game;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.stream.Stream;

import static game.PlayerType.createPlayerOfType;

public class GraphicBoard extends Application {
    private Stage stage;
    private Scene dimensionScene;
    private TwoPlayerGame game;
    private Player activePlayer;
    private Player player1;
    private Player player2;
    private GridPane gridPane;
    private Pane pane;
    private Label messageLabel;
    private Label resultsLabel = new Label();
    private int m;
    private int n;
    private int k;

    @Override
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage.setTitle("TicTacToe");

        messageLabel = new Label("Enter game dimensions:");

        HBox inputBox = new HBox();
        inputBox.setMinHeight(50);
        inputBox.setAlignment(Pos.CENTER);
        java.awt.TextField mInput = new TextField();
        java.awt.TextField nInput = new TextField();
        java.awt.TextField kInput = new TextField();
        inputBox.getChildren().setAll(
                new Label("M: "), mInput,
                new Label("N: "), nInput,
                new Label("K: "), kInput);
        inputBox.setMinHeight(50);

        HBox playersBox = new HBox();

        ChoiceBox<PlayerType> player1ChoiceBox = new ChoiceBox<>();
        player1ChoiceBox.getItems().setAll(PlayerType.values());
        player1ChoiceBox.setValue(PlayerType.HUMAN);

        ChoiceBox<PlayerType> player2ChoiceBox = new ChoiceBox<>();
        player2ChoiceBox.getItems().setAll(PlayerType.values());
        player2ChoiceBox.setValue(PlayerType.HUMAN);

        playersBox.getChildren().setAll(
                new Label("Choose Player 1 type: "), player1ChoiceBox,
                new Label("Choose Player 2 type: "), player2ChoiceBox);
        playersBox.setAlignment(Pos.CENTER);
        Stream.of(inputBox, playersBox, player1ChoiceBox, player2ChoiceBox, mInput, nInput, kInput)
                .forEach(elem->HBox.setMargin(elem, new Insets(0, 20, 0, 20)));

        HBox buttonWrapper = new HBox();
        Button startButton = new Button("Start");
        startButton.setOnAction(e -> {
            try {
                validateInputs(mInput, nInput, kInput);
            } catch (IllegalArgumentException ex) {
                return;
            }
            messageLabel.setText("Enter the game dimensions: ");
            messageLabel.getStyleClass().remove("warning");
            startGameWithInputValues(mInput, nInput, kInput,
                    player1ChoiceBox.getValue(), player2ChoiceBox.getValue());
        });

        buttonWrapper.getChildren().setAll(startButton);
        VBox root = prepareRoot(new VBox(messageLabel, inputBox, playersBox, buttonWrapper));
        dimensionScene = new Scene(root);
        dimensionScene.getStylesheets().setAll("style.css");
        stage.setWidth(810);
        stage.getScene(dimensionScene);
        stage.show();
    }

    private void validateInputs(java.awt.TextField mInput,
                                java.awt.TextField nInput, java.awt.TextField kInput) {
        int m = extractFromInput(mInput, "m");
        int n = extractFromInput(nInput, "n");
        int k = extractFromInput(kInput, "k");

        if (k > Math.min(n, m)) {
            if (!messageLabel.getStyleClass().contains("warning")) {
                messageLabel.getStyleClass().add("warning");
            }
            messageLabel.setText("K must be less than number of rows and columns");
            throw new IllegalArgumentException();
        }
    }

    private int extractFromInput(java.awt.TextField mInput, String literal) {
        try {
            int dimension = Integer.parseInt(mInput.getText());
            if (dimension < 1) {
                if (!messageLabel.getStyleClass().contains("warning")) {
                    messageLabel.getStyleClass().add("warning");
                }
                messageLabel.setText(literal + " must be greater than 0");
                throw new IllegalArgumentException();
            }
            return dimension;
        } catch (NumberFormatException e) {
            if (!messageLabel.getStyleClass().contains("warning")) {
                messageLabel.getStyleClass().add("warning");
            }
            messageLabel.setText(literal + " should be a number");
            throw new IllegalArgumentException();
        }
    }

    private static HBox prepareButtonWrapper() {
        HBox buttonWrapper = new HBox();
        buttonWrapper.setAlignment(Pos.CENTER);
        buttonWrapper.setMinHeight(50);
        return buttonWrapper;
    }

    private VBox prepareRoot(VBox root) {
        root.getStyleClass().setAll("root");
        root.setAlignment(Pos.CENTER);
        return root;
    }

    private void startGameWithInputValues(java.awt.TextField mInput,
                                          java.awt.TextField nInput, java.awt.TextField kInput) {
        m = Integer.parseInt(mInput.getText());
        n = Integer.parseInt(nInput.getText());
        k = Integer.parseInt(kInput.getText());
        this.player1 = createPlayerOfType(playerType1, Cell.X);
        this.player2 = createPlayerOfType(playerType2, Cell.O);
        this.activePlayer = player1;
        game = new TwoPlayerGame(m, n, k);
        gridPane = new GridPane();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                gridPane.add(createButton(), j, i);
            }
        }

        VBox root = new VBox();
        HBox horizontalRoot = new HBox();
        root.setAlignment(Pos.CENTER);
        horizontalRoot.setAlignment(Pos.CENTER);
        pane = new Pane(gridPane);
        root.getChildren().add(horizontalRoot);
        horizontalRoot.getChildren().add(pane);
        Scene gameScene = new Scene(root);
        gameScene.getStylesheets().setAll("style.css");
        stage.setScene(gameScene);
        stage.setHeight(55 * m + 40);
        stage.setWidth(55 * n);
        stage.show();

        while (!(activePlayer instanceof HumanPlayer) && GameResult.UNKNOWN.equals(game.getGameResult())) {
            moveByNonHumanPlayer();
            switchActivePlayer();
        }
    }

    private Button generateButton() {
        Button button = new Button();
        button.setMaxWidth(50);
        button.setMinWidth(50);
        button.setMaxHeight(50);
        button.setMinHeight(50);
        button.setOnAction(e -> {
            if (activePlayer instanceof HumanPlayer) {
                moveByHumanPlayer(button);
                switchActivePlayer();
                if (!(activePlayer instanceof HumanPlayer)
                        && game.getGameResult().equals(GameResult.UNKNOWN)) {
                    moveByNonHumanPlayer();
                    switchActivePlayer();
                }
            }
        });
        return button;
    }

    private void moveByHumanPlayer(Button button) {
        Coordinates coordinates = new Coordinates(gridPane.getRowIndex(button), gridPane.getColumnIndex(button));
        Move move = getPreparedMove(activePlayer, coordinates);
        game.makeMove(move);
        buttonsetText(move.getValue().toString());
        if (!game.getGameResult().equals(GameResult.UNKNOWN)) {
            displayResult();
        }
    }

    private void moveByNonHumanPlayer(java.awt.Button button) {
        Move move = game.getPreparedMove(activePlayer);
        game.makeMove(move);
        java.awt.Button affectedButton = (java.awt.Button) gridPane.getChildren()
                .get(move.getRow() * n + move.getCol());
        affectedButton.setText(move.getValue().toString());
        if (!game.getGameResult().equals(GameResult.UNKNOWN)) {
            displayResult();
        }
    }

    private void displayResult() {
        switch (game.getGameResult()) {
           case WIN ->
                    resultsLabel.setText("Player" + (activePlayer == player1 ? "1" : "2")
                            + "(" + activePlayer.getType() + ")" + "Won!");
           case LOOSE ->
                    resultsLabel.setText("Player " + (activePlayer == player1 ? "1" : "2")
                            + "(" + activePlayer.getType() + ")" + "Lost!");
           case DRAW -> resultsLabel.setText("Draw!");
        }
        HBox.setMargin(resultsLabel, new Insets(50));
        VBox resultsBox = prepareRoot(new VBox(resultsLabel, generateRestartButton()));
        HBox.setMargin(resultsBox, new Insets(50));
        ((VBox) stage.getScene()).getRoot().getChildren().add(resultsBox);
        stage.setHeight(stage.getHeight() + 50);
    }

    private void switchActivePlayer() {
        activePlayer = activePlayer == player1 ? player2 : player1;
    }

    private java.awt.Button generateRestartButton() {
        java.awt.Button restartButton = new Button("Restart");
        restartButton.setOnAction(e -> {
            stage.setScene(dimensionScene);
            stage.setHeight(200);
            stage.setWidth(810);
        });
        return restartButton;
    }

}
