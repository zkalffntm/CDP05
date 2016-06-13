package harmony.admin.gui;

import java.awt.Image;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import harmony.admin.controller.AreaController;
import harmony.admin.controller.BlockController;
import harmony.admin.controller.ItemController;
import harmony.admin.controller.ItemImageController;
import harmony.admin.controller.NodeController;
import harmony.admin.controller.RecommendController;
import harmony.admin.controller.RecommendItemController;
import harmony.admin.controller.ShareBlockController;
import harmony.admin.gui.probremdomain.RoomData;
import harmony.admin.gui.probremdomain.RouteData;
import harmony.admin.gui.probremdomain.WorkData;
import harmony.admin.model.Area;
import harmony.admin.model.Block;
import harmony.admin.model.Item;
import harmony.admin.model.ItemImage;
import harmony.admin.model.Node;
import harmony.admin.model.Recommend;
import harmony.admin.model.RecommendItem;

public class GUI_console {

  private ImageIcon plusImg, plusImg_s;
  private ImageIcon plus2Img, plus2Img_s;
  private ImageIcon plus3Img, plus3Img_s;
  private ImageIcon backImg, backImg_s;
  private ImageIcon addWorkImg, addWorkImg_s;
  private ImageIcon tabImg, tabImg_s;
  private ImageIcon xImg, xImg_s;
  private ImageIcon addImageImg, addImageImg_s;
  private ImageIcon addImage2Img, addImage2Img_s;
  private ImageIcon addMapImg, addMapImg_s;
  private ImageIcon addRouteImg, addRouteImg_s;
  private ImageIcon inImg, inImg_s;
  private ImageIcon outImg, outImg_s;
  private ImageIcon nodeImg, nodeImg_s;
  private ImageIcon workImg, workImg_s;
  private ImageIcon saveImg, saveImg_s;
  private ImageIcon pathImg, pathImg_s;

  ///////////////// mainPage ///////////////
  private ImageIcon workPageImg, workPageImg_s;
  private ImageIcon mapPageImg, mapPageImg_s;
  private ImageIcon routePageImg, routePageImg_s;
  ///////////////////////////////////////////

  private FileOutputStream fos = null;
  private ObjectOutputStream oos = null;
  private FileInputStream fis = null;
  private ObjectInputStream ois = null;

  //////////////////////////////////////////
  private DataManagePage dmp = null;
  private MapManagePage mmp = null;
  private RouteManagePage rmp = null;

  private ArrayList<RouteData> routeList;
  private ArrayList<RoomData> roomDataList;
  private HashMap workHashMap;

  private int currentRoomNum = 0;

  private int roomCnt = 0; // ���ý� �� ����
  private int workCnt = 0; // ���ù� �� ����
  private int nodeCnt = 0; // ��� �� ����
  private int routeCnt = 0;

  private int realRoomNum = 0;
  private int realWorkNum = 0;

  private JFrame frame;
  private static GUI_console gui_console = new GUI_console();

