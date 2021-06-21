package quinzical.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import quinzical.GameController;

public class  QuestionController {
    private Save _save;
	private QuestionBoard _questionBoard;
    private String _catPath = System.getProperty("user.dir") + "/categories/";
    private Category _practiceCategory;
    private Category _internationalCategory;
    private Question _currentGameQuestion;
    private Random _random = new Random();

    
    public QuestionController(GameController gameController) throws FileNotFoundException {
    	_questionBoard=new QuestionBoard(makeGameCategories(),gameController.getSave());
    	_internationalCategory=new Category("International",makeQuestions(new File(_catPath+"International")));
    	_save=gameController.getSave();
    }
    
    /*
     * Creates a list of categories made from all files in the categories folder excluding international
     */
    private List<Category> makeGameCategories() throws FileNotFoundException {
        List<Category> catList = new ArrayList<Category>();
        File folder = new File(_catPath);
        File[] listOfFiles = folder.listFiles();
        if(listOfFiles != null&&listOfFiles.length > 0) {
            for (File file : listOfFiles) {
                if (file.isFile() &&!file.getName().equals("International")) {
                    catList.add(new Category(file.getName(), makeQuestions(file)));
                }
            }
        }else{
            throw new FileNotFoundException("No categories found in categories folder");
        }
        return catList;

    }
    
    /*
     * Reads the questions from a file and creates a question list
     */
    private List<Question> makeQuestions(File catFile){
        List<Question> QList = new ArrayList<Question>();
        try {
            BufferedReader br = new BufferedReader(new FileReader(catFile));
            String line;

            while ((line = br.readLine()) != null) {
               Question Q = new Question(line);

                QList.add(Q);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return QList;
    }
    
    public List<Category> getGameCategories() {
    	return _questionBoard.getGameCategories();
    }
    
    /*
     * Reset game categories then set it to the list provided if there
     * is not a game saved
     */
    public void setGameCategories(List<Category> list) {
    	if(_save.checkActiveGame()) {
    	    _questionBoard.loadGameCategories();
        }else {
    	    _save.startGame();
            _questionBoard.setGameCategories(list);
        }

    }
    
    public List<Category> getCategoryList() {
    	return _questionBoard.getBoard();
    }
    
    public void setPracticeCategory(Category c) {
    	_practiceCategory =c;
    }
    
    public void setCurrentGameQuestion(Question q) {
    	_currentGameQuestion =q;
    }
    
    public Question getCurrentGameQuestion() {
    	return _currentGameQuestion;
    }
    
    public Question getRandomNZQuestion() {
    	return _practiceCategory.getQuestion(_random.nextInt(_practiceCategory.getQuestionCount()));  	
    }
    
    /*
     * Returns a random international question that is different from the one provided
     */
    public Question getRandomInternationalQuestion(Question previous) {
    	Question newQuestion = _internationalCategory.getQuestion(_random.nextInt(_internationalCategory.getQuestionCount()));
    	while (newQuestion.equals(previous) && _internationalCategory.getQuestionCount()>1) {
    		newQuestion = _internationalCategory.getQuestion(_random.nextInt(_internationalCategory.getQuestionCount()));
    	}
    	return newQuestion;	
    }
    
    /*
     * Checks if the game is complete
     */
    public boolean isGameComplete() {
    	for (Category c : _questionBoard.getGameCategories()) {
    		if (!c.isComplete()) {
    			return false;
    		}
    	}
    	return true;
    }
    
    public void updateNumCatsDone(){
        _questionBoard.updateCatsDone();
    }
    
    /*
     * Check if answer is correct, ignores capitalisation and leading and trailing space
     */
    public boolean checkAnswer(Question q, String usrAns) {
    	usrAns=usrAns.toLowerCase().trim();
    	
    	for (String ans : q.getAnswer()) {
    		String actualAns=ans;
    		actualAns=actualAns.toLowerCase().trim();	
    		if (actualAns.equals(usrAns)) {
    			return true;
    		}
    	} 	
    	return false;
    }
    
    public void resetGame() {
    	_questionBoard.resetGame();  	
    }
}
