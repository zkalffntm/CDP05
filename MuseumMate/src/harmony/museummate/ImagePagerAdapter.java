package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 
 * @author Kyuho
 * @see http://codetheory.in/android-image-slideshow-using-viewpager-pageradapter/
 */
public class ImagePagerAdapter extends PagerAdapter
{
    private LayoutInflater inflater;
    private List<View> viewList;
    
    public ImagePagerAdapter(Context context)
    {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewList = new ArrayList<View>();
    }
 
    public void addView(View view)
    { viewList.add(view); }
    
    public void clearViews()
    { viewList.clear(); }
    
    @Override
    public int getCount()
    {  return viewList.size(); }
 
    @Override
    public boolean isViewFromObject(View view, Object object)
    { return view == ((ImageView) object); }
 
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }
 
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)
    { container.removeView((ImageView) object); }
}