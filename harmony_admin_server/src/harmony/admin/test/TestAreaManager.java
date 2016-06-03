package harmony.admin.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import harmony.admin.controller.AreaController;
import harmony.admin.controller.BlockController;
import harmony.admin.controller.ShareBlockController;
import harmony.admin.database.DbConnector;
import harmony.admin.model.Area;
import harmony.admin.model.Block;
import harmony.admin.model.Item;
import harmony.admin.model.ItemImage;
import harmony.admin.model.ShareBlock;

/**
 * GUI의 지도 관리 메뉴에 해당
 * 
 * @author Seongjun Park
 * @since 2016/6/2
 * @version 2016/6/2
 */
public class TestAreaManager {

  @Test
  public void loadScreenTest() throws SQLException {
    Area[] areas = AreaController.getAreas(); // 모든 구역 가져오기
    for (Area area : areas) {
      System.out.println(
          area.getNum() + ", " + area.getName() + ", " + area.getImage());

      Block[] blocks = BlockController.getBlocksByAreaNum(area.getNum()); // 구역별
                                                                          // 블록
                                                                          // 가져오기
      for (Block block : blocks) {
        System.out.println("\t" + block.getNum() + ", " + block.getSeq() + ", "
            + block.getItemNum() + ", " + block.getAreaNum());

        ShareBlock[] shareBlocks = ShareBlockController
            .getShareBlocksByBlockNum1(block.getNum()); // 블록별 공유블록 가져오기
        for (ShareBlock shareBlock : shareBlocks) {
          Block[] blockPairs = new Block[2];
          blockPairs[0] = BlockController
              .getBlockByNum(shareBlock.getBlockNum1());
          blockPairs[1] = BlockController
              .getBlockByNum(shareBlock.getBlockNum2());

          System.out.println("\t\t" + blockPairs[0].getAreaNum() + ", "
              + blockPairs[1].getSeq() + ", " + blockPairs[0].getAreaNum()
              + ", " + blockPairs[1].getSeq());
        }
        System.out.println();
      }
      System.out.println();
    }
    System.out.println();

    DbConnector.getInstance().closeConnection();
  }
}