  private GUI_console() {
    // load();

    // if(data == null)
    // data = new Data();

    /*
     * 
     */
    roomDataList = new ArrayList<RoomData>();
    routeList = new ArrayList<RouteData>();
    workHashMap = new HashMap();

    plusImg = new ImageIcon("image/plus.png");
    plusImg_s = new ImageIcon("image/plus_s.png");
    plus2Img = new ImageIcon("image/plus2.png");
    plus2Img_s = new ImageIcon("image/plus2_s.png");
    plus3Img = new ImageIcon("image/plus3.png");
    plus3Img_s = new ImageIcon("image/plus3_s.png");
    backImg = new ImageIcon("image/back.png");
    backImg_s = new ImageIcon("image/back_s.png");
    addWorkImg = new ImageIcon("image/addwork.png");
    addWorkImg_s = new ImageIcon("image/addwork_s.png");
    tabImg = new ImageIcon("image/tab.png");
    tabImg_s = new ImageIcon("image/tab.png"); //
    xImg = new ImageIcon("image/x.png");
    xImg_s = new ImageIcon("image/x_s.png");
    addImageImg = new ImageIcon("image/addimage.png");
    addImageImg_s = new ImageIcon("image/addimage_s.png");
    addImage2Img = new ImageIcon("image/addimage2.png");
    addImage2Img_s = new ImageIcon("image/addimage2_s.png");
    addMapImg = new ImageIcon("image/addmap.png");
    addMapImg_s = new ImageIcon("image/addmap_s.png");
    addRouteImg = new ImageIcon("image/addroute.png");
    addRouteImg_s = new ImageIcon("image/addroute_s.png");
    inImg = new ImageIcon("image/in.png");
    inImg_s = new ImageIcon("image/in_s.png");
    outImg = new ImageIcon("image/out.png");
    outImg_s = new ImageIcon("image/out_s.png");
    nodeImg = new ImageIcon("image/node.png");
    nodeImg_s = new ImageIcon("image/node_s.png");
    workImg = new ImageIcon("image/work.png");
    workImg_s = new ImageIcon("image/work_s.png");
    saveImg = new ImageIcon("image/save.png");
    saveImg_s = new ImageIcon("image/save_s.png");
    pathImg = new ImageIcon("image/path.png");
    pathImg_s = new ImageIcon("image/path_s.png");

    ///////////////// mainPage ///////////////
    workPageImg = new ImageIcon("image/workpage.png");
    workPageImg_s = new ImageIcon("image/workpage.png");//
    mapPageImg = new ImageIcon("image/mappage.png");
    mapPageImg_s = new ImageIcon("image/mappage.png"); //
    routePageImg = new ImageIcon("image/routepage.png");
    routePageImg_s = new ImageIcon("image/routepage.png");//

  }

  public void setFrame(JFrame f) {
    frame = f;
  }

  public JFrame getFrame() {
    return frame;
  }

  // --------------page move-----------//

  public void moveMainPage() {
    MainPage p = new MainPage();
    frame.setVisible(false);
    frame = p;
    frame.setBounds(0, 0, 450, 600);

    frame.setVisible(true);
  }

  public void moveDataManagePage() {
    if (dmp == null)
      dmp = new DataManagePage();
    frame.setBounds(0, 0, 1010, 810);
    dmp.setSize(frame.getSize());
    frame.setVisible(false);
    frame = dmp;
    frame.setVisible(true);
  }

  public void moveMapManagePage() {
    if (mmp == null)
      mmp = new MapManagePage();
    frame.setBounds(0, 0, 1010, 810);
    mmp.setSize(frame.getSize());
    frame.setVisible(false);
    frame = mmp;
    frame.setVisible(true);
  }

  public void moveRouteManagePage() {
    if (rmp == null)
      rmp = new RouteManagePage();

    frame.setBounds(0, 0, 1000, 800);
    rmp.setSize(frame.getSize());
    frame.setVisible(false);
    frame = rmp;
    frame.setVisible(true);
  }

  // -------------------Image Get-------------------//

  public ImageIcon getPlusImg() {
    return plusImg;
  }

  public ImageIcon getPlusImg_s() {
    return plusImg_s;
  }

  public ImageIcon getPlus2Img() {
    return plus2Img;
  }

  public ImageIcon getPlus2Img_s() {
    return plus2Img_s;
  }

  public ImageIcon getPlus3Img() {
    return plus3Img;
  }

  public ImageIcon getPlus3Img_s() {
    return plus3Img_s;
  }

  public ImageIcon getBackImg() {
    return backImg;
  }

  public ImageIcon getBackImg_s() {
    return backImg_s;
  }

  public ImageIcon getAddWorkImg() {
    return addWorkImg;
  }

  public ImageIcon getAddWorkImg_s() {
    return addWorkImg_s;
  }

  public ImageIcon getTabImg() {
    return tabImg;
  }

  public ImageIcon getTabImg_s() {
    return tabImg_s;
  }

  public ImageIcon getxImg() {
    return xImg;
  }

  public ImageIcon getxImg_s() {
    return xImg_s;
  }

  public ImageIcon getAddImageImg() {
    return addImageImg;
  }

  public ImageIcon getAddImageImg_s() {
    return addImageImg_s;
  }

  public ImageIcon getAddImage2Img() {
    return addImage2Img;
  }

  public ImageIcon getAddImage2Img_s() {
    return addImage2Img_s;
  }

  public ImageIcon getAddMapImg() {
    return addMapImg;
  }

  public ImageIcon getAddMapImg_s() {
    return addMapImg_s;
  }

