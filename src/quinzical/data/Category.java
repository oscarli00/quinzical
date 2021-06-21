package quinzical.data;

import java.util.*;

public class Category implements Iterable<Question>{

	private static int _count=1;
	private final int _id;
	private String _name;
	private List<Question> _questions;
	private static final int[] QUESTION_VALUES = new int[] {200,400,600,800,1000};


	public Category(String name, List<Question> questions) {
		_id=_count++;
		_name=name;
		_questions = questions;
	}
	
	/*
	 * Loads in a list of questions from a save
	 */
	public Category loadCategory(Save save){
		List<Question> newList = new ArrayList<Question>();
		int i = 0;
		for (Question Q : _questions){
			if(save.isQuestionActive(Q.getId())){
				if(save.isQuestionDone(Q.getId())){
					Q.setAnswered(true);
				}
				newList.add(Q);
				Q.setValue(QUESTION_VALUES[i++]);
			}
		}
		return new Category(_name,newList);
	}
	
	public Question getQuestion(int index) {
		return _questions.get(index);
	}

	public int getQuestionValue(int index) {
		return _questions.get(index).getValue();
	}

	public int getQuestionCount() {
		return _questions.size();
	}

	public boolean isQuestionAnswered(int index) {
		return _questions.get(index).isAnswered();
	}
	
	public boolean isComplete() {
		for (Question q : _questions) {
			if (!q.isAnswered()) {
				return false;
			}
		}
		return true;
	}
	
	public void resetAnswerState() {
		for (Question q:_questions) {
			q.setAnswered(false);
		}
	}
	public int getId(){
		return _id;
	}

	@Override
	public String toString() {
		return _name;
	}

	@Override
	public Iterator<Question> iterator() {
		return _questions.iterator();
	}
}
