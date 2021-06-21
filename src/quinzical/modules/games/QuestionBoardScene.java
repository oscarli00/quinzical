package quinzical.modules.games;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import quinzical.GameController;
import quinzical.data.Category;
import quinzical.data.QuestionController;

public class QuestionBoardScene {
	private GridPane _layout;
	private GameController _gController;
	private QuestionController _qController;
	private List<Category> _gameCategories;
	private int _winnings;

	public QuestionBoardScene(GameController controller, QuestionController quesionController) {
		_gController = controller;
		_qController = quesionController;
	}

	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_gameCategories = _qController.getGameCategories();
		_winnings = _gController.getWinnings();
		
		_layout = new GridPane();
		_layout.setStyle("-fx-background-color: black");

		GridPane board = new GridPane();
		board.setMaxSize(GameController.WINDOW_WIDTH, GameController.WINDOW_HEIGHT);
		board.setGridLinesVisible(true);
		board.getColumnConstraints().add(new ColumnConstraints(GameController.WINDOW_WIDTH / 5-10,GameController.WINDOW_WIDTH / 5,GameController.WINDOW_WIDTH /5 +10));
		for (int i = 0; i < 5; i++) {
			//Creating the category labels
			Text text = new Text();
			text.setFill(Color.WHITE);
			text.setText(_gameCategories.get(i).toString());
			text.setFont(new Font(16));
			
			//Normalising the label width
			double textWidth = text.getLayoutBounds().getWidth();
			if (textWidth >= GameController.WINDOW_WIDTH / 5 - 20) {
				double newSize = text.getFont().getSize() * (GameController.WINDOW_WIDTH / 5 - 20) / textWidth;
				text.setFont(new Font(newSize));
			}
			board.add(text, i, 0);
			GridPane.setMargin(text, new Insets(10, 10, 10, 10));
			GridPane.setHalignment(text, HPos.CENTER);
			
			//Creating the question buttons
			for (int j = 0; j < 5; j++) {
				if (_gameCategories.get(i).isQuestionAnswered(j)) {
					text = new Text("");
					text.setFill(Color.WHITE);
					text.setFont(new Font(16));
					board.add(text, i, j + 1);
					GridPane.setHalignment(text, HPos.CENTER);
					GridPane.setMargin(text, new Insets(5, 5, 5, 5));
					GridPane.setVgrow(text, Priority.ALWAYS);
					GridPane.setHgrow(text, Priority.ALWAYS);
				} else if (!_gameCategories.get(i).isQuestionAnswered(j)
						&& (j == 0 || _gameCategories.get(i).isQuestionAnswered(j - 1))) {
					String value = "$" + _gameCategories.get(i).getQuestionValue(j);
					Button btnQuestion = new Button(value);
					btnQuestion.setFont(new Font(16));
					btnQuestion.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							int category = GridPane.getColumnIndex(btnQuestion);
							int question = GridPane.getRowIndex(btnQuestion) - 1;
							_qController.setCurrentGameQuestion(_gameCategories.get(category).getQuestion(question));
							_gController.showAskGameQuestion();
						}
					});
					btnQuestion.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
					board.add(btnQuestion, i, j + 1);
					GridPane.setFillWidth(btnQuestion, true);
					GridPane.setFillHeight(btnQuestion, true);
					GridPane.setVgrow(btnQuestion, Priority.ALWAYS);
					GridPane.setHgrow(btnQuestion, Priority.ALWAYS);
				} else {
					text = new Text("$" + _gameCategories.get(i).getQuestionValue(j));
					text.setFill(Color.WHITE);
					text.setFont(new Font(16));
					board.add(text, i, j + 1);
					GridPane.setHalignment(text, HPos.CENTER);
					GridPane.setMargin(text, new Insets(5, 5, 5, 5));
					GridPane.setVgrow(text, Priority.ALWAYS);
					GridPane.setHgrow(text, Priority.ALWAYS);
				}
			}
		}
		_layout.add(board, 0, 0);
		GridPane.setVgrow(board, Priority.ALWAYS);
		GridPane.setHgrow(board, Priority.ALWAYS);	
		
		Label winnings = new Label("Winnings: $" + _winnings);
		winnings.setTextFill(Color.WHITE);
		winnings.setFont(new Font(18));

		Button btnReturn = new Button("Menu");
		btnReturn.setFont(new Font(16));
		btnReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showMenu();
			}
		});

		BorderPane bottomRow = new BorderPane();
		bottomRow.setLeft(winnings);
		BorderPane.setAlignment(winnings, Pos.CENTER_LEFT);
		bottomRow.setRight(btnReturn);
		_layout.add(bottomRow, 0, 1);
	}
	
	/*
	 * Returns a scene containing the generated components.
	 */
	public Scene getScene() {
		generate();
		Scene scene = new Scene(_layout, GameController.WINDOW_WIDTH, GameController.WINDOW_HEIGHT);
		scene.getStylesheets().add("DarkTheme.css");
		return scene;
	}
}
