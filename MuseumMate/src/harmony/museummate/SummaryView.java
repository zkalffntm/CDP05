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

public class SummaryView extends ToolTipView
{
	private ExhibitionView targetView;
	private ViewGroup contentHolder;
	private ImageView exitButton;
	private Button removeButton;
	
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
		targetView = (ExhibitionView)view;
		aboveTarget = view.getY() > getY();
		gapX = getX() - (targetView.getX() + targetView.getWidth() / 2);
		gapY = getY() - (targetView.getY() + (aboveTarget ? 0.0f : targetView.getHeight()));
		
    	// Set Views
    	TextView textViewName = (TextView)findViewById(R.id.name);
    	textViewName.setText(targetView.getExhibition().getName());
    	TextView textViewSummary = (TextView)findViewById(R.id.summary);
    	textViewSummary.setText(targetView.getExhibition().getSummary());
    	// To do Image Load
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
    		Toast.makeText(getContext(), "테스트 : 경로에서 제외", Toast.LENGTH_SHORT).show();
    	}
    }
	
	public ExhibitionView getExhibitionView()
	{ return targetView; }
	
    public void addRemoveButton()
    {
    	removeButton = new Button(getContext());
    	LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
    			LayoutParams.WRAP_CONTENT, 90);
    	//params.setMargins(5, 5, 5, 5);
    	removeButton.setLayoutParams(params);
    	removeButton.setPadding(0, -15, 0, -15);
    	removeButton.setText(R.string.remove_from_route);
    	removeButton.setTextColor(Color.WHITE);
    	removeButton.setTextSize(14);
    	removeButton.setBackgroundResource(R.drawable.button_border);
    	removeButton.setOnClickListener(this);
    	((LinearLayout)findViewById(R.id.additive_layout)).addView(removeButton);
    }
}
