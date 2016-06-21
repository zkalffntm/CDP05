package harmony.museummate;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;

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
    private List<Integer> childIds;
    
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
        childIds = new ArrayList<Integer>();
        for(Area e : areaList)
        {
        	Fragment childFragment = new ExhibitionListInnerFragment(e);
        	childIds.add(childFragment.getId());
        	adapter.addFrag(childFragment, e.getName());
        }
        
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        
    	return v;
    }
    
    @Override
    public void onDestroyView()
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        
        for(Integer e : childIds)
            transaction.remove(fragmentManager.findFragmentById(e));
        transaction.commit();
        		
        super.onDestroyView();
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
