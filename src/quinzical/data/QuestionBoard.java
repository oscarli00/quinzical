package quinzical.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class QuestionBoard {
	private static final int[] QUESTION_VALUES = new int[] {200,400,600,800,1000};
	private Save _save;
	private List<Category> _board;
	private List<Category> _gameCategories;
	
	public QuestionBoard(List<Category> board, Save save) {
		_board=board;
		_save=save;
	}
	
	public List<Category> getBoard() {
		return _board;
	}

	/*
	 * Checks the number of game categories completed
	 */
	public void updateCatsDone() {
		_save.setCatsDone(0);
		if (_gameCategories != null) {
			for (Category category : _gameCategories) {
				if (category.isComplete()) {
					_save.incrementNumCatsDone();
				}
			}
		}
	}

	/*
	 * Load in the game categories from the save
	 */
	public void loadGameCategories(){
		_gameCategories = new ArrayList<Category>();
		for(Category category : _board){
			if(_save.isCategoryActive(category.getId())){
				Category newCat = category.loadCategory(_save);
				_gameCategories.add(newCat);
			}

		}
	}

	/*
	 * Generates a set of 5 questions for each category provided and sets them as the current
	 * game categories
	 */
	public void setGameCategories(List<Category> categories) {
		_gameCategories=new ArrayList<Category>();
		Random r = new Random();
		for (Category c : categories) {
			Set<Integer> qIndices = new HashSet<Integer>();
			
			while (qIndices.size()<5) {
				qIndices.add(r.nextInt(c.getQuestionCount()));
			}
			
			List<Question> questions = new ArrayList<Question>();			
			int valueIndex=0;
			for (Integer j : qIndices) {
				Question q = c.getQuestion(j);
				q.setValue(QUESTION_VALUES[valueIndex]);
				_save.addActiveQuestion(q.getId());
				questions.add(q);
				valueIndex++;
			}
			_gameCategories.add(new Category(c.toString(),questions));
		}
	}
	
	public List<Category> getGameCategories() {
    	return _gameCategories;
    }
	
	public void resetGame() {
		for (Category c : _board) {
    		c.resetAnswerState();
    	}
		_gameCategories=null;
	}
}
