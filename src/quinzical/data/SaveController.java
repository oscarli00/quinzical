package quinzical.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import quinzical.modules.leaderboard.User;

public class SaveController {

	private File _saveFile;
	private File _nzScoresFile;
	private File _internationalScoresFile;

	public SaveController() {
		String currentPath = System.getProperty("user.dir");
		File dir = new File(currentPath + "/saves");
		dir.mkdirs();

		_saveFile = new File(dir, "save");
		if (!_saveFile.exists()) {
			try {
				_saveFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			reset();
		}

		_nzScoresFile = new File(dir, "nzScores");
		if (!_nzScoresFile.exists()) {
			try {
				_nzScoresFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		_internationalScoresFile = new File(dir, "internationalScores");
		if (!_internationalScoresFile.exists()) {
			try {
				_internationalScoresFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Saving the game data to text files
	 */
	public void saveGame(Save save) {
		try {
			FileWriter scoreWriter = new FileWriter(_saveFile);

			scoreWriter.write(String.valueOf(save.getScore()) + "\n");
			scoreWriter.append(String.valueOf(save.getNumCatsDone()) + "\n");

			// Saving active categories
			List<Integer> activeCategoriesArray = save.getActiveCategories();
			String activeCategories = "";
			for (int i : activeCategoriesArray) {
				activeCategories = activeCategories + i + " ";
			}
			scoreWriter.append(activeCategories + "\n");

			// Saving active questions
			List<Integer> activeQuestionsArray = save.getActiveQuestions();
			String activeQuestions = "";
			for (int i : activeQuestionsArray) {
				activeQuestions = activeQuestions + i + " ";
			}
			scoreWriter.append(activeQuestions + "\n");

			// Saving completed questions
			List<Integer> questionArray = save.getQuestionsDone();
			String questionsDone = "";
			for (int i : questionArray) {
				questionsDone = questionsDone + i + " ";
			}
			scoreWriter.append(questionsDone + "\n");

			// Saving game state
			if (save.checkActiveGame()) {
				scoreWriter.append(1 + "\n");
			} else {
				scoreWriter.append(0 + "\n");
			}

			// Saving international unlock state
			if (save.intUnlocked()) {
				scoreWriter.append(1 + "\n");
			} else {
				scoreWriter.append(0 + "\n");
			}

			scoreWriter.close();

			// Saving NZ scores
			scoreWriter = new FileWriter(_nzScoresFile);
			scoreWriter.write("");
			for (User u : save.getNZScores()) {
				scoreWriter.append(u.getName() + "|" + u.getScore() + "\n");
			}
			scoreWriter.close();

			// Saving international scores
			scoreWriter = new FileWriter(_internationalScoresFile);
			scoreWriter.write("");
			for (User u : save.getInternationalScores()) {
				scoreWriter.append(u.getName() + "|" + u.getScore() + "\n");
			}
			scoreWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Loading in the game data from text files
	 */
	public Save loadGame() throws FileNotFoundException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(_saveFile));

			// Loading game score
			String score = br.readLine();
			int scoreInt = Integer.parseInt(score);

			// Loading number of categories done
			String numCatsDone = br.readLine();
			int numCatsDoneInt = Integer.parseInt(numCatsDone);

			// Loading active categories
			String activeCategories = br.readLine();
			List<Integer> activeCategoriesArray = parseString(activeCategories);

			// Loading active questions
			String activeQuestions = br.readLine();
			List<Integer> activeQuestionsArray = parseString(activeQuestions);

			// Loading completed questions
			String questionsDone = br.readLine();
			List<Integer> questionArray = parseString(questionsDone);

			// Loading game state
			String activeGame = br.readLine();
			int activeGameInt = Integer.parseInt(activeGame);

			// Loading international unlock state
			String intUnlockedString = br.readLine();
			int intUnlocked = Integer.parseInt(intUnlockedString);
			br.close();

			// Loading NZ scores
			br = new BufferedReader(new FileReader(_nzScoresFile));
			List<User> nz = new ArrayList<User>();
			String line = br.readLine();
			while (line != null) {
				nz.add(new User(line.split("\\|")[0], Integer.parseInt(line.split("\\|")[1])));
				line = br.readLine();
			}
			br.close();

			// Loading international scores
			br = new BufferedReader(new FileReader(_internationalScoresFile));
			List<User> international = new ArrayList<User>();
			line = br.readLine();
			while (line != null) {
				international.add(new User(line.split("\\|")[0], Integer.parseInt(line.split("\\|")[1])));
				line = br.readLine();
			}
			br.close();

			return Save.getInstance(scoreInt, numCatsDoneInt, activeCategoriesArray, activeQuestionsArray,
					questionArray, activeGameInt, nz, international, intUnlocked);
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw (new FileNotFoundException("The save file could not be read"));
	}

	/*
	 * Helper method to convert a string into an int array.
	 */
	private List<Integer> parseString(String string) {
		String[] stringArray = string.split(" ");

		List<Integer> intArray = new ArrayList<Integer>();
		for (int i = 0; i < stringArray.length; i++) {
			intArray.add(Integer.parseInt(stringArray[i]));
		}
		return intArray;
	}

	/*
	 * Resets all game data (locks the international section)
	 */
	public void reset() {
		try {
			FileWriter saveWriter = new FileWriter(_saveFile, false);
			saveWriter.write("0\n0\n0 0\n0 0\n0 0\n0\n0");
			saveWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Starts a new game (international section stays unlocked)
	 */
	public void newGame() {
		try {
			FileWriter saveWriter = new FileWriter(_saveFile, false);
			saveWriter.write("0\n0\n0 0\n0 0\n0 0\n0\n1");
			saveWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}