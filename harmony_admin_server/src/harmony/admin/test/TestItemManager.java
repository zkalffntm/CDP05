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
import harmony.admin.controller.ItemController;
import harmony.admin.controller.ItemImageController;
import harmony.admin.model.Area;
import harmony.admin.model.Item;
import harmony.admin.model.ItemImage;

/**
 * GUI의 전시물 관리 메뉴에 해당
 * 
 * @author Seongjun Park
 * @since 2016/6/2
 * @version 2016/6/2
 */
public class TestItemManager {

  // @Test
  public void loadScreenTest() throws SQLException {
    Area[] areas = AreaController.getAreas(); // 모든 구역 가져오기
    for (Area area : areas) {
      System.out.println(
          area.getNum() + ", " + area.getName() + ", " + area.getImage());

      Item[] items = ItemController.getItemsByAreaNum(area.getNum()); // 구역별 전시물
                                                                      // 가져오기
      for (Item item : items) {
        System.out.println("\t" + item.getNum() + ", " + item.getTitle() + ", "
            + item.getArtist() + ", " + item.getSimpleContent() + ", "
            + item.getDetailContent() + ", " + item.getSize() + ", "
            + item.getAreaNum());

        ItemImage[] itemImages = ItemImageController
            .getItemImagseByItemNum(item.getNum()); // 전시물별 이미지 가져오기
        for (ItemImage itemImage : itemImages) {
          System.out.println("\t\t" + itemImage.getNum() + ", "
              + itemImage.getSeq() + ", " + itemImage.getImage() + ", "
              + itemImage.isMain() + ", " + itemImage.getItemNum());
        }
        System.out.println();
      }
      System.out.println();
    }
    System.out.println();
  }

  @Test
  public void saveTest() throws IOException, SQLException {
    AreaController.saveAreas(makeAreas(), makeItems(), makeItemImages());
  }

  private Area[] makeAreas() throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream("test\\areas.txt")));
    List<Area> areaList = new ArrayList<Area>();

    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] tokens = line.split(";");
      Area area = new Area();
      area.setNum(Integer.parseInt(tokens[0]));
      area.setName(tokens[1]);
      area.setImage(tokens[2]);
      area.setImageEdited("true".equals(tokens[3]));
      areaList.add(area);
    }

    reader.close();
    return (Area[]) areaList.toArray(new Area[areaList.size()]);
  }

  private Item[][] makeItems() throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream("test\\items.txt")));
    List<Item[]> itemArrList = new ArrayList<Item[]>();

    List<Item> itemList = null;
    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] tokens = line.split(";");
      if (tokens.length == 1) {
        if (itemList != null) {
          itemArrList.add((Item[]) itemList.toArray(new Item[itemList.size()]));
        }
        itemList = new ArrayList<Item>();
      } else {
        Item item = new Item();
        item.setNum(Integer.parseInt(tokens[0]));
        item.setTitle(tokens[1]);
        item.setArtist(tokens[2]);
        item.setSimpleContent(tokens[3]);
        item.setDetailContent(tokens[4]);
        item.setSize(tokens[5]);
        item.setAreaNum(Integer.parseInt(tokens[6]));
        itemList.add(item);
      }
    }
    if (itemList != null) {
      itemArrList.add((Item[]) itemList.toArray(new Item[itemList.size()]));
    }

    reader.close();
    return (Item[][]) itemArrList.toArray(new Item[itemArrList.size()][]);
  }

  private ItemImage[][][] makeItemImages() throws IOException {
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream("test\\item_images.txt")));
    List<ItemImage[][]> itemImageArrArrList = new ArrayList<ItemImage[][]>();

    List<ItemImage[]> itemImageArrList = null;
    List<ItemImage> itemImageList = null;
    String line = null;
    while ((line = reader.readLine()) != null) {
      String[] tokens = line.split(";");
      if (tokens.length == 2) {
        if (itemImageArrList != null) {
          itemImageArrArrList.add((ItemImage[][]) itemImageArrList
              .toArray(new ItemImage[itemImageArrList.size()][]));
        }
        itemImageArrList = new ArrayList<ItemImage[]>();
      } else if (tokens.length == 1) {
        if (itemImageList != null) {
          itemImageArrList.add((ItemImage[]) itemImageList
              .toArray(new ItemImage[itemImageList.size()]));
        }
        itemImageList = new ArrayList<ItemImage>();
      } else {
        ItemImage itemImage = new ItemImage();
        itemImage.setNum(Integer.parseInt(tokens[0]));
        itemImage.setSeq(Integer.parseInt(tokens[1]));
        itemImage.setImage(tokens[2]);
        itemImage.setImageEdited("true".equals(tokens[3]));
        itemImage.setMain("true".equals(tokens[4]));
        itemImage.setItemNum(Integer.parseInt(tokens[5]));
        itemImageList.add(itemImage);
      }
    }
    if (itemImageList != null) {
      itemImageArrList.add((ItemImage[]) itemImageList
          .toArray(new ItemImage[itemImageList.size()]));
    }
    if (itemImageArrList != null) {
      itemImageArrArrList.add((ItemImage[][]) itemImageArrList
          .toArray(new ItemImage[itemImageArrList.size()][]));
    }

    reader.close();
    return (ItemImage[][][]) itemImageArrArrList
        .toArray(new ItemImage[itemImageArrArrList.size()][][]);
  }
}
