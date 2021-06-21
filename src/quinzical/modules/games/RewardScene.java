package quinzical.modules.games;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import quinzical.GameController;

public class RewardScene {
	private BorderPane _layout;
	private GameController _gController;
	private int _winnings;

	public RewardScene(GameController g) {
		_gController=g;
	}
	
	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout=new BorderPane();
		_winnings=_gController.getWinnings();					
		
		Label gameOver = new Label("Game Over");
		gameOver.setTextFill(Color.WHITE);
		gameOver.setFont(new Font(40));
		
		Label finalScore = new Label("Your final score is: $"+_winnings);
		finalScore.setTextFill(Color.WHITE);
		
		Label rewardText = new Label("Congratulations! You have earned the title of ");			
		String url=System.getProperty("user.dir")+"/images/";
		if (_winnings < 3000) {
			rewardText.setText(rewardText.getText()+"Wonderful Weta!");
			url=url+"weta.png";
		} else if (_winnings < 6000) {
			rewardText.setText(rewardText.getText()+"Knowledgeable Kiwi!");
			url=url+"kiwi.png";
		} else if (_winnings < 9000) {
			rewardText.setText(rewardText.getText()+"Brilliant Bellbird!");
			url=url+"bellbird.png";
		} else if (_winnings < 12000) {
			rewardText.setText(rewardText.getText()+"Fantastic Fantail!");
			url=url+"fantail.png";
		} else {
			rewardText.setText(rewardText.getText()+"Talented Tuatara!");
			url=url+"tuatara.png";
		}	
		
		Label img = new Label();
		img.setGraphic(new ImageView(new Image(new File(url).toURI().toString(),300,0,true,false)));
		
		Label captionText= new Label();
		if (_winnings < 3000) {
			captionText.setText("Earn $3000 next time to earn a higher title");
		} else if (_winnings < 6000) {
			captionText.setText("Earn $6000 next time to earn a higher title");
		} else if (_winnings < 9000) {
			captionText.setText("Earn $9000 next time to earn a higher title");
		} else if (_winnings < 12000) {
			captionText.setText("Earn $12000 next time to earn a higher title");
		} else {
			captionText.setText("Nice! You have earned the highest title");
		}	
		
		Label prompt = new Label("Enter your name below");
		VBox.setMargin(prompt, new Insets(10,10,0,10));
		
		TextField nameField = new TextField();
		nameField.setPromptText("Enter your name here");
		nameField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.addNZScore(nameField.getText(), _winnings);
				_gController.showLeaderboardScreen();
			}});
		
		Button btnContinue = new Button("Enter");
		btnContinue.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.addNZScore(nameField.getText(), _winnings);
				_gController.showLeaderboardScreen();
			}
		});		
		HBox userInput = new HBox(5);
		userInput.setAlignment(Pos.CENTER);
		userInput.getChildren().addAll(nameField,btnContinue);
		VBox.setMargin(userInput, new Insets(5,10,10,10));
		
		VBox gameInfo = new VBox();
		gameInfo.setAlignment(Pos.CENTER);
		gameInfo.getChildren().addAll(gameOver,finalScore,rewardText,img,captionText,prompt,userInput);		
		_layout.setCenter(gameInfo);
		
		_gController.newGame();
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
