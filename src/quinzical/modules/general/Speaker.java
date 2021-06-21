package quinzical.modules.general;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Speaker {
	/*
	 * Method to call the festival bash command using a scheme file.
	 */
	public void speak(String phrase, int speed){
		double factor = (double)100/speed;
		File phraseFile = new File(".phrase.scm");        
		try {
			phraseFile.createNewFile();
			FileWriter writer = new FileWriter(".phrase.scm",false);
			writer.write("(voice_akl_nz_jdt_diphone)\n");
			String durationCommand = "(Parameter.set 'Duration_Stretch "+factor+")\n";
			writer.append(durationCommand);
			String speakCommand = "(SayText \""+phrase+"\")";
			writer.append(speakCommand);
			writer.close();
			ProcessBuilder pb = new ProcessBuilder( "festival", "-b", ".phrase.scm");
			pb.start();           
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * This method stops any TTS that is currently playing by killing the related processes.
	 */
	public void stopSpeaking() {		
		try {
			ProcessBuilder pb1 = new ProcessBuilder( "/bin/bash", "-c", "kill $(ps aux | grep '[f]estival' | awk '{print $2}')");
			ProcessBuilder pb2 = new ProcessBuilder( "/bin/bash", "-c", "kill $(ps aux | grep '[a]play' | awk '{print $2}')");
			Process p1 = pb1.start();
			Process p2 = pb2.start();
			p1.waitFor();
			p2.waitFor();			
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (InterruptedException e) {
			e.printStackTrace();
		}   
	}
	
}
