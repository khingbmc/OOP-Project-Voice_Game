package com.vaja.voice;

import edu.cmu.sphinx.frontend.util.Microphone;

import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.util.props.ConfigurationManager;
import edu.cmu.sphinx.util.props.PropertyException;

import java.io.File;
import java.io.IOException;
import java.net.URL;


/**
 * A simple HelloWorld demo showing a simple speech application 
 * built using Sphinx-4. This application uses the Sphinx-4 endpointer,
 * which automatically segments incoming audio into utterances and silences.
 */
public class SpeechRecognize {

    /**
     * Main method for running the HelloWorld demo.
     */
	
	private String resultText;
	
	private Recognizer recognizer;
	private Microphone microphone;
	private String[] message;
	
	public SpeechRecognize() {
		message = new String[2];
		try {
            URL url;
            
            url = SpeechRecognize.class.getResource("helloworld.config.xml");
           

            System.out.println("Loading...");

            ConfigurationManager cm = new ConfigurationManager(url);

            recognizer = (Recognizer) cm.lookup("recognizer");
            microphone = (Microphone) cm.lookup("microphone");


            /* allocate the resource necessary for the recognizer */
            recognizer.allocate();

            /* the microphone will keep recording until the program exits */
	    if (microphone.startRecording()) {

		this.message[0] = "Start speaking.(dog | love | ant | sexy)";

		
		this.message[1] = "Waiting....";
		
		

                    
		    Result result = recognizer.recognize();
		    
		    if (result != null) {
		    	
			resultText = result.getBestFinalResultNoFiller();
			System.out.println(resultText);
			
		    } else {
			System.out.println("I can't hear what you said.\n");
		    }
		
	    } else {
		System.out.println("Cannot start microphone.");
		recognizer.deallocate();
		System.exit(1);
	    }
        } catch (IOException e) {
            System.err.println("Problem when loading HelloWorld: " + e);
            e.printStackTrace();
        } catch (PropertyException e) {
            System.err.println("Problem configuring HelloWorld: " + e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            System.err.println("Problem creating HelloWorld: " + e);
            e.printStackTrace();
        }
    }
	
	
	
    public String getResultText() {
		return resultText;
	}



	public String[] getMessage() {
		return message;
	}

	

	
}