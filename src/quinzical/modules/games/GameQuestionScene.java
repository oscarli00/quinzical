package quinzical.modules.games;

import java.io.File;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
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

public class GameQuestionScene {
	private static final int TIMER_AMOUNT = 20;
	private static final int READING_TIME = 4;
	private BorderPane _layout;
	private GameController _gController;
	private QuestionController _qController;
	private Question _question;
	private HBox _macronButtons;
	private TextField _answerField;
	private VBox _voiceControls;
	private Label _questionText;
	private Label _timerText;
	private int _secondsRemaining = TIMER_AMOUNT;
	private Timeline _timeline;
	private VBox _questionBox;
	private VBox _answerBox;
	private Speaker _tts;

	public GameQuestionScene(GameController gcontroller, QuestionController qcontroller) {
		_gController = gcontroller;
		_qController = qcontroller;
	}

	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_layout = new BorderPane();
		_layout.setMaxSize(GameController.WINDOW_WIDTH, GameController.WINDOW_HEIGHT);
		_layout.setPadding(new Insets(10, 10, 10, 10));

		_question = _qController.getCurrentGameQuestion();
		_question.setAnswered(true);
		_gController.addAnswered(_question.getId());
		_gController.updateNumCatsDone();

		createQuestionBox();
		_layout.setCenter(_questionBox);
		BorderPane.setMargin(_questionBox, new Insets(12, 12, 12, 12));

		_voiceControls = new VoiceControls(_gController);
		_layout.setTop(_voiceControls);
		BorderPane.setAlignment(_voiceControls, Pos.CENTER_RIGHT);

		createAnswerBox();
		_layout.setBottom(_answerBox);

		_tts = new Speaker();
		_tts.speak(_question.getQuestionString(), _gController.getVoiceSpeed());

		_secondsRemaining = TIMER_AMOUNT + READING_TIME;
		createTimer();
	}

	/*
	 * This method creates the timer and defines its behaviour for when the user
	 * runs out of time.
	 */
	private void createTimer() {
		_timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_secondsRemaining--;
				if (_secondsRemaining == 0) {
					_tts.stopSpeaking();
					_timeline.stop();
					
					_secondsRemaining = TIMER_AMOUNT + READING_TIME;
					
					_timerText.setText("Timeout!");
					_timerText.setTextFill(Color.RED);
					_layout.setTop(_timerText);
					
					_questionText.setText(_questionText.getText() + "\n\nThe correct answer was: " + _question.getAnswer()[0]);
					_layout.setCenter(_questionText);
					
					Button btnContinue = new Button("Continue");
					btnContinue.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							if (_qController.isGameComplete()) {
								_gController.showRewardScreen();
							} else {
								_gController.showQuestionBoard();
							}
						}
					});
					_layout.setBottom(btnContinue);
					BorderPane.setAlignment(btnContinue, Pos.CENTER);
					BorderPane.setAlignment(_timerText, Pos.CENTER);
				} else if (_secondsRemaining > TIMER_AMOUNT) {
					_timerText.setText("");
				} else {
					_timerText.setText(String.valueOf(_secondsRemaining));
				}
			}
		}));
		_timeline.setCycleCount(TIMER_AMOUNT + READING_TIME + 1);
		_timeline.play();
	}

	/*
	 * This method creates the question text and play button
	 */
	private void createQuestionBox() {
		_questionBox = new VBox(10);
		
		_timerText = new Label();
		_timerText.setTextFill(Color.WHITE);
		_timerText.setFont(new Font(16));
		_timerText.setWrapText(true);
		
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
		Image play = new Image(new File(System.getProperty("user.dir") + "/images/play.png").toURI().toString());
		btnPlay.setGraphic(new ImageView(play));
		
		_questionBox.getChildren().addAll(_timerText, _questionText, btnPlay);
		_questionBox.setAlignment(Pos.CENTER);
	}

	/*
	 * This method creates the answer field and defines its behaviour for incorrect
	 * and correct answers
	 */
	private void createAnswerField() {
		_answerField = new TextField();
		_answerField.setPromptText("Enter your answer here");
		_answerField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_tts.stopSpeaking();
				_timeline.stop();

				Text result = new Text();
				result.setFont(new Font(40));
				if (_qController.checkAnswer(_question, _answerField.getText())) {
					VBox texts = new VBox();
					result.setText("Correct!");
					result.setFill(Color.GREEN);
					
					Label amountWon = new Label("You have won: $" + _question.getValue());
					amountWon.setFont(new Font(14));
					amountWon.setTextFill(Color.WHITE);
					
					texts.getChildren().addAll(result, amountWon);
					texts.setAlignment(Pos.CENTER);
					_layout.setCenter(texts);
					
					_gController.setWinnings(_gController.getWinnings() + _question.getValue());
				} else {
					_questionText.setText(_questionText.getText() + "\n\nThe correct answer was: " + _question.getAnswer()[0]);
					result.setText("Incorrect");
					result.setFill(Color.RED);
					_layout.setCenter(result);
					
					PauseTransition pt = new PauseTransition(Duration.seconds(0.75));
					pt.setOnFinished(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_layout.setCenter(_questionText);
						}
					});
					pt.play();
				}
				_layout.setTop(null);

				Button btnContinue = new Button("Continue");
				btnContinue.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (_qController.isGameComplete()) {
							_gController.showRewardScreen();
						} else {
							_gController.showQuestionBoard();
						}
					}
				});
				_layout.setBottom(btnContinue);
				BorderPane.setAlignment(btnContinue, Pos.CENTER);
			}
		});
	}

	/*
	 * This method creates the prefix label ("What is","Who is", etc), answer field,
	 * the "Don't Know" button and groups them together into a VBox.
	 */
	private void createAnswerBox() {
		_answerBox = new VBox();
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BASELINE_LEFT);
		
		Text prefix = new Text(_question.getType() + " ");
		prefix.setFill(Color.WHITE);
		prefix.setFont(new Font(14));
		
		createAnswerField();
		HBox.setHgrow(_answerField, Priority.ALWAYS);
		HBox.setMargin(_answerField, new Insets(0, 10, 0, 0));
		
		Button btnDontKnow = new Button("Don't know");
		btnDontKnow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_tts.stopSpeaking();
				_timeline.stop();
				_questionText.setText(_questionText.getText() + "\n\nThe correct answer was: " + _question.getAnswer()[0]);
				_layout.setCenter(_questionText);
				
				Button btnContinue = new Button("Continue");
				btnContinue.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						if (_qController.isGameComplete()) {
							_gController.showRewardScreen();
						} else {
							_gController.showQuestionBoard();
						}
					}
				});
				
				_layout.setTop(null);
				_layout.setBottom(btnContinue);
				BorderPane.setAlignment(btnContinue, Pos.CENTER);
			}
		});		
		
		hbox.getChildren().addAll(prefix, _answerField, btnDontKnow);
		_macronButtons = new MacronButtons(_answerField);
		_answerBox.getChildren().addAll(_macronButtons, hbox);
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
