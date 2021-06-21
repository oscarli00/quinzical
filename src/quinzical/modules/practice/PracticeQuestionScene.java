package quinzical.modules.practice;

import java.io.File;

import javafx.animation.PauseTransition;
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
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import quinzical.GameController;
import quinzical.data.Question;
import quinzical.data.QuestionController;
import quinzical.modules.general.MacronButtons;
import quinzical.modules.general.Speaker;
import quinzical.modules.general.VoiceControls;

public class PracticeQuestionScene {
	private BorderPane _layout;
	private GameController _gController;
	private QuestionController _qController;
	private Question _question;
	private char _hint;
	private int _remainingAttempts;
	private HBox _topBox;
	private VBox _centerBox;
	private VBox _bottomBox;
	private TextField _answerField;
	private Label _questionText;
	private VBox _voiceControls;
	private HBox _answerBox;
	private HBox _macrons;
	private Text _attemptsText;
	private Speaker _tts;
	private PauseTransition _pt;

	public PracticeQuestionScene(GameController game, QuestionController question) {
		_gController = game;
		_qController = question;
	}

	/*
	 * Creating the layout for the scene
	 */
	private void generate() {		
		_question = _qController.getRandomNZQuestion();
		_hint = _question.getAnswer()[0].charAt(0);
		_remainingAttempts = 3;
		
		_layout = new BorderPane();
		_layout.setMaxSize(GameController.WINDOW_WIDTH, GameController.WINDOW_HEIGHT);
		_layout.setPadding(new Insets(10, 10, 12, 10));
		
		createQuestionBox();
		_layout.setCenter(_centerBox);
		BorderPane.setMargin(_centerBox, new Insets(12, 12, 12, 12));
		
		_topBox = new HBox(190);
		_attemptsText = new Text("Attempts remaining: " + _remainingAttempts);	
		_attemptsText.setFill(Color.WHITE);
		_voiceControls=new VoiceControls(_gController);
		_topBox.getChildren().addAll(_attemptsText,_voiceControls);
		_layout.setTop(_topBox);
		
		_bottomBox = new VBox();
		createAnswerField();
		_macrons = new MacronButtons(_answerField);
		_bottomBox.getChildren().addAll(_macrons,_answerBox);
		_layout.setBottom(_bottomBox);
		
		_tts=new Speaker();
		_tts.speak(_question.getQuestionString(), _gController.getVoiceSpeed());
	}
	
	/*
	 * This method creates the question text and play button
	 */
	private void createQuestionBox() {
		_centerBox = new VBox(10);
		_questionText = new Label(_question.getQuestionString());
		_questionText.setTextFill(Color.WHITE);
		_questionText.setFont(new Font(16));
		_questionText.setWrapText(true);
		
		Button btnPlay = new Button();
		btnPlay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_tts.stopSpeaking();
				_tts.speak(_question.getQuestionString(), _gController.getVoiceSpeed());
			}
		});
		Image play = new Image(new File(System.getProperty("user.dir")+"/images/play.png").toURI().toString());
		btnPlay.setGraphic(new ImageView(play));
		
		_centerBox.getChildren().addAll(_questionText,btnPlay);
		_centerBox.setAlignment(Pos.CENTER);
	}
	
	/*
	 * This method creates the answer field and defines its behaviour for incorrect 
	 * and correct answers
	 */
	private void createAnswerField() {		
		_answerBox = new HBox();
		_answerBox.setAlignment(Pos.BASELINE_LEFT);
		
		Text prefix = new Text(_question.getType() + " ");
		prefix.setFont(new Font(14));
		prefix.setFill(Color.WHITE);
		
		_answerField = new TextField();
		_answerField.setPromptText("Enter your answer here");
		_answerField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_tts.stopSpeaking();
				
				Text result = new Text();
				result.setFont(new Font(40));
				
				if (_qController.checkAnswer(_question, _answerField.getText())) {
					_topBox.getChildren().remove(_voiceControls);
					_layout.setPadding(new Insets(10, 10, 12, 10));
					
					result.setText("Correct!");
					result.setFill(Color.GREEN);
					_layout.setCenter(result);
									
					Button btnNewQ = new Button("New Question");
					btnNewQ.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_gController.showAskPracticeQuestion();
						}
					});
					
					Button btnChangeCat = new Button("Change Category");
					btnChangeCat.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_gController.showPracticeCategorySelect();
						}
					});
					
					Button btnMenu = new Button("Menu");
					btnMenu.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_gController.showMenu();
						}
					});
					
					HBox hbox = new HBox(20);
					hbox.getChildren().addAll(btnNewQ, btnChangeCat, btnMenu);
					hbox.setAlignment(Pos.CENTER);
					_layout.setBottom(hbox);
				} else {
					_remainingAttempts--;
					_attemptsText.setText("Attempts remaining: " + _remainingAttempts);
					
					result.setText("Incorrect");
					result.setFill(Color.RED);
					_layout.setCenter(result);
					
					if (_pt != null) {
						_pt.stop();
					}									
					_pt = new PauseTransition(Duration.seconds(0.75));
					_pt.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_layout.setCenter(_centerBox);
						}
					});
					
					if (_remainingAttempts == 1) {
						_layout.setPadding(new Insets(10, 10, 0, 10));
						Text hint = new Text("Hint: The answer begins with " + _hint);
						hint.setFill(Color.WHITE);
						_bottomBox.getChildren().add(hint);
					} else if (_remainingAttempts == 0) {
						_topBox.getChildren().remove(_voiceControls);
						_layout.setPadding(new Insets(10, 10, 12, 10));
						_questionText.setText(_questionText.getText() + "\n\nThe correct answer was: " + _question.getAnswer()[0]);
									
						Button btnNewQ = new Button("New Question");
						btnNewQ.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								_gController.showAskPracticeQuestion();
							}
						});
						
						Button btnChangeCat = new Button("Change Category");
						btnChangeCat.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								_gController.showPracticeCategorySelect();
							}
						});
						
						Button btnMenu = new Button("Menu");
						btnMenu.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								_gController.showMenu();
							}
						});
						
						HBox hbox = new HBox(20);
						hbox.getChildren().addAll(btnNewQ, btnChangeCat, btnMenu);
						hbox.setAlignment(Pos.CENTER);
						_layout.setBottom(hbox);
						
						_pt.setOnFinished(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent event) {
								_layout.setCenter(_questionText);
							}
						});
					}
					_pt.play();
				}
			}
		});
		_answerBox.getChildren().addAll(prefix, _answerField);
		HBox.setHgrow(_answerField, Priority.ALWAYS);
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
