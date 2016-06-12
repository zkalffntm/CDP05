package tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;

/**
 * Some custom function tools on Bitmap
 * 
 * @author Kyuho
 *
 */
public class BitmapTool
{
	/**
	 * 
	 * @param bitmap			Source(To be gerbage collected)
	 * @param backgroundColor
	 * @return					Completely new instance from source
	 */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int backgroundColor)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        final int color = backgroundColor;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, width, height);
        final RectF rectF = new RectF(rect);
        final float roundPx = (width > height) ? width : height;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
}