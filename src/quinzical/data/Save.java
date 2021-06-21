package quinzical.data;

import java.util.ArrayList;
import java.util.List;

import quinzical.modules.leaderboard.User;

public class Save {
    private static Save _saveField = null;
    private int _score;
    private int _numCatsDone;
    private List<Integer> _activeCategories;
    private List<Integer> _activeQuestions;
    private List<Integer> _questionsDone;
    private int _activeGame;
    private List<User> _nzScores;
    private List<User> _intScores;
    private int _intUnlocked;

    private Save(int score, int numCatsDone, List<Integer> activeCategories, List<Integer> activeQuestions, List<Integer> questionsDone, int activeGame, List<User> nzScores, List<User> intScores, int intUnlocked) {
        _score = score;
        _numCatsDone = numCatsDone;
        _activeCategories = activeCategories;
        _activeQuestions = activeQuestions;
        _questionsDone = questionsDone;
        _activeGame = activeGame;
        _nzScores = nzScores;
        _intScores = intScores;
        _intUnlocked = intUnlocked;
    }

    public static Save getInstance(int score, int numCatsDone, List<Integer> activeCategories, List<Integer> activeQuestions, List<Integer> questionsDone, int newGame, List<User> nzScores, List<User> intScores, int intUnlocked){
        if(_saveField == null){
            _saveField = new Save(score, numCatsDone,activeCategories,activeQuestions,questionsDone, newGame, nzScores, intScores, intUnlocked);
        }
        return _saveField;
    }
    
    /*
     * Reset all game data
     */
    public void reset(){
        newGame();
        _nzScores.clear();
        _intScores.clear();
        _intUnlocked = 0;
    }
    
    /*
     * Create a new game in the games mode
     */
    public void newGame() {
    	_score = 0;
        _numCatsDone = 0;
        _activeCategories = new ArrayList<Integer>();
        _activeQuestions = new ArrayList<Integer>();
        _questionsDone = new ArrayList<Integer>();
        for (int i=0;i<2;i++) {
        	_activeCategories.add(0);
        	_activeQuestions.add(0);
        	_questionsDone.add(0);
        }
        _activeGame = 0;
    }

    public int getScore() {
        return _score;
    }

    public void setScore(int score) {
        _score = score;
    }
    
    public void setCatsDone(int i) {
    	_numCatsDone=0;
    }

    public int getNumCatsDone() {
        return _numCatsDone;
    }

    public void incrementNumCatsDone(){
        _numCatsDone++;
        if (_numCatsDone > 1) {
        	_intUnlocked = 1;
        }
    }

    public List<Integer> getQuestionsDone() {
        return _questionsDone;
    }

    public List<Integer> getActiveQuestions() {
        return _activeQuestions;
    }

    public List<Integer> getActiveCategories() {
        return _activeCategories;
    }

    public boolean isCategoryActive(int id){
        for(int i : _activeCategories){
            if(id == i){
                return true;
            }
        }
        return false;
    }

    public boolean isQuestionActive(int id){
        for(int i : _activeQuestions){
            if(id == i){
                return true;
            }
        }
        return false;
    }

    public boolean isQuestionDone(int id){
        for(int i : _questionsDone){
            if(id == i){
                return true;
            }
        }
        return false;
    }
    
    public void addActiveCategory(int id){
        _activeCategories.add(id);
    }
    
    public void addActiveQuestion(int id){
        _activeQuestions.add(id);
    }
    
    public void addQuestionDone(int id){
        _questionsDone.add(id);
    }
    
    public boolean checkActiveGame(){
        return _activeGame == 1;
    }
    
    public void startGame(){
        _activeGame = 1;
    }
    
    public boolean intUnlocked() {
    	return _intUnlocked==1;
    }
    
    public List<User> getNZScores() {
    	return _nzScores;
    }
    public List<User> getInternationalScores() {
    	return _intScores;
    }
}
