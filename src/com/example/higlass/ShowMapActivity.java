package com.example.higlass;

import java.util.Locale;

import com.google.android.glass.touchpad.Gesture;
import com.google.android.glass.touchpad.GestureDetector;
import com.google.android.glass.touchpad.GestureDetector.BaseListener;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

public class ShowMapActivity extends Activity implements OnInitListener{
	
	/**
	 * The image that displays the map of the museum
	 */
	ImageView mImg;
	
	/**
	 * The gesture detector (displays the options menu when the user taps)
	 */
	private GestureDetector mGestureDetector;
	
	/**
	 * The text to speech service (used to read the location out loud to the user)
	 */
	private TextToSpeech tts;
	
	/**
	 * When the activity is created, display the map
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = getLayoutInflater().inflate(R.layout.activity_map, null);
		mGestureDetector = createGestureDetector(this);
		tts = new TextToSpeech(this,this);
		setContentView(v);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return false;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}
	
	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {
		if(mGestureDetector != null){
			return mGestureDetector.onMotionEvent(event);
		}
		return false;
	}
	
	/**
	 * On a tap, display the options menu
	 */
	private GestureDetector createGestureDetector(Context context){
		GestureDetector gestureDetector = new GestureDetector(context);
		gestureDetector.setBaseListener( new BaseListener() {
			@Override
			public boolean onGesture(Gesture gesture) {
				if(gesture == Gesture.TAP){
					openOptionsMenu();
					return true;
				}
				return false;
			}
		});
		return gestureDetector;
	}
	
	@Override
	protected void onDestroy() {
		if(tts != null){
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA
                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }
            speakOut("You are in the Impressionism exhibit");
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
	}
	
	private void speakOut(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