  public ImageIcon getAddRouteImg() {
    return addRouteImg;
  }

  public ImageIcon getAddRouteImg_s() {
    return addRouteImg_s;
  }

  public ImageIcon getInImg() {
    return inImg;
  }

  public ImageIcon getInImg_s() {
    return inImg_s;
  }

  public ImageIcon getOutImg() {
    return outImg;
  }

  public ImageIcon getOutImg_s() {
    return outImg_s;
  }

  public ImageIcon getNodeImg() {
    return nodeImg;
  }

  public ImageIcon getNodeImg_s() {
    return nodeImg_s;
  }

  public ImageIcon getWorkImg() {
    return workImg;
  }

  public ImageIcon getWorkImg_s() {
    return workImg_s;
  }

  public ImageIcon getSaveImg() {
    return saveImg;
  }

  public ImageIcon getSaveImg_s() {
    return saveImg_s;
  }

  public ImageIcon getWorkPageImg() {
    return workPageImg;
  }

  public ImageIcon getWorkPageImg_s() {
    return workPageImg_s;
  }

  public ImageIcon getMapPageImg() {
    return mapPageImg;
  }

  public ImageIcon getMapPageImg_s() {
    return mapPageImg_s;
  }

  public ImageIcon getRoutePageImg() {
    return routePageImg;
  }

  public ImageIcon getRoutePageImg_s() {
    return routePageImg_s;
  }

  public ImageIcon getPathImg() {
    return pathImg;
  }

  public ImageIcon getPathImg_s() {
    return pathImg_s;
  }

  // ------------------GET / SET-------------------//

  public ArrayList<RouteData> getRouteList() {
    return routeList;
  }

  public void setRouteList(ArrayList<RouteData> routeList) {
    this.routeList = routeList;
  }

  public int getRealRoomNum() {
    return realRoomNum;
  }

  public int getRealWorkNum() {
    return realWorkNum;
  }

  public ArrayList<RoomData> getRoomDataList() {
    return roomDataList;
  }

  public void setRoomDataList(ArrayList<RoomData> roomData) {
    this.roomDataList = roomData;
  }

  public int getRoomCnt() {
    return roomCnt;
  }

  public void setRoomCnt(int roomCnt) {
    this.roomCnt = roomCnt;
  }

  public int getWorkCnt() {
    return workCnt;
  }

  public void setWorkCnt(int workCnt) {
    this.workCnt = workCnt;
  }

  public int getNodeCnt() {
    return nodeCnt;
  }

  public void setNodeCnt(int nodeCnt) {
    this.nodeCnt = nodeCnt;
  }

  public int getDataCnt() {
    return workCnt;
  }

  public HashMap getWorkHashMap() {
    return workHashMap;
  }

  public void setWorkHashMap(HashMap workHashMap) {
    this.workHashMap = workHashMap;
  }

  public void setCurrentRoomNum(int num) {
    currentRoomNum = num;
  }

  public int getCurrentRoomNum() {
    return currentRoomNum;
  }

  public int getRouteCnt() {
    return routeCnt;
  }

  ////////////////// ++ / -- ////////////////////

  public void removeNode(int realNum) {

  }

  public void removeWork(int realNum) {

  }

  public void routeCntIncrement() {
    routeCnt++;
  }

  public void routeCntDecrement() {
    routeCnt--;
  }

  public void roomCntIncrement() {
    roomCnt++;
  }

  public void roomCntDecrement() {
    roomCnt--;
  }

  public void dataCntIncrement() {
    workCnt++;
  }

  public void dataCntDecrement() {
    workCnt--;
  }

  public void nodeCntIncrement() {
    nodeCnt++;
  }

  public void nodeCntDecrement() {
    nodeCnt--;
  }

  public int realRoomNumIncrement() {
    return ++realRoomNum;
  }

  public int realWorkNumIncrement() {
    return ++realWorkNum;
  }
  /////////////////////////////////////

  public DataManagePage getDmp() {
    return dmp;
  }

  public void setDmp(DataManagePage dmp) {
    this.dmp = dmp;
  }

  public MapManagePage getMmp() {
    return mmp;
  }

  public void setMmp(MapManagePage mmp) {
    this.mmp = mmp;
  }

