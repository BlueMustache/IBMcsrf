package com.nostalgiarunner.watson.stt;

import java.io.File;

public class Send {
	
	public static void main(String[] args) {
		SpeechToText.submit(new File("file.wav"));
		//System.out.println(transcript);
	}

}
