package harmony.museummate;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

class MapOverlay extends View
{
	public MapOverlay(Context context)
	{ super(context); }
	
	public MapOverlay(Context context, AttributeSet attributeSet)
	{ super(context, attributeSet); }

	protected void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		
		Paint paint = new Paint();
		paint.setColor(Color.BLUE);
		canvas.drawRect(50, 100, 50+100, 100+100, paint);
		
		/*
		Paint paint3 = new Paint();
		paint3.setColor(Color.BLACK);
		
		canvas.drawLine(200, 500, 500, 600, paint3);
		*/
	}	
}