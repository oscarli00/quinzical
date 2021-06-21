package quinzical.data;

public class Question {

	private static int _count=1;
	private final int _id;
	private int _value;
	private String _question;
	private String[] _answer;
	private String _type;
	private boolean _answered;
	
	public Question(String questionInfo) {
		_id=_count++;
		_question=questionInfo.split("\\|")[0];
		_type=questionInfo.split("\\|")[1];
		String answerText=questionInfo.split("\\|")[2];
		_answer=answerText.split("/");
		_answered=false;
	}
	
	public int getValue() {
		return _value;
	}
	
	public void setValue(int value) {
		_value=value;
	}
	
	public String getQuestionString() {
		return _question;
	}
	
	public String[] getAnswer() {
		return _answer;
	}
	
	public boolean isAnswered() {
		return _answered;
	}
	
	public void setAnswered(boolean b) {
		_answered=b;
	}

	public String getType(){
		return _type;
	}

	public int getId(){
		return _id;
	}

}
