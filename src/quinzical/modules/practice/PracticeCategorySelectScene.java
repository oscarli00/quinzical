package quinzical.modules.practice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import quinzical.GameController;
import quinzical.data.Category;
import quinzical.data.QuestionController;

public class PracticeCategorySelectScene {
	private GridPane _layout;
	private GameController _gController;
	private QuestionController _qController;
	
	public PracticeCategorySelectScene(GameController gController, QuestionController qController) {	
		_gController=gController;
		_qController=qController;		
	}
		
	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout=new GridPane();
		_layout.setPadding(new Insets(10,10,10,10));
		
		Label label=new Label("Categories:");
		label.setTextFill(Color.WHITE);
		label.setFont(new Font(16));
		_layout.add(label, 0, 0);
		
		ObservableList<Category> categories = FXCollections.observableArrayList(_qController.getCategoryList());
		ListView<Category> listView = new ListView<Category>(categories);
		if (!listView.getItems().isEmpty()) {
			listView.getSelectionModel().select(0);
		}
		_layout.add(listView, 0, 1);		
		GridPane.setHgrow(listView, Priority.ALWAYS);
		
		Button btnSelect = new Button("Select");
		btnSelect.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (listView.getSelectionModel().getSelectedItem()!=null) {
					_qController.setPracticeCategory(listView.getSelectionModel().getSelectedItem());
					_gController.showAskPracticeQuestion();
				}				
			}
		});
		_layout.add(btnSelect, 1, 1);
		GridPane.setValignment(btnSelect, VPos.TOP);
		GridPane.setMargin(btnSelect, new Insets(0,10,10,10));
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
