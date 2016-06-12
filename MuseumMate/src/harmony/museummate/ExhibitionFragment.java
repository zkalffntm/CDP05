package harmony.museummate;

import android.support.v4.app.Fragment;

import java.util.List;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import datatype.Area;
import datatype.Museum;

public class ExhibitionFragment extends Fragment
{
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
        	adapter.addFrag(new RecyclerFragment(e), e.getName());
        
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    	return v;
    }
}
