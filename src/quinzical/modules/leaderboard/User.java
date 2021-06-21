package quinzical.modules.leaderboard;

public class User implements Comparable<User>{
	private String _name;
	private int _score;
	
	/*
	 * This class contains a name and score to be displayed on the leaderboard. Users can
	 * be compared by their score.
	 */
	public User(String name, int score) {
		_name=name;
		_score=score;
	}
	
	public int getScore() {
		return _score;
	}
	
	public String getName() {
		return _name;
	}

	/*
	 * Allows for Collections of Users to be sorted.
	 */
	@Override
	public int compareTo(User other) {
		return _score-other.getScore();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof User)) {
			return false;
		}
		User other = (User) o;
		return other.getScore()==_score;
	}
}
