  package harmony.admin.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import harmony.admin.controller.AreaController;
import harmony.admin.controller.BlockController;
import harmony.admin.controller.ItemController;
import harmony.admin.controller.ItemImageController;
import harmony.admin.controller.NodeController;
import harmony.admin.controller.RecommendController;
import harmony.admin.controller.RecommendItemController;
import harmony.admin.controller.ShareBlockController;
import harmony.admin.model.Area;
import harmony.admin.model.Block;
import harmony.admin.model.Item;
import harmony.admin.model.ItemImage;
import harmony.admin.model.Node;
import harmony.admin.model.Recommend;
import harmony.admin.model.RecommendItem;
import harmony.admin.model.ShareBlock;
import harmony.common.AbstractService;

/**
 * 현재 관리자 서버가 가지고 있는 전시물 정보, 블록 정보, 비콘세기율 정보,<br>
 * 지도 정보, 공유 블록 정보를 제공함.
 * 
 * @author Seonjun Park
 * @since 2016/5/8
 * @version 2016/6/10
 */
public class UpdateService extends AbstractService {

  /**
   * DB에 있는 구역, 전시물, 블록, 공유블록, 노드, 추천경로 정보를 제공함.
   * 
   * @param argument
   *          사용 안 함
   * @return Object[][][]
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  @Override
  public Object doService(Object argument) throws SQLException, IOException {

    // [0]은 area, [1]은 item, [2]는 block,
    // [3]은 share_block, [4]는 node, [5]는 recommend
    List<Object[][]> objList = new ArrayList<Object[][]>();
    objList.add(this.getAreas());
    objList.add(this.getItems());
    objList.add(this.getBlocks());
    objList.add(this.getShareBlocks());
    objList.add(this.getNodes());
    objList.add(this.getRecommends());

    return (Object[][][]) objList.toArray(new Object[objList.size()][][]);
  }

  /**
   * 
   * @return Object[][] = {{a_num, a_name, a_row, a_col}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getAreas() throws SQLException {
    Area[] areas = AreaController.getAreas();
    Object[][] objArr2D = new Object[areas.length][];

    for (int i = 0; i < areas.length; i++) {
      List<Object> objList = new ArrayList<Object>();
      objList.add(areas[i].getNum());
      objList.add(areas[i].getName());
      objList.add(10);
      objList.add(10);
      objArr2D[i] = (Object[]) objList.toArray(new Object[objList.size()]);
    }

    return objArr2D;
  }

  /**
   * 
   * @return Object[][] = {{i_num, i_title, i_artist, i_simple_content,
   *         i_detail_content, ii_num1(대표), ii_num2, ...}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getItems() throws SQLException {
    Item[] items = ItemController.getItems();
    Object[][] objArr2D = new Object[items.length][];

    for (int i = 0; i < items.length; i++) {
      List<Object> objList = new ArrayList<Object>();
      objList.add(items[i].getNum());
      objList.add(items[i].getTitle());
      objList.add(items[i].getArtist());
      objList.add(items[i].getSimpleContent());
      objList.add(items[i].getDetailContent());
      ItemImage[] itemImages = ItemImageController
          .getItemImagseByItemNum(items[i].getNum());
      for (ItemImage itemImage : itemImages) {
        if (itemImage.isMain() && objList.size() >= 6) {
          objList.add(5, itemImage.getNum());
        } else {
          objList.add(itemImage.getNum());
        }
      }
      objArr2D[i] = (Object[]) objList.toArray(new Object[objList.size()]);
    }

    return objArr2D;
  }

  /**
   * 
   * @return Object[][] = {{bl_seq, i_num, a_num}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getBlocks() throws SQLException {
    Block[] blocks = BlockController.getBlocks();
    Object[][] objArr2D = new Object[blocks.length][];

    for (int i = 0; i < blocks.length; i++) {
      List<Object> objList = new ArrayList<Object>();
      objList.add(blocks[i].getSeq());
      objList.add(blocks[i].getItemNum());
      objList.add(blocks[i].getAreaNum());
      objArr2D[i] = (Object[]) objList.toArray(new Object[objList.size()]);
    }

    return objArr2D;
  }

  /**
   * 
   * @return Object[][] = {{sb_num, bl_seq1, bl_seq2, a_num1, a_num2}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getShareBlocks() throws SQLException {
    ShareBlock[] shareBlocks = ShareBlockController.getShareBlocks();
    Object[][] objArr2D = new Object[shareBlocks.length][];

    for (int i = 0; i < shareBlocks.length; i++) {
      List<Object> objList = new ArrayList<Object>();
      objList.add(shareBlocks[i].getNum());
      Block block1 = BlockController
          .getBlockByNum(shareBlocks[i].getBlockNum1());
      Block block2 = BlockController
          .getBlockByNum(shareBlocks[i].getBlockNum2());
      objList.add(block1.getSeq());
      objList.add(block2.getSeq());
      objList.add(block1.getAreaNum());
      objList.add(block2.getAreaNum());
      objArr2D[i] = (Object[]) objList.toArray(new Object[objList.size()]);
    }

    return objArr2D;
  }

  /**
   * 
   * @return Object[][] = {{bl_seq, a_num}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getNodes() throws SQLException {
    Node[] nodes = NodeController.getNodes();
    Object[][] objArr2D = new Object[nodes.length][];

    for (int i = 0; i < nodes.length; i++) {
      Block block = BlockController.getBlockByNum(nodes[i].getBlockNum());
      List<Object> objList = new ArrayList<Object>();
      objList.add(block.getSeq());
      objList.add(block.getAreaNum());
      objArr2D[i] = (Object[]) objList.toArray(new Object[objList.size()]);
    }

    return objArr2D;
  }

  /**
   * 
   * @return Object[][] = {{r_num, r_content, i_num1, i_num2, ...}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getRecommends() throws SQLException {
    Recommend[] recommends = RecommendController.getRecommends();
    Object[][] objArr2D = new Object[recommends.length][];

    for (int i = 0; i < recommends.length; i++) {
      List<Object> objList = new ArrayList<Object>();
      objList.add(recommends[i].getNum());
      objList.add(recommends[i].getContent());
      RecommendItem[] recommendItems = RecommendItemController
          .getRecommendItemsByRecommendNum(recommends[i].getNum());
      for (RecommendItem recommendItem : recommendItems) {
        objList.add(recommendItem.getItemNum());
      }
      objArr2D[i] = (Object[]) objList.toArray(new Object[objList.size()]);
    }

    return objArr2D;
  }
}
