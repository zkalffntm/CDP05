package tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;
import datatype.Area;
import datatype.CentralServer;
import datatype.Exhibition;
import datatype.Link;
import datatype.Museum;
import datatype.Node;
import datatype.Recommendation;
import datatype.User;
import datatype.Visited;
import pathsearch.Dijkstra;

public class InitialLoader extends Thread
{
	private Handler handler;
	
	public InitialLoader(Handler handler)
	{ this.handler = handler; }
	
	@Override
	public void run()
	{
		try
		{
			// Connect Central
			JSONTransactionClient centralClient = 
					JSONTransactionClient.getClient(CentralServer.IP, CentralServer.PORT);
			
			// Provider List
			String ip = "psjpi.iptime.org";
			int port = 9696;
			/*
			JSONArray providerData = centralClient.request(PacketLiteral.REQ_PROVIDER_LIST, 
					new Double[] {1.1, 2.2}).getJSONArray(PacketLiteral.VALUE);
			for(int i = 0; i < providerData.length(); i++)
			{
				JSONArray providerElement = providerData.getJSONArray(i);
				String name = providerElement.getString(0);
				ip 	= providerElement.getInt(1) + ".";
				ip += providerElement.getInt(2) + ".";
				ip += providerElement.getInt(3) + ".";
				ip += providerElement.getInt(4);
				port 	= providerElement.getInt(5);
				int major 	= providerElement.getInt(6);
				
				Log.i("test", major + " : " + name + " - " + ip + ":" + port);
			}*/

			ip = "psjpi.iptime.org";
			port = 9696;
			
			// Visiteds List
			JSONArray visitedData =  centralClient.request(PacketLiteral.REQ_VISITED, 
					User.getCurrentUser().getId()).getJSONArray(PacketLiteral.VALUE);
			List<Visited> visitedList = new ArrayList<Visited>();
			for(int i = 0; i < visitedData.length(); i++)
			{
				JSONArray visitedElement = visitedData.getJSONArray(i);
				int major = (int)visitedElement.get(0);
				String museumName = (String)visitedElement.get(1);
				visitedList.add(new Visited(major, museumName));
			}
			// test
			visitedList.add(new Visited(0, "전시관1"));
			visitedList.add(new Visited(0, "전시관2"));
			visitedList.add(new Visited(0, "전시관3"));
			visitedList.add(new Visited(0, "전시관4"));
			User.getCurrentUser().setVisitedList(visitedList);
			
			// Connect Admin
			JSONTransactionClient adminClient = 
					JSONTransactionClient.getClient(ip, port);
			
			// Notice URL
			String noticeUrl =  adminClient.request(PacketLiteral.REQ_NOTICE, null).
					getString(PacketLiteral.VALUE);
			
			//Log.i("test", noticeUrl);
			
			long lastUpdate =  adminClient.request(PacketLiteral.REQ_UPDATE_DATE, null).
					getLong(PacketLiteral.VALUE);
			
			JSONArray fundamentalData = adminClient.request(PacketLiteral.REQ_UPDATE, null).
					getJSONArray(PacketLiteral.VALUE);
			
			// Area Data
			List<Area> areaList = new ArrayList<Area>();
			JSONArray areaData = fundamentalData.getJSONArray(0);
			for(int i = 0; i < areaData.length(); i++)
			{
				JSONArray areaElement = areaData.getJSONArray(i);
				int num 	= areaElement.getInt(0);
				String name = areaElement.getString(1);
				int row	 	= areaElement.getInt(2);
				int col 	= areaElement.getInt(3);
				areaList.add(new Area(num, name, row, col));
			}
			
			// Exhibition Data
			List<Node> nodeList = new ArrayList<Node>();
			List<Exhibition> exhibitionList = new ArrayList<Exhibition>();
			JSONArray exhibitionData = fundamentalData.getJSONArray(1);
			for(int i = 0; i < exhibitionData.length(); i++)
			{
				JSONArray exhibitionElement = exhibitionData.getJSONArray(i);
				int id				= exhibitionElement.getInt(0);
				String name 		= exhibitionElement.getString(1);
				String author 		= exhibitionElement.getString(2);
				String summary 		= exhibitionElement.getString(3);
				String description 	= exhibitionElement.getString(4);
				int[] images		= new int[exhibitionElement.length() - 5];
				for(int j = 5; j < exhibitionElement.length(); j++)
					images[j-5] = exhibitionElement.getInt(j);
				
				Exhibition instance = new Exhibition(id, name, author, summary, description, images);
				nodeList.add(instance);
				exhibitionList.add(instance);
			}

			// Location Data
			JSONArray locationData = fundamentalData.getJSONArray(2);
			for(int i = 0; i < locationData.length(); i++)
			{
				JSONArray locationElement = locationData.getJSONArray(i);
				int blockNumber 		= locationElement.getInt(0);
				int exhibitionNumber 	= locationElement.getInt(1);
				int areaNumber 			= locationElement.getInt(2);
				
				if(exhibitionNumber != 0)
				{
					exhibitionList.get(exhibitionNumber - 1).
						place(areaList.get(areaNumber - 1), blockNumber - 1);
				}
			}

			// Link Data
			JSONArray linkData = fundamentalData.getJSONArray(3);
			for(int i = 0; i < linkData.length(); i++)
			{
				JSONArray linkElement = linkData.getJSONArray(i);
				int blockNumberSrc 	= linkElement.getInt(1);
				int blockNumberDst	= linkElement.getInt(2);
				int areaNumberSrc 	= linkElement.getInt(3);
				int areaNumberDst 	= linkElement.getInt(4);

				Link link = new Link(areaList.get(areaNumberDst - 1), blockNumberDst - 1);
				Area area = areaList.get(areaNumberSrc - 1);
				link.place(area, blockNumberSrc - 1);
				nodeList.add(link);
			}
			
			// Node Data
			JSONArray nodeData = fundamentalData.getJSONArray(4);
			for(int i = 0; i < nodeData.length(); i++)
			{
				JSONArray nodeElement = nodeData.getJSONArray(i);
				int blockNumber = nodeElement.getInt(0);
				int areaNumber	= nodeElement.getInt(1);

				Area area = areaList.get(areaNumber - 1);
				int columnCount = area.getColumnCount();
				Node found = area.getNode((blockNumber - 1) / columnCount,
						(blockNumber - 1) % columnCount);
				
				if(found == null)
				{
					Node node = new Node();
					nodeList.add(node);
					node.place(areaList.get(areaNumber - 1), blockNumber - 1);
				}
			}
			
			// Recommendation Data
			List<Recommendation> recommendationList = new ArrayList<Recommendation>();
			JSONArray recommendationData = fundamentalData.getJSONArray(5);
			for(int i = 0; i < recommendationData.length(); i++)
			{
				JSONArray recommendationElement = recommendationData.getJSONArray(i);
				int number 	= recommendationElement.getInt(0);
				String name	= recommendationElement.getString(1);
				int[] route	= new int[recommendationElement.length() - 2];
				for(int j = 2; j < recommendationElement.length(); j++)
					route[j-2] = recommendationElement.getInt(j);
				
				recommendationList.add(new Recommendation(number, name, "", route));
			}

			// Museum
			Museum museum = new Museum();
			museum.select();
			museum.setIP(ip);
			museum.setPort(port);
			museum.setName("샘플 전시관");
			museum.setMajor(1);
			museum.setNoticeUrl(noticeUrl);
			museum.setExhibitionList(exhibitionList);
			museum.setAreaList(areaList);
			museum.setNodeList(nodeList);
			museum.setRecommendationList(recommendationList);
			Dijkstra.makeGraph(areaList, nodeList);
			
			try { Thread.sleep(500); } catch(Exception e) {}
			
			handler.sendEmptyMessage(CustomMsg.SUCCESS);
			
		}
		catch(Exception e)
		{
			Log.e("test", "ERROR", e);
			System.runFinalizersOnExit(true);
			System.exit(0); 
		} 
	}
	
