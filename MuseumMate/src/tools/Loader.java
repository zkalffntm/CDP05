package tools;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import datatype.Area;
import datatype.Exhibition;
import datatype.Museum;
import datatype.Recommandation;

public class Loader extends Thread
{
	private Handler handler;
	
	public Loader(Handler handler)
	{
		this.handler = handler;
	}
	
	@Override
	public void run()
	{
		// 테스트
		Museum museum = new Museum();
		museum.select();
		museum.setNoticeUrl("http://www.swgumho.es.kr/upload"
			+ "/2016/04/19/3f6b73e553b874e14bdec8aabe8a8208.png");
		museum.setName("전시회 이름");
		museum.setMajor(1);
		
		int[] imageIds = new int[] {1, 2, 3};
		Exhibition testEx1 = new Exhibition(1, "샘플1", "작가", "간략설명내용", "자세한설명내용", imageIds);
		
		List<Exhibition> exList = new ArrayList<Exhibition>();
		exList.add(testEx1);
		museum.setExhibitionList(exList);
		

		List<Area> areaList = new ArrayList<Area>();
		Area testArea = new Area(1, "");
		testEx1.place(testArea, 32);
		areaList.add(testArea);
		museum.setAreaList(areaList);
		
		List<Recommandation> reclist = new ArrayList<Recommandation>();
		reclist.add(new Recommandation(1, "샘플", new int[]{1, 2}));
		museum.setRecommandationList(reclist);

		try { Thread.sleep(2000); } catch(Exception e) {}
		
		handler.sendEmptyMessage(CustomMsg.SUCCESS);
	}
}
