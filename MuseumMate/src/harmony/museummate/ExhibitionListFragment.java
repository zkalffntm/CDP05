package harmony.museummate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import datatype.Area;
import datatype.Museum;


public class ExhibitionListFragment extends Fragment
{
	// Singleton /////////////////////////////////////////////////////////////
	private static ExhibitionListFragment self;								//
	public static ExhibitionListFragment getInstance()						//
	{ if(self == null) self = new ExhibitionListFragment(); return self; }	//
	private ExhibitionListFragment() {}										//
	//////////////////////////////////////////////////////////////////////////
	
    private TabLayout tabLayout;
    private ViewPager viewPager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
    	View v = inflater.inflate(R.layout.exhibition, container, false);
    	
        // ****TEMP**** Set TabLayout
        tabLayout = (TabLayout)v.findViewById(R.id.tab_layout);
        viewPager = (ViewPager)v.findViewById(R.id.viewpager);
        
        RecyclerViewPagerAdapter adapter = 
        		new RecyclerViewPagerAdapter(getActivity().getSupportFragmentManager());
        List<Area> areaList = Museum.getSelectedMuseum().getAreaList();
        
        for(Area e : areaList)
        	adapter.addFrag(new ExhibitionListInnerFragment(e), e.getName());
        
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    	return v;
    }
    
    class RecyclerViewPagerAdapter extends FragmentPagerAdapter
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
        
        public void addFrag(Fragment fragment, String title)
        {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position)
        { return mFragmentTitleList.get(position); }
    }
}