	private void testCode()
	{
		// Museum
		Museum museum = new Museum();
		museum.select();
		museum.setNoticeUrl("http://www.swgumho.es.kr/upload"
			+ "/2016/04/19/3f6b73e553b874e14bdec8aabe8a8208.png");
		museum.setName("샘플 전시관");
		museum.setMajor(0);
		
		// Exhibitions and Links
		int[] imageIds = new int[] {0, 0, 0};
		List<Exhibition> exList = new ArrayList<Exhibition>();
		for(int i = 0; i < 7; i++)
			exList.add(new Exhibition(0, "샘플"+ (i+1), "작가", "간략설명내용", "자세한설명내용 "
					+ "자세한설명내용 자세한설명내용 \n자세한설명내용 자세한설명내용 \n자세한설명내용 자세한설명내용 자세한"
					+ "설명내용 \n자세한설명내용 자세한설명내용 자세한설명내용 자세한설명내용 \n", imageIds));
		museum.setExhibitionList(exList);
		
		List<Area> areaList = new ArrayList<Area>();
		Area testArea1 = new Area(0, "방1", 7, 10);
		Area testArea2 = new Area(0, "방2", 7, 10);
		exList.get(0).place(testArea1, 64);
		exList.get(1).place(testArea1, 40);
		exList.get(2).place(testArea1, 24);
		exList.get(3).place(testArea1, 32);
		exList.get(4).place(testArea1, 36);
		exList.get(5).place(testArea2, 24);
		exList.get(6).place(testArea2, 69);
		//Link a = new Link();
		//Link b = new Link();
		//Link.setPair(a, b);
		//a.place(testArea1, 34);
		//b.place(testArea2, 40);
		areaList.add(testArea1);
		areaList.add(testArea2);
		museum.setAreaList(areaList);
		
		// Recommendations
		List<Recommendation> reclist = new ArrayList<Recommendation>();
		reclist.add(new Recommendation(0, "샘플1", "컨셉설명", new int[]{1, 2, 3, 4, 5, 6, 7}));
		reclist.add(new Recommendation(0, "샘플2", "컨셉설명", new int[]{3, 5, 7}));
		reclist.add(new Recommendation(0, "샘플3", "컨셉설명", new int[]{1, 2, 4, 7}));
		museum.setRecommendationList(reclist);

		// Visiteds
		List<Visited> visitedList = new ArrayList<Visited>();
		visitedList.add(new Visited(0, "전시관1"));
		visitedList.add(new Visited(0, "전시관2"));
		visitedList.add(new Visited(0, "전시관3"));
		visitedList.add(new Visited(0, "전시관4"));
		User.getCurrentUser().setVisitedList(visitedList);
		
		try { Thread.sleep(2000); } catch(Exception e) {}
		
		handler.sendEmptyMessage(CustomMsg.SUCCESS);
	}
}
