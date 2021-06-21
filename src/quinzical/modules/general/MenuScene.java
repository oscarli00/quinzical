package quinzical.modules.general;
import java.io.File;
import java.util.Optional;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import quinzical.GameController;

public class MenuScene {
	private VBox _layout;
	private GameController _gController;

	public MenuScene(GameController controller) {			
		_gController=controller;		
	} 
	
	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout=new VBox();
		_layout.setPadding(new Insets(10, 10, 10, 10));
		_layout.setAlignment(Pos.CENTER);
		_layout.setSpacing(15);
		
		Label img = new Label();
		img.setGraphic(new ImageView(new Image(new File(System.getProperty("user.dir")+"/images/fern.png").toURI().toString(),0,100,true,false)));
		
		Text welcome = new Text("Quinzical");
		welcome.setFill(Color.WHITE);
		welcome.setFont(new Font(40));
		welcome.setTextAlignment(TextAlignment.CENTER);
		
		Button btnPlay = new Button("Play");
		btnPlay.setMinWidth(120);
		btnPlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showModuleSelect();
			}
		});
		
		Button btnLeaderboard = new Button("Leaderboard");
		btnLeaderboard.setMinWidth(120);
		btnLeaderboard.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showLeaderboardScreen();
			}
		});

		Button btnHelp = new Button("Help");
		btnHelp.setMinWidth(120);
		btnHelp.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showHelpScreen();
			}
		});

		// The reset button requires confirmation before the game is reset
		Button btnReset = new Button("Reset");
		btnReset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert warning = new Alert(AlertType.CONFIRMATION);
				warning.getDialogPane().getStylesheets().add("DarkTheme.css");
				warning.setTitle("Reset");
				warning.setHeaderText("Reset the game");
				warning.setContentText("Are you sure? This will reset all game data including high scores.");
				warning.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
				
				//Changing OK button appearance
				Button btnOK = (Button) warning.getDialogPane().lookupButton(ButtonType.OK);
				btnOK.setDefaultButton(false);
				btnOK.getStylesheets().add("DarkTheme.css");
				
				Optional<ButtonType> result = warning.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					_gController.resetGame();
				}				
			}
		});
		btnReset.setMinWidth(120);

		Button btnQuit = new Button("Quit");
		btnQuit.setMinWidth(120);
		btnQuit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});
		
		_layout.getChildren().addAll(img,welcome,btnPlay,btnLeaderboard,btnHelp,btnReset,btnQuit);
	}
	
	/*
	 * Returns a scene containing the generated components.
	 */
	public Scene getScene() {
		generate();
		Scene scene = new Scene(_layout,GameController.WINDOW_WIDTH,GameController.WINDOW_HEIGHT);
		scene.getStylesheets().add("DarkTheme.css");
		return scene;
	}
}
