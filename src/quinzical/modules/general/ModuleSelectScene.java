package quinzical.modules.general;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import quinzical.GameController;
import quinzical.data.Save;

public class ModuleSelectScene {
	private BorderPane _layout;
	private Save _save;
	private GameController _gController;

	public ModuleSelectScene(GameController g) {
		_gController = g;
	}

	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_save = _gController.getSave();
		_layout = new BorderPane();

		Text t = new Text("Select a Module");
		BorderPane.setMargin(t, new Insets(20, 20, 10, 20));
		BorderPane.setAlignment(t, Pos.CENTER);
		t.setFont(new Font(20));
		t.setFill(Color.WHITE);
		_layout.setTop(t);

		GridPane options = new GridPane();
		options.getColumnConstraints().add(new ColumnConstraints(GameController.WINDOW_WIDTH / 2 - 30));
		options.getRowConstraints().add(new RowConstraints(GameController.WINDOW_HEIGHT / 3));
		options.setAlignment(Pos.CENTER);
		BorderPane.setMargin(options, new Insets(10, 20, 20, 20));
		options.setHgap(20);
		options.setVgap(20);
		_layout.setCenter(options);

		Button btnGames = new Button("Games Mode");
		btnGames.setFont(new Font(16));
		btnGames.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnGames.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (_save.checkActiveGame()) {
					_gController.loadGame();
					_gController.showQuestionBoard();
				} else {
					_gController.showGameCategorySelect();
				}
			}
		});
		options.add(btnGames, 0, 0);
		GridPane.setHgrow(btnGames, Priority.ALWAYS);
		GridPane.setVgrow(btnGames, Priority.ALWAYS);

		Button btnPractice = new Button("Practice Mode");
		btnPractice.setFont(new Font(16));
		btnPractice.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnPractice.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showPracticeCategorySelect();
			}
		});
		options.add(btnPractice, 1, 0);
		GridPane.setHgrow(btnPractice, Priority.ALWAYS);
		GridPane.setVgrow(btnPractice, Priority.ALWAYS);

		// The international button only becomes visible when unlocked
		Button btnInternational = new Button("International Mode");
		btnInternational.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showAskIntQuestion();
			}
		});
		btnInternational.setVisible(false);
		if (_gController.checkIntGame()) {
			btnInternational.setVisible(true);
		}
		btnInternational.setFont(new Font(16));
		btnInternational.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		options.add(btnInternational, 0, 1);
		GridPane.setHgrow(btnInternational, Priority.ALWAYS);
		GridPane.setVgrow(btnInternational, Priority.ALWAYS);

		Button btnMenu = new Button("Menu");
		btnMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showMenu();
			}
		});
		_layout.setBottom(btnMenu);
		BorderPane.setAlignment(btnMenu, Pos.BOTTOM_RIGHT);
		BorderPane.setMargin(btnMenu, new Insets(5, 5, 5, 5));
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
