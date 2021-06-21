package quinzical.modules.leaderboard;

import java.util.Collections;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import quinzical.GameController;

public class LeaderboardScene {
	private BorderPane _layout;
	private GameController _gController;
	private List<User> _nz;
	private List<User> _international;
	private Label _nzScores;
	private Label _internationalScores;

	public LeaderboardScene(GameController controller) {			
		_gController=controller;		
		_nz=_gController.getNZScores();
		_international=_gController.getInternationalScores();
	} 
	
	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout=new BorderPane();
		
		GridPane leaderboards=new GridPane();
		
		Label nzLabel = new Label("NZ mode");
		nzLabel.setFont(new Font(20));
		nzLabel.setTextFill(Color.WHITE);
		leaderboards.add(nzLabel, 0, 0);
		
		Label internationalLabel = new Label("International mode");
		internationalLabel.setFont(new Font(20));
		internationalLabel.setTextFill(Color.WHITE);
		leaderboards.add(internationalLabel, 1, 0);
		
		createNZLeaderboard();
		ScrollPane scrollNZ = new ScrollPane(_nzScores);
		scrollNZ.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		leaderboards.add(scrollNZ, 0, 1);
		
		createInternationalLeaderboard();
		ScrollPane scrollInternational = new ScrollPane(_internationalScores);
		scrollInternational.setPrefSize(Integer.MAX_VALUE, Integer.MAX_VALUE);
		leaderboards.add(scrollInternational, 1, 1);
		
		_layout.setCenter(leaderboards);
		
		Button btnReturn = new Button("Menu");
		btnReturn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showMenu();
			}
		});
		_layout.setBottom(btnReturn);
		BorderPane.setAlignment(btnReturn, Pos.CENTER);		
	}
	
	/*
	 * Creates a label with each user's NZ score on each line
	 */
	private void createNZLeaderboard() {
		Collections.sort(_nz);
		Collections.reverse(_nz);
		
		_nzScores=new Label();
		_nzScores.setFont(new Font(16));
		_nzScores.setTextFill(Color.WHITE);
		
		int count=1;
		for (User u : _nz) {
			_nzScores.setText(_nzScores.getText()+count+".  $"+u.getScore()+"    "+u.getName()+"\n");
			count++;
		}	
	}
	
	/*
	 * Creates a label with each user's International score on each line
	 */
	private void createInternationalLeaderboard() {
		Collections.sort(_international);
		Collections.reverse(_international);
		
		_internationalScores=new Label();
		_internationalScores.setFont(new Font(16));
		_internationalScores.setTextFill(Color.WHITE);
		
		int count=1;
		for (User u : _international) {
			_internationalScores.setText(_internationalScores.getText()+count+".  $"+u.getScore()+"    "+u.getName()+"\n");
			count++;
		}
		
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
