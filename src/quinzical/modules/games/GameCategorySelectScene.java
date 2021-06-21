package quinzical.modules.games;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import quinzical.GameController;
import quinzical.data.Category;
import quinzical.data.QuestionController;

public class GameCategorySelectScene {
	private GridPane _layout;
	private GameController _gController;
	private QuestionController _qController;
	
	public GameCategorySelectScene(GameController gController, QuestionController qController) {	
		_gController=gController;
		_qController=qController;		
	}
	
	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout=new GridPane();
		_layout.setPadding(new Insets(10,10,10,10));
		Label label=new Label("Please select five categories:");
		label.setTextFill(Color.WHITE);
		label.setFont(new Font(16));
		_layout.add(label, 0, 0);	
		
		VBox buttonGroup = new VBox(15);
		
		Button btnStart = new Button("Start");	
		btnStart.setFont(new Font(18));
		btnStart.setVisible(false);
		btnStart.setMinWidth(100);
		
		Button btnCancel = new Button("Cancel");
		btnCancel.setFont(new Font(18));
		btnCancel.setMinWidth(100);
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showModuleSelect();
			}});
		
		buttonGroup.getChildren().addAll(btnStart,btnCancel);		
		_layout.add(buttonGroup, 1, 1);
		GridPane.setValignment(buttonGroup, VPos.TOP);
		GridPane.setMargin(buttonGroup, new Insets(0,10,10,10));
	
		//Creating a ListView of checkboxes, one checkbox for each category
		VBox checkboxes = new VBox(5);
		ScrollPane scroll = new ScrollPane(checkboxes);	
		List<CategoryCheckBox> boxes = new ArrayList<CategoryCheckBox>();		
		for (Category c : _qController.getCategoryList()) {
			CategoryCheckBox check = new CategoryCheckBox(c);
			check.setFont(new Font(16));
			boxes.add(check);
			check.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					int count=0;
					for (CategoryCheckBox ccb : boxes) {
						if (ccb.isSelected()) {
							count++;
						}
					}
					if (count==5) {
						btnStart.setVisible(true);
					} else {
						btnStart.setVisible(false);
					}
				}				
			});
			checkboxes.getChildren().add(check);
		}
		_layout.add(scroll, 0, 1);
		GridPane.setHgrow(scroll, Priority.ALWAYS);
		GridPane.setVgrow(scroll, Priority.ALWAYS);
		
		btnStart.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				List<Category> gameCategories = new ArrayList<Category>();
				for (Node n : checkboxes.getChildren()) {
					CategoryCheckBox ccb = (CategoryCheckBox) n;
					if (ccb.isSelected()) {
						_gController.saveCategory(ccb.getCategory().getId());
						gameCategories.add(ccb.getCategory());
					}					
				}
				_qController.setGameCategories(gameCategories);
				_gController.showQuestionBoard();
			}
		});
		
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
