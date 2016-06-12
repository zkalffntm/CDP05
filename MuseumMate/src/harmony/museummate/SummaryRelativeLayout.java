package harmony.museummate;

import com.nhaarman.supertooltips.ToolTip;
import com.nhaarman.supertooltips.ToolTipRelativeLayout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SummaryRelativeLayout extends ToolTipRelativeLayout
{
    public SummaryRelativeLayout(final Context context)
    { super(context); }

    public SummaryRelativeLayout(final Context context, final AttributeSet attrs)
    { super(context, attrs); }

    public SummaryRelativeLayout(final Context context, final AttributeSet attrs, final int defStyle)
    { super(context, attrs, defStyle); }
    
	@Override
    public SummaryView showToolTipForView(final ToolTip toolTip, final View view)
	{
        final SummaryView toolTipView = new SummaryView(getContext());
        toolTipView.setToolTip(toolTip, view);
        addView(toolTipView);
        return toolTipView;
	}
}
