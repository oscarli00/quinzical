package quinzical.modules.general;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class MacronButtons extends HBox{
	private TextField _answerField;
	
	/*
	 * Creates an HBox containing a set of macron buttons that inputs the selected macron
	 * to the specific text field on click.
	 */
	public MacronButtons(TextField answerField) {
		super();
		setAlignment(Pos.CENTER);
		_answerField=answerField;
		generate();
	}
	
	private void generate() {
		Button btnA=new Button("ā");
		btnA.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					_answerField.setText(_answerField.getText()+"ā");
				}
			});
		
		Button btnE=new Button("ē");
		btnE.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					_answerField.setText(_answerField.getText()+"ē");
				}
			});
		
		Button btnI=new Button("ī");
		btnI.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					_answerField.setText(_answerField.getText()+"ī");
				}
			});
		
		Button btnO=new Button("ō");
		btnO.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					_answerField.setText(_answerField.getText()+"ō");
				}
			});
		
		Button btnU=new Button("ū");
		btnU.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
					_answerField.setText(_answerField.getText()+"ū");
				}
			});
		
		getChildren().addAll(btnA,btnE,btnI,btnO,btnU);	
	}
}
