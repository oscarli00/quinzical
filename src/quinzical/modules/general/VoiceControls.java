package quinzical.modules.general;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import quinzical.GameController;

public class VoiceControls extends VBox {
	private GameController _gController;
	
	/*
	 * Creates a VBox containing a speed slider and text field that allows the user to 
	 * control the speed of speech synthesis.
	 */
	public VoiceControls(GameController g) {
		super(5);
		_gController=g;
		generate();
	}
	
	private void generate() {
		Slider speedControl = new Slider(25,200,_gController.getVoiceSpeed());
		speedControl.setMajorTickUnit(25);
		speedControl.setShowTickLabels(true);
		speedControl.setShowTickMarks(true);
		speedControl.setPrefWidth(0);
		
		TextField speedText = new TextField(String.valueOf(_gController.getVoiceSpeed()));
		speedControl.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				speedText.setText(String.valueOf(newValue.intValue()));
				_gController.setVoiceSpeed(newValue.intValue());
			}
		});
		
		// The textfield changes the synthesis speed when the user presses enter
		speedText.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				int value;
				try {
					value=(int)Math.round(Double.valueOf(speedText.getText()));
					speedControl.setValue(value);
					speedText.setText(String.valueOf((int)speedControl.getValue()));
					_gController.setVoiceSpeed(value);
				} catch (Exception e) {
					speedText.setText(String.valueOf((int)speedControl.getValue()));
				}
			}
		});
		
		//The textfield also changes the synthesis speed when the text field loses focus
		//(i.e. the user clicks on another component)
		speedText.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue, Boolean newValue) {
				if (!newValue) {
					int value;
					try {
						value=(int)Math.round(Double.valueOf(speedText.getText()));
						speedControl.setValue(value);
						speedText.setText(String.valueOf((int)speedControl.getValue()));
						_gController.setVoiceSpeed(value);
					} catch (Exception e) {
						speedText.setText(String.valueOf((int)speedControl.getValue()));
					}
				}
			}
		});
		
		HBox speedInfo = new HBox();
		Text prefix = new Text("Voice speed:");
		Text suffix = new Text("%");
		prefix.setFill(Color.WHITE);
		suffix.setFill(Color.WHITE);
		speedInfo.getChildren().addAll(prefix,speedText,suffix);
		getChildren().addAll(speedControl,speedInfo);
		setAlignment(Pos.CENTER);
		setMaxWidth(150);
	}
}
