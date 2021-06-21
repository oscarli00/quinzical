package quinzical.modules.general;

import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import quinzical.GameController;


public class HelpScene {
	private BorderPane _layout;
	private GameController _gController;


	public HelpScene(GameController g) {
		_gController=g;
	}
	
	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout=new BorderPane();

		Text helpText = new Text("For help on how to use Quinzical, visit the link below");
		helpText.setFont(new Font(16));
		helpText.setFill(Color.WHITE);

		Text message = new Text("Copied to clipboard!");
		message.setFill(Color.WHITE);
		message.setVisible(false);

		//Creating a hyperlink that gets copied to clipboard on click
		Hyperlink link = new Hyperlink("https://bit.ly/34Ionem");
		link.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Clipboard clip = Clipboard.getSystemClipboard();
				ClipboardContent content = new ClipboardContent();
				content.putString(link.getText());
				clip.setContent(content);

				message.setVisible(true);
				PauseTransition pt = new PauseTransition(Duration.seconds(1));
				pt.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						message.setVisible(false);
					}
				});
				pt.play();
			}
		});

		Button btnMenu = new Button("Menu");
		btnMenu.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showMenu();
			}
		});

		VBox gameInfo = new VBox(5);     
		gameInfo.getChildren().addAll(helpText,link,message,btnMenu);
		gameInfo.setAlignment(Pos.CENTER);
		_layout.setCenter(gameInfo);
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