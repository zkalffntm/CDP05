package harmony.museummate;

import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import datatype.Exhibition;

public class DescriptionDialog extends Dialog
{
	private static final int THRESHOULD_FLIP = 20;
	
	private static DescriptionDialog self;
	private static Exhibition currentExhibition;
	private static boolean voiceOn;
	
	public static void showExhibitionDialog(Context context, Exhibition exhibition)
	{
		if(self == null) self = new DescriptionDialog(context);
		if(currentExhibition == null || !currentExhibition.equals(exhibition))
			self.setExhibition(exhibition);
		if(!self.isShowing()) self.show();
	}
	
	private ViewPager pager;
    private CustomPagerAdapter pagerAdapter;
	private TextView textName;
	private TextView textAuthor;
	private TextView textSummary;
	private TextView textDescription;
	private ImageView exitButton;
	private ImageView voiceButton;
	
	private TextToSpeech tts;
	private String currentTTSString;
	private boolean ttsLoaded;
	private boolean ttsPended;
	
	private DescriptionDialog(Context context)
	{
		super(context);

		// Setup Views
		setContentView(R.layout.description_dialog);
		pager = (ViewPager)findViewById(R.id.view_pager);
		textName = (TextView)findViewById(R.id.txt_name);
		textAuthor = (TextView)findViewById(R.id.txt_author);
		textSummary = (TextView)findViewById(R.id.txt_summary);
		textDescription = (TextView)findViewById(R.id.txt_description);
		exitButton = (ImageView)findViewById(R.id.exit_button);
		voiceButton = (ImageView)findViewById(R.id.voice_button);
		
		// Set TTS
		tts = new TextToSpeech(context, ttsListener);
		
		// Set Listeners
		exitButton.setOnClickListener(new View.OnClickListener()
		{ public void onClick(View v) { self.dismiss(); } });
		
		voiceButton.setOnClickListener(new View.OnClickListener()
		{ public void onClick(View v) { setVoiceOn(!voiceOn); } });
	}
    
	private void setExhibition(Exhibition exhibition)
	{
		currentExhibition = exhibition;
		
		// To do load voice on setting and setVoiceOn()
		currentTTSString = exhibition.getName() + "." + exhibition.getAuthor() + "." +
				exhibition.getSummary() + "." + exhibition.getDescription();
		setVoiceOn(true);
		
		// Update Texts
		textName.setText(exhibition.getName());
		textAuthor.setText(exhibition.getAuthor());
		textSummary.setText(exhibition.getSummary());
		textDescription.setText(exhibition.getDescription());

		// To do : dynamic load
		// test code
		pager.removeAllViews();
		ImageView image1 = new ImageView(getContext());
		image1.setImageResource(R.drawable.exhibition_sample);
		
		ImageView image2 = new ImageView(getContext());
		image2.setImageResource(R.drawable.exhibition_sample);

		
		// Set adapter
		pagerAdapter = new CustomPagerAdapter(getContext());
		pagerAdapter.addView(image1);
		pagerAdapter.addView(image2);
		pager.setAdapter(pagerAdapter);
	}
	
	public static void setVoiceOn(boolean b)
	{
		// To do if voiceOn : Stop voice
		voiceOn = b;
		self.voiceButton.setImageResource(voiceOn ? R.drawable.voice_on : R.drawable.voice_off);
		if(voiceOn) 
		{
			if(self.ttsLoaded) self.tts.speak(self.currentTTSString, TextToSpeech.QUEUE_FLUSH, null);
			else self.ttsPended = true;
		}
		else self.tts.stop();
	}
	
	private OnInitListener ttsListener = new OnInitListener()
	{
		@Override
		public void onInit(int status)
		{
            if(status != TextToSpeech.ERROR)
                tts.setLanguage(Locale.KOREAN);
            
            if(ttsPended)
            {
            	ttsPended = false;
            	tts.speak(self.currentTTSString, TextToSpeech.QUEUE_FLUSH, null);
            }
            
            ttsLoaded = true;
		}
	};
}
