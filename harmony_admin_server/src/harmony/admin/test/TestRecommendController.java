package harmony.admin.test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import harmony.admin.controller.ItemController;
import harmony.admin.controller.RecommendController;
import harmony.admin.controller.RecommendItemController;
import harmony.admin.model.Item;
import harmony.admin.model.Recommend;
import harmony.admin.model.RecommendItem;

/**
 * GUI의 추천경로 관리 메뉴에 해당
 * 
 * @author Seongjun Park
 * @since 2016/6/2
 * @version 2016/6/2
 */
public class TestRecommendController {

  // @Test
  public void loadScreenTest() throws SQLException {
    Recommend[] recommends = RecommendController.getRecommends();
    for (Recommend recommend : recommends) {
      System.out.println(recommend.getNum() + ", " + recommend.getContent()
          + ", " + recommend.getImage());

      RecommendItem[] recommendItems = RecommendItemController
          .getRecommendItemsByRecommendNum(recommend.getNum());
      for (RecommendItem recommendItem : recommendItems) {
        Item item = ItemController.getItemByNum(recommendItem.getItemNum());

        System.out.println("\t" + item.getNum() + ", " + item.getTitle() + ", "
            + item.getArtist() + ", " + item.getSimpleContent() + ", "
            + item.getDetailContent() + ", " + item.getSize() + ", "
            + item.getAreaNum());
      }
      System.out.println();
    }
    System.out.println();
  }

  @Test
  public void saveTest() throws IOException, SQLException {
    RecommendController.saveRecommends(makeRecommends(), makeRecommendItems());
  }

  private Recommend[] makeRecommends() throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream("test\\recommends.txt")));
    List<Recommend> recommendList = new ArrayList<Recommend>();

    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] tokens = line.split(";");
      Recommend recommend = new Recommend();
      recommend.setNum(Integer.parseInt(tokens[0]));
      recommend.setContent(tokens[1]);
      recommend.setImage(tokens[2]);
      recommend.setImageEdited("true".equals(tokens[3]));
      recommendList.add(recommend);
    }

    reader.close();
    return (Recommend[]) recommendList
        .toArray(new Recommend[recommendList.size()]);
  }

  private RecommendItem[][] makeRecommendItems() throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(
        new FileInputStream("test\\recommend_items.txt")));
    List<RecommendItem[]> recommendItemArrList = new ArrayList<RecommendItem[]>();
    List<RecommendItem> recommendItemList = null;

    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] tokens = line.split(";");
      if (tokens.length == 1) {
        if (recommendItemList != null) {
          recommendItemArrList.add((RecommendItem[]) recommendItemList
              .toArray(new RecommendItem[recommendItemList.size()]));
        }
        recommendItemList = new ArrayList<RecommendItem>();
      } else {
        RecommendItem recommendItem = new RecommendItem();
        recommendItem.setNum(Integer.parseInt(tokens[0]));
        recommendItem.setSeq(Integer.parseInt(tokens[1]));
        recommendItem.setRecommendNum(Integer.parseInt(tokens[2]));
        recommendItem.setItemNum(Integer.parseInt(tokens[3]));
        recommendItemList.add(recommendItem);
      }
    }
    if (recommendItemList != null) {
      recommendItemArrList.add((RecommendItem[]) recommendItemList
          .toArray(new RecommendItem[recommendItemList.size()]));
    }

    reader.close();
    return (RecommendItem[][]) recommendItemArrList
        .toArray(new RecommendItem[recommendItemArrList.size()][]);
  }
}