  public RouteManagePage getRmp() {
    return rmp;
  }

  public void setRmp(RouteManagePage rmp) {
    this.rmp = rmp;
  }

  ///////////////////////////////////////////////

  public static GUI_console getInstance() {
    return gui_console;
  }

  //////////////// ���� ��ư ������ �� ������ ������ ���� //////////////////
  /*
   * RoomData
   * WorkData
   * RouteData
   * �ʿ��� ������ �����ؼ� �����ϸ� ��
   * 
   * ������ �� ���ÿ� ����
   */
  public void save() {

    /** 박성준 작업함. 2016.6.8 */
    // 전시물 데이터 관리에서의 저장
    Area[] areas = new Area[this.roomCnt];
    Item[][] items = new Item[this.roomCnt][];
    ItemImage[][][] itemImages = new ItemImage[this.roomCnt][][];
    for (int i = 0; i < areas.length; i++) {
      RoomData roomData = this.roomDataList.get(i);
      areas[i] = new Area();
      areas[i].setNum(roomData.getRealNum());
      areas[i].setName(roomData.getName());
      areas[i].setImage(
          (roomData.getFilePath() == null ? "" : roomData.getFilePath()));

      items[i] = new Item[roomData.getWorkCnt()];
      itemImages[i] = new ItemImage[roomData.getWorkCnt()][];
      for (int j = 0; j < roomData.getWorkCnt(); j++) {
        WorkData workData = roomData.getWorkDataList().get(j);
        items[i][j] = new Item();
        items[i][j].setNum(workData.getRealNum());
        items[i][j].setTitle(workData.getTitle());
        items[i][j].setArtist(workData.getArtist());
        items[i][j].setSimpleContent(workData.getSimpleContents());
        items[i][j].setDetailContent(workData.getContents());
        items[i][j].setSize("");
        items[i][j].setAreaNum(roomData.getRealNum());

        itemImages[i][j] = new ItemImage[workData.getImageScr().size()];
        for (int k = 0; k < workData.getImageScr().size(); k++) {
          String itemImagePath = (workData.getImageScr().get(k) == null ? ""
              : workData.getImageScr().get(k));
          itemImages[i][j][k] = new ItemImage();
          itemImages[i][j][k].setNum(workData.getImageRealNum().get(k));
          itemImages[i][j][k].setSeq(k + 1);
          itemImages[i][j][k].setImage(itemImagePath);
          itemImages[i][j][k].setMain(k == 0);
          itemImages[i][j][k].setItemNum(workData.getRealNum());
        }
      }
    }
    try {
      AreaController.saveAreas(areas, items, itemImages);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }

    // roomData, workData의 real num 갱신
    for (int i = 0; i < areas.length; i++) {
      RoomData roomData = this.roomDataList.get(i);
      roomData.setRealNum(areas[i].getNum());

      for (int j = 0; j < items[i].length; j++) {
        WorkData workData = roomData.getWorkDataList().get(j);
        workData.setRealNum(items[i][j].getNum());

        for (int k = 0; k < itemImages[i][j].length; k++) {
          workData.getImageRealNum().set(k, itemImages[i][j][k].getNum());
        }
      }
    }

    // 지도 관리에서의 저장
    Area[] areas2 = new Area[this.roomCnt];
    Item[][] items2 = new Item[this.roomCnt][];
    Block[][] blocks = new Block[this.roomCnt][];
    Node[][] nodes = new Node[this.roomCnt][];
    for (int i = 0; i < this.roomCnt; i++) {
      RoomData roomData = this.roomDataList.get(i);
      areas2[i] = new Area();
      areas2[i].setNum(roomData.getRealNum());
      areas2[i].setName(roomData.getName());
      areas2[i].setImage(roomData.getFilePath());

      items2[i] = new Item[roomData.getBlock().size()];
      blocks[i] = new Block[roomData.getBlock().size()];
      nodes[i] = new Node[roomData.getBlock().size()];
      for (int j = 0; j < roomData.getBlock().size(); j++) {
        MapManagePage.Block blockData = roomData.getBlock().get(j);
        if (blockData.getType() == MapManagePage.WORK) {
          WorkData workData = roomData.getWorkDataList()
              .get(blockData.getWorkNum());
          items2[i][j] = new Item();
          items2[i][j].setNum(workData.getRealNum());
          items2[i][j].setArtist(workData.getArtist());
          items2[i][j].setArtist(workData.getArtist());
          items2[i][j].setSimpleContent(workData.getSimpleContents());
          items2[i][j].setDetailContent(workData.getContents());
          items2[i][j].setSize("");
          items2[i][j].setAreaNum(roomData.getRealNum());
          nodes[i][j] = null;
        } else if (blockData.getType() == MapManagePage.NODE) {
          items[i][j] = null;
          nodes[i][j] = new Node();
        } else {
          items2[i][j] = null;
          nodes[i][j] = null;
        }

        blocks[i][j] = new Block();
        blocks[i][j].setNum(0);
        blocks[i][j].setSeq(j + 1);
        blocks[i][j]
            .setItemNum(items2[i][j] == null ? 0 : items2[i][j].getNum());
        blocks[i][j].setAreaNum(roomData.getRealNum());
      }
    }

    Block[][][] blockPairs = new Block[this.roomCnt][][];
    for (int i = 0; i < this.roomCnt; i++) {

      RoomData roomData = this.roomDataList.get(i);
      List<Block[]> pairList = new ArrayList<Block[]>();

      for (int j = 0; j < roomData.getBlock().size(); j++) {
        MapManagePage.Block blockData = roomData.getBlock().get(j);
        if (blockData.getType() == MapManagePage.SHARE) {
          Block[] blockPair = new Block[2];
          blockPair[0] = blocks[i][j];
          blockPair[1] = blocks[blockData.getShareRoomNum() - 1][blockData
              .getShareBlockNum() - 1];
          pairList.add(blockPair);
        }
      }
      blockPairs[i] = (Block[][]) pairList
          .toArray(new Block[pairList.size()][]);
    }
    try {
      AreaController.saveAreas(areas, items, blocks, blockPairs, nodes);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }

    // 추천 경로 관리에서의 저장
    Recommend[] recommends = new Recommend[this.routeCnt];
    RecommendItem[][] recommendItems = new RecommendItem[this.routeCnt][];
    for (int i = 0; i < this.routeCnt; i++) {
      RouteData routeData = this.routeList.get(i);
      recommends[i] = new Recommend();
      recommends[i].setNum(routeData.getRouteNum());
      recommends[i].setImage(
          (routeData.getImgScr() == null ? "" : routeData.getImgScr()));
      recommends[i].setContent(routeData.getTitle());

      int itemLength = routeData.getWorkDataList().size();
      recommendItems[i] = new RecommendItem[itemLength];
      for (int j = 0; j < itemLength; j++) {
        recommendItems[i][j] = new RecommendItem();
        recommendItems[i][j].setNum(0);
        recommendItems[i][j].setSeq(j + 1);
        recommendItems[i][j].setRecommendNum(routeData.getRealNum());
        recommendItems[i][j]
            .setItemNum(routeData.getWorkDataList().get(j).getRealNum());
      }
    }
    try {
      RecommendController.saveRecommends(recommends, recommendItems);
    } catch (SQLException | IOException e) {
      e.printStackTrace();
    }
    /** end of 박성준 */
  }

