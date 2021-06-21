package quinzical.modules.international;

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
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import quinzical.GameController;
import quinzical.data.Question;
import quinzical.data.QuestionController;
import quinzical.modules.general.MacronButtons;
import quinzical.modules.general.Speaker;
import quinzical.modules.general.VoiceControls;

public class InternationalQuestionScene {
	private static final int TIMER_AMOUNT = 90;
	private BorderPane _layout;
	private GameController _gController;
	private QuestionController _qController;
	private Question _question;
	private HBox _buttonBox;
	private TextField _answerField;
	private VoiceControls _voiceControls;
	private Label _questionText;
	private VBox _questionBox;
	private VBox _answerBox;
	private Speaker _tts;
	private int _secondsRemaining = TIMER_AMOUNT;
	private Timeline _timeline;
	private Label _timerText;
	private int _score;
	private Label _scoreLabel;
	private Text _prefix;
	private PauseTransition _pt;

	public InternationalQuestionScene(GameController gcontroller, QuestionController qcontroller) {
		_gController = gcontroller;
		_qController = qcontroller;
	}

	/*
	 * Creating the layout for the scene
	 */
	private void generate() {
		_score=0;
		_tts = new Speaker();

		BorderPane topBox = new BorderPane();
		_scoreLabel = new Label("Score: $" + _score);
		_scoreLabel.setFont(new Font(14));
		_scoreLabel.setTextFill(Color.WHITE);

		_voiceControls = new VoiceControls(_gController);

		topBox.setLeft(_scoreLabel);
		topBox.setRight(_voiceControls);
		_layout.setTop(topBox);

		createQuestionBox();
		createBottomBox();
		createTimer();
	}

	/*
	 * Creating a time and implementing the layout's behaviour for 
	 * when the timer hits zero.
	 */
	private void createTimer() {
		_timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_secondsRemaining--;
				if (_secondsRemaining <= 0) {
					_tts.stopSpeaking();
					_timeline.stop();
					if (_pt != null) {
						_pt.stop();
					}

					_secondsRemaining = TIMER_AMOUNT;
					_timerText.setText("Timeout!");
					_timerText.setTextFill(Color.RED);
					_layout.setTop(_timerText);
					BorderPane.setAlignment(_timerText, Pos.CENTER);

					_scoreLabel.setText("Congratulations! Your final score was $" + _score
							+ ".\nEnter your name below to be shown on the leaderboard");
					_scoreLabel.setAlignment(Pos.CENTER);

					TextField nameField = new TextField();
					nameField.setPromptText("Enter your name here");
					nameField.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_gController.addInternationalScore(nameField.getText(), _score);
							_gController.showLeaderboardScreen();
						}
					});

					Button btnContinue = new Button("Enter");
					btnContinue.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							_gController.addInternationalScore(nameField.getText(), _score);
							_gController.showLeaderboardScreen();
						}
					});
					HBox hbox = new HBox(5);
					hbox.setAlignment(Pos.CENTER);
					hbox.getChildren().addAll(nameField, btnContinue);

					VBox vbox = new VBox(15);
					vbox.getChildren().addAll(_scoreLabel, hbox);
					vbox.setAlignment(Pos.CENTER);
					_layout.setCenter(vbox);

					_layout.setBottom(null);

				} else {
					_timerText.setText(String.valueOf(_secondsRemaining));
				}
			}
		}));
		_timeline.setCycleCount(TIMER_AMOUNT + 1);
		_timeline.play();
	}

	/*
	 * This method is called first before generate() to allow the user to confirm
	 * that they want to start a game.
	 */
	private void getConfirmation() {
		_layout = new BorderPane();
		_layout.setMaxSize(GameController.WINDOW_WIDTH, GameController.WINDOW_HEIGHT);
		_layout.setPadding(new Insets(10, 10, 10, 10));

		VBox vb = new VBox(15);
		vb.setAlignment(Pos.CENTER);

		Label txt = new Label("You have " + TIMER_AMOUNT
				+ " seconds to answer as many questions as you can. \nYou get $1000 for every correct answer. \nPress OK when you are ready to begin.");
		txt.setTextFill(Color.WHITE);
		txt.setWrapText(true);
		txt.setTextAlignment(TextAlignment.CENTER);

		HBox buttonGroup = new HBox(10);
		buttonGroup.setAlignment(Pos.CENTER);

		Button btnYes = new Button("OK");
		btnYes.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				generate();
			}
		});

		Button btnNo = new Button("No, take me back");
		btnNo.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_gController.showModuleSelect();
			}
		});

		buttonGroup.getChildren().addAll(btnYes, btnNo);
		vb.getChildren().addAll(txt, buttonGroup);
		_layout.setCenter(vb);
	}

	/*
	 * This method creates the question text and play button
	 */
	private void createQuestionBox() {
		_question = _qController.getRandomInternationalQuestion(_question);

		_timerText = new Label(String.valueOf(_secondsRemaining));
		_timerText.setTextFill(Color.WHITE);
		_timerText.setFont(new Font(16));
		_timerText.setWrapText(true);

		_questionBox = new VBox(10);
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
		BorderPane.setMargin(_questionBox, new Insets(12, 12, 12, 12));

		_layout.setCenter(_questionBox);

		_tts.stopSpeaking();
		_tts.speak(_question.getQuestionString(), _gController.getVoiceSpeed());
	}

	/*
	 * This method creates the answer field and defines the behaviour for incorrect
	 * and correct answers
	 */
	private void createAnswerField() {
		_answerField = new TextField();
		_answerField.setPromptText("Enter your answer here");
		_answerField.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_tts.stopSpeaking();

				Text result = new Text();
				result.setFont(new Font(40));
				if (_qController.checkAnswer(_question, _answerField.getText())) {
					_score += 1000;
					_scoreLabel.setText("Score: $" + _score);
					result.setText("Correct!");
					result.setFill(Color.GREEN);
					_layout.setCenter(result);
				} else {
					result.setText("Incorrect");
					result.setFill(Color.RED);
					_layout.setCenter(result);

				}
				_answerField.clear();

				_pt = new PauseTransition(Duration.seconds(0.5));
				_pt.setOnFinished(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						createQuestionBox();
						_prefix.setText(_question.getType() + " ");
					}
				});
				_pt.play();
			}
		});
	}

	/*
	 * This method creates the prefix label ("What is","Who is", etc), answer field,
	 * the "Don't Know" button and groups them together into a VBox.
	 */
	private void createBottomBox() {
		_answerBox = new VBox();
		HBox hbox = new HBox();
		hbox.setAlignment(Pos.BASELINE_LEFT);

		_prefix = new Text(_question.getType() + " ");
		_prefix.setFill(Color.WHITE);
		_prefix.setFont(new Font(14));

		createAnswerField();
		HBox.setHgrow(_answerField, Priority.ALWAYS);
		HBox.setMargin(_answerField, new Insets(0, 10, 0, 0));

		Button btnDontKnow = new Button("Don't know");
		btnDontKnow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				createQuestionBox();
				_prefix.setText(_question.getType() + " ");
			}
		});

		hbox.getChildren().addAll(_prefix, _answerField, btnDontKnow);
		_buttonBox = new MacronButtons(_answerField);
		_answerBox.getChildren().addAll(_buttonBox, hbox);

		_layout.setBottom(_answerBox);
	}

	/*
	 * Returns a scene containing the generated components.
	 */
	public Scene getScene() {
		getConfirmation();
		Scene scene = new Scene(_layout, GameController.WINDOW_WIDTH, GameController.WINDOW_HEIGHT);
		scene.getStylesheets().add("DarkTheme.css");
		return scene;
	}
}