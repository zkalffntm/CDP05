package harmony.museummate;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class RecyclerViewPagerAdapter extends FragmentPagerAdapter
{
	private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public RecyclerViewPagerAdapter(FragmentManager manager)
    { super(manager); }

    @Override
    public Fragment getItem(int position)
    { return mFragmentList.get(position); }

    @Override
    public int getCount()
    { return mFragmentList.size(); }

    public void addArea()
    {
    	
    }
    
    public void addFrag(Fragment fragment, String title)
    {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position)
    { return mFragmentTitleList.get(position); }
    
}