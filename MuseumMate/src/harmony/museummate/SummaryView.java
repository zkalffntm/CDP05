package harmony.museummate;

import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipView;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import datatype.Exhibition;
import tools.DynamicLoader;

public class SummaryView extends ToolTipView
{
	private View targetView;
	private ViewGroup contentHolder;
	private ImageView exhibitionImage;
	private ImageView exitButton;
	private ImageView removeButton;
	
	private Exhibition exhibition;
	
	private boolean aboveTarget;
	private float gapX, gapY;
	
    public SummaryView(final Context context)
    {
    	super(context);
    	
    	// Inflate layout
        contentHolder = (ViewGroup)findViewById(com.nhaarman.supertooltips.R.id.tooltip_contentholder);
    	LayoutInflater inflater = (LayoutInflater)context.
    			getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	inflater.inflate(R.layout.summary_view, contentHolder);
    	exhibitionImage = (ImageView)findViewById(R.id.photo);
    	
    	// Exit Button
    	exitButton = new ImageView(context);
    	exitButton.setImageResource(R.drawable.exit);
    	exitButton.setOnClickListener(this);
    	FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(60, 60);
    	params.gravity = Gravity.TOP | Gravity.RIGHT;
    	params.rightMargin = 10;
    	params.topMargin = 10;
    	contentHolder.addView(exitButton, params);
    }
    
    @Override
	public void setToolTip(final ToolTip toolTip, final View view)
	{
		super.setToolTip(toolTip, view);
		targetView = view;
		aboveTarget = view.getY() > getY();
		gapX = getX() - (targetView.getX() + targetView.getWidth() / 2);
		gapY = getY() - (targetView.getY() + (aboveTarget ? 0.0f : targetView.getHeight()));
	}
    
    public void setExhibition(Exhibition exhibition)
    {
    	this.exhibition = exhibition;
    	
    	// Set Views
    	TextView textViewName = (TextView)findViewById(R.id.name);
    	textViewName.setText(exhibition.getName());
    	TextView textViewSummary = (TextView)findViewById(R.id.summary);
    	textViewSummary.setText(exhibition.getSummary());
    	// To do Image Load
    	DynamicLoader.startExhibitionImage(exhibitionImage, exhibition.getImageIds()[0]);
    }
	
	public void update()
	{
		int left = (int)(targetView.getLeft() + targetView.getWidth() / 2 + gapX);
		int top = (int)(targetView.getTop() + (aboveTarget ? 0.0f : targetView.getHeight()) + gapY);
		int right = left + getWidth();
		int bottom = top + getHeight();
		setLeft(left);
		setRight(right);
		setTop(top);
		setBottom(bottom);
	}

    @Override
    public void onClick(final View view)
    {
    	if(view.equals(exitButton)) remove();
    	else if(removeButton != null && view.equals(removeButton))
    	{
    		remove();
    		DescriptionDialog.showExhibitionDialog(getContext(), exhibition);
    		//Toast.makeText(getContext(), "테스트 : 경로에서 제외", Toast.LENGTH_SHORT).show();
    	}
    }
	
    public void addRemoveButton()
    {
    	removeButton = new ImageView(getContext());
    	removeButton.setImageResource(R.drawable.remove_from_route_button);
    	removeButton.setOnClickListener(this);
    	((LinearLayout)findViewById(R.id.additive_layout)).addView(removeButton);
    }
}