  ///////////////// ����� ������ �ҷ����� //////////////////////
  public void load() {
    /** 박성준 작업 2016.6.11. */
    try {
      // roomData 로드
      Area[] areas = AreaController.getAreas();
      this.roomDataList.clear();
      this.workHashMap.clear();
      for (int i = 0; i < areas.length; i++) {
        RoomData roomData = new RoomData(areas[i].getName());
        roomData.setFilePath(areas[i].getImage());
        roomData.setMap("".equals(areas[i].getImage())
            ? new ImageIcon(areas[i].getImage()).getImage() : null);
        roomData.setRealNum(areas[i].getNum());
        roomData.setRoomNum(i + 1);
        this.roomDataList.add(roomData);

        // workData 로드
        Item[] items = ItemController.getItemsByAreaNum(areas[i].getNum());
        ArrayList<WorkData> workDataList = new ArrayList<WorkData>();
        for (int j = 0; j < items.length; j++) {
          WorkData workData = new WorkData();
          workData.setArtist(items[j].getArtist());
          workData.setAssigned(false);
          workData.setContents(items[j].getDetailContent());
          workData.setEdited(false);
          workData.setHashNum(items[i].getNum());
          workData.setName(items[i].getTitle());
          workData.setRealNum(items[i].getNum());
          workData.setRoomNum(i + 1);
          workData.setSimpleContents(items[i].getSimpleContent());
          workData.setTitle(items[i].getTitle());
          workData.setWorkNum(j + 1);
          workDataList.add(workData);
          this.workHashMap.put(items[i].getNum(), workData);

          ItemImage[] itemImages = ItemImageController
              .getItemImagseByItemNum(items[j].getNum());
          ArrayList<Image> imgList = new ArrayList<Image>();
          ArrayList<String> imgScrList = new ArrayList<String>();
          ArrayList<Integer> imgRealNumList = new ArrayList<Integer>();
          for (int k = 0; k < itemImages.length; k++) {
            imgRealNumList.add(itemImages[k].getNum());
            imgScrList.add(itemImages[k].getImage());
            imgList.add("".equals(itemImages[k].getImage())
                ? new ImageIcon(itemImages[k].getImage()).getImage() : null);
          }
          workData.setImage(imgList);
          workData.setImageScr(imgScrList);
          workData.setImageRealNum(imgRealNumList);
        }
        roomData.setWorkCnt(items.length);
        roomData.setWorkDataList(workDataList);
      }
      this.roomCnt = this.roomDataList.size();
      this.workCnt = this.workHashMap.size();

      // block 로드
      for (int i = 0; i < this.roomCnt; i++) {
        Block[] blocks = BlockController.getBlocksByAreaNum(areas[i].getNum());
        ArrayList<MapManagePage.Block> blockList = new ArrayList<MapManagePage.Block>();
        for (int j = 0; j < blocks.length; j++) {
          MapManagePage.Block block = /* new MapManagePage.Block(j) */null; // !!!!!!!
          if (blocks[j].getItemNum() != 0) {
            block.setWorkNum(
                ((WorkData) this.workHashMap.get(blocks[j].getItemNum()))
                    .getWorkNum());
          } else {
            block.setWorkNum(-1);
          }
          block.setHashWorkNum(blocks[j].getItemNum());
          block.setRoomNum(i + 1);

          if (blocks[j].getItemNum() != 0) {
            block.setType(MapManagePage.WORK);
          } else if (NodeController
              .getNodeByBlockNum(blocks[j].getNum()) != null) {
            block.setType(MapManagePage.NODE);
          } else if (ShareBlockController
              .getShareBlocksByBlockNum1(blocks[j].getNum()) != null) {

            Block toBlock = BlockController.getBlockByNum(ShareBlockController
                .getShareBlocksByBlockNum1(blocks[j].getNum())[0]
                    .getBlockNum2());

            block.setType(MapManagePage.SHARE);
            block.setShareBlockNum(toBlock.getSeq()); // -1 해야할까? 말아야할까? (재민)
            block.setShareRoomNum(toBlock.getAreaNum()); // -1 해야할까? 말아야할까? (재민)
          } else {
            block.setType(MapManagePage.NONE);
          }
        }
        this.roomDataList.get(i).setBlock(blockList);
      }

      // RouteData 로드
      Recommend[] recommends = RecommendController.getRecommends();
      this.routeList.clear();
      for (int i = 0; i < recommends.length; i++) {
        RouteData routeData = new RouteData(recommends[i].getContent(), i + 1);
        routeData.setRealNum(recommends[i].getNum());
        routeData.setImg("".equals(recommends[i].getImage())
            ? new ImageIcon(recommends[i].getImage()).getImage() : null);
        routeData.setImgScr(recommends[i].getImage());

        // RouteData내의 WorkData 로드
        RecommendItem[] recommendItems = RecommendItemController
            .getRecommendItemsByRecommendNum(recommends[i].getNum());
        ArrayList<WorkData> workDataList = new ArrayList<WorkData>();
        for (int j = 0; j < recommendItems.length; j++) {
          WorkData workData = (WorkData) this.workHashMap
              .get(recommendItems[j].getItemNum());
          workDataList.add(workData);
        }
        routeData.setWorkDataList(workDataList);

        this.routeList.add(routeData);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    /** end of 박성준 */
  }
}
