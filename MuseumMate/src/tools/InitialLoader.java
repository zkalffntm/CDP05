package tools;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import datatype.Area;
import datatype.Exhibition;
import datatype.Link;
import datatype.Museum;
import datatype.Recommendation;
import datatype.User;
import datatype.Visited;

public class InitialLoader extends Thread
{
	private Handler handler;
	
	public InitialLoader(Handler handler)
	{ this.handler = handler; }
	
	@Override
	public void run()
	{
		// Museum
		Museum museum = new Museum();
		museum.select();
		museum.setNoticeUrl("http://www.swgumho.es.kr/upload"
			+ "/2016/04/19/3f6b73e553b874e14bdec8aabe8a8208.png");
		museum.setName("���� ���ð�");
		museum.setMajor(0);
		
		// Exhibitions and Links
		int[] imageIds = new int[] {0, 0, 0};
		List<Exhibition> exList = new ArrayList<Exhibition>();
		for(int i = 0; i < 7; i++)
			exList.add(new Exhibition(0, "����"+ (i+1), "�۰�", "����������", "�ڼ��Ѽ����� "
					+ "�ڼ��Ѽ����� �ڼ��Ѽ����� \n�ڼ��Ѽ����� �ڼ��Ѽ����� \n�ڼ��Ѽ����� �ڼ��Ѽ����� �ڼ���"
					+ "������ \n�ڼ��Ѽ����� �ڼ��Ѽ����� �ڼ��Ѽ����� �ڼ��Ѽ����� \n", imageIds));
		museum.setExhibitionList(exList);
		
		List<Area> areaList = new ArrayList<Area>();
		Area testArea1 = new Area(0, "��1");
		Area testArea2 = new Area(0, "��2");
		exList.get(0).place(testArea1, 32);
		exList.get(1).place(testArea1, 20);
		exList.get(2).place(testArea1, 12);
		exList.get(3).place(testArea1, 16);
		exList.get(4).place(testArea1, 18);
		exList.get(5).place(testArea2, 12);
		exList.get(6).place(testArea2, 35);
		new Link(testArea2, 20).place(testArea1, 17);
		new Link(testArea1, 17).place(testArea2, 20);
		areaList.add(testArea1);
		areaList.add(testArea2);
		museum.setAreaList(areaList);
		
		// Recommendations
		List<Recommendation> reclist = new ArrayList<Recommendation>();
		reclist.add(new Recommendation(0, "����1", "��������", new int[]{1, 2, 3, 4, 5, 6, 7}));
		reclist.add(new Recommendation(0, "����2", "��������", new int[]{3, 5, 7}));
		reclist.add(new Recommendation(0, "����3", "��������", new int[]{1, 2, 4, 7}));
		museum.setRecommendationList(reclist);

		// Visiteds
		List<Visited> visitedList = new ArrayList<Visited>();
		visitedList.add(new Visited(0, "���ð�1"));
		visitedList.add(new Visited(0, "���ð�2"));
		visitedList.add(new Visited(0, "���ð�3"));
		visitedList.add(new Visited(0, "���ð�4"));
		User.getCurrentUser().setVisitedList(visitedList);
		
		try { Thread.sleep(2000); } catch(Exception e) {}
		
		handler.sendEmptyMessage(CustomMsg.SUCCESS);
	}
}
