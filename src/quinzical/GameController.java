package quinzical;

import javafx.application.Application;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import quinzical.data.QuestionController;
import quinzical.data.Save;
import quinzical.data.SaveController;
import quinzical.modules.games.GameCategorySelectScene;
import quinzical.modules.games.GameQuestionScene;
import quinzical.modules.games.QuestionBoardScene;
import quinzical.modules.games.RewardScene;
import quinzical.modules.general.HelpScene;
import quinzical.modules.general.MenuScene;
import quinzical.modules.general.ModuleSelectScene;
import quinzical.modules.international.InternationalQuestionScene;
import quinzical.modules.leaderboard.LeaderboardScene;
import quinzical.modules.leaderboard.User;
import quinzical.modules.practice.PracticeCategorySelectScene;
import quinzical.modules.practice.PracticeQuestionScene;

import java.io.FileNotFoundException;
import java.util.List;

public class GameController extends Application {
	public static final int WINDOW_WIDTH=500;
	public static final int WINDOW_HEIGHT=450;
	private Stage _primaryStage;
	private QuestionController _questionController;
	private SaveController _saveController;
	private int _winnings=0;
	private Save _save;
	private int _voiceSpeed=100;
	private MenuScene _menu;
	private QuestionBoardScene _questionBoard;
	private ModuleSelectScene _moduleSelect;
	private GameCategorySelectScene _gameCategorySelect;
	private PracticeCategorySelectScene _practiceCategorySelect;
	private PracticeQuestionScene _pracQuestion;
	private GameQuestionScene _gameQuestion;
	private InternationalQuestionScene _intQuestion;
	private RewardScene _reward;
	private HelpScene _help;
	private LeaderboardScene _leaderboard;
	
	public void showMenu() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_menu.getScene());
	}
	
	public void showQuestionBoard() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_questionBoard.getScene());
	}
	
	public void showGameCategorySelect() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_gameCategorySelect.getScene());
	}
	
	public void showPracticeCategorySelect() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_practiceCategorySelect.getScene());
	}
	
	public void showModuleSelect() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_moduleSelect.getScene());
	}
	
	public void showAskPracticeQuestion() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_pracQuestion.getScene());
	}
	
	public void showAskGameQuestion() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_gameQuestion.getScene());
	}

	public void showAskIntQuestion() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_intQuestion.getScene());
	}
	
	public void showRewardScreen() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_reward.getScene());
	}

	public void showHelpScreen() {
		_saveController.saveGame(_save);
		_primaryStage.setScene(_help.getScene());
	}
	
	public void showLeaderboardScreen() {
		_primaryStage.setScene(_leaderboard.getScene());
	}

	public void setWinnings(int i) {
		_winnings=i;
		_save.setScore(i);
		saveGame();
	}
	
	public int getWinnings() {
		return _winnings;
	}
	
	public void setVoiceSpeed(int i) {
		_voiceSpeed=i;
	}
	
	public int getVoiceSpeed() {
		return _voiceSpeed;
	}

	public Save getSave(){
		return _save;
	}
	
	/*
	 * Starts a new Games mode game but keeps leaderboard scores and 
	 * international section unlocked
	 */
	public void newGame() {
		_saveController.newGame();
		_save.newGame();
		try {
			_save = _saveController.loadGame();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		_questionController.resetGame();
		_winnings=0;
	}

	/*
	 * Resets all game data
	 */
	public void resetGame() {
		_saveController.reset();
		_save.reset();
		try {
			_save = _saveController.loadGame();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		_questionController.resetGame();
		_winnings=0;
	}
	
	/*
	 * Creating an instance of every scene-related class
	 */
	private void createScenes() {
		_menu=new MenuScene(this);
		_moduleSelect=new ModuleSelectScene(this);
		_questionBoard=new QuestionBoardScene(this,_questionController);
		_gameCategorySelect=new GameCategorySelectScene(this,_questionController);
		_practiceCategorySelect=new PracticeCategorySelectScene(this,_questionController);
		_pracQuestion=new PracticeQuestionScene(this,_questionController);
		_gameQuestion=new GameQuestionScene(this,_questionController);
		_intQuestion=new InternationalQuestionScene(this,_questionController);
		_reward= new RewardScene(this);
		_help = new HelpScene(this);
		_leaderboard = new LeaderboardScene(this);
	}
	
	/*
	 * Load in save data using SaveController
	 */
	private void loadSave(){				
		_saveController = new SaveController();
		try {
			_save = _saveController.loadGame();
			setWinnings(_save.getScore());			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void loadGame(){
		_questionController.setGameCategories(null);
	}
	
	public void saveCategory(int id){
		_save.addActiveCategory(id);
	}
	
	public void updateNumCatsDone(){
		_questionController.updateNumCatsDone();
	}
	
	public boolean checkIntGame(){
		return _save.intUnlocked();
	}

	public void saveGame(){
		_saveController.saveGame(_save);
	}
	
	public void addAnswered(int id){
		_save.addQuestionDone(id);
	}
	
	public void addNZScore(String name, int score) {
		_save.getNZScores().add(new User(name,score));
	}
	
	public void addInternationalScore(String name, int score) {
		_save.getInternationalScores().add(new User(name,score));
	}
	
	public List<User> getNZScores() {
		return _save.getNZScores();
	}
	
	public List<User> getInternationalScores() {
		return _save.getInternationalScores();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			loadSave();
			_questionController = new QuestionController(this);
			_primaryStage = primaryStage;
			_primaryStage.setResizable(false);
			_primaryStage.setTitle("Quinzical");
			createScenes();
			showMenu();
			_primaryStage.show();
		}catch(FileNotFoundException e){
			Alert noCatAlert = new Alert(Alert.AlertType.ERROR);
			noCatAlert.setContentText(e.getMessage());
			noCatAlert.show();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
