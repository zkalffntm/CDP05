package harmony.admin.test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import harmony.admin.database.DbConnector;
import harmony.common.ImageManager;

public class TestResetter {
  public static void main(String[] args) throws SQLException, IOException {

    // 자동 커밋 일시 해제
    Connection dbConnection = DbConnector.getInstance().getConnection();
    boolean prevAutoCommit = dbConnection.getAutoCommit();
    dbConnection.setAutoCommit(false);

    // 레코드 갱신 쿼리 실행
    dbConnection.prepareStatement("delete from node").executeUpdate();
    dbConnection.prepareStatement("delete from share_block").executeUpdate();
    dbConnection.prepareStatement("delete from block").executeUpdate();
    dbConnection.prepareStatement("delete from recommend_item").executeUpdate();
    dbConnection.prepareStatement("delete from recommend").executeUpdate();
    dbConnection.prepareStatement("delete from beacon").executeUpdate();
    dbConnection.prepareStatement("delete from item_image").executeUpdate();
    dbConnection.prepareStatement("delete from item").executeUpdate();
    dbConnection.prepareStatement("delete from area").executeUpdate();

    dbConnection
        .prepareStatement(
            "insert into area values (1,'제1전시실','image/area/test1')")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into area values (2,'제2전시실','image/area/test2')")
        .executeUpdate();

    dbConnection
        .prepareStatement(
            "insert into item values (1,'돌칼','','청동기 유물','돌칼은 처음에서는 돌창, 뼈칼 등을 본으로 하여 청동기 시대에 자생적으로 발생하였다. 실용도구로 출발하였으나 차츰 청동검을 대신하는 부장용 의기로 변해가고, 형태도 중국식 청동검의 영향을 받아 세련되게 되었다.','길이 43.2cm',1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item values (2,'민무늬토기','','무문 토기','민무늬토기는 점토질 흙에 돌가루를 섞어서 손으로 빚어 올린 것이며, 색은 붉은 기가 도는 밝은 갈색이지만 색조에 변화가 있고 아래쪽이 납작한 화분형이 압도적으로 많다. 이 토기는 대구 월성동 2호분에서 출토되었다.','높이 34.5cm',1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item values (3,'오리모양토기','','오리와 닭이 조합된 듯한 신비한 새의 형상','새는 예로부터 천상과 지상을 연결하는 매개자로 죽은 이의 영혼을 인도하다고 믿어져 부장품으로 많이 쓰인다. 부리를 치켜세운 오리의 모습이 당당하다. 살짝 치켜 올린 꼬리가 잘리긴 했지만, 잘 다듬어져 뒤뚱뒤뚱 걷는 오리모습 그대로를 연상시킨다.','높이 16.0cm',2)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item values (4,'금동여래입상','','통일신라시대의 불상','풍만하면서도 평면적인 얼굴 모습은 통일신라시대 불상의 양식을 잘 반영한 것이다. 넓은 양 어깨에 걸친 옷은 길게 내려서 U자형의 옷깃을 보이면서 몸 전체를 감싸고 있다. 가슴에는 속에 입은 옷과 띠매듭을 나타내었는데, 이것도 통일신라시대 불상에서 나타나는 대표적인 특징이다.','높이 15.5cm',2)")
        .executeUpdate();

    dbConnection
        .prepareStatement(
            "insert into item_image values (1,1,'image/item/test1.png',1,1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (2,2,'image/item/test2.png',0,1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (3,1,'image/item/test3.png',1,2)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (4,2,'image/item/test4.png',2,2)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (5,1,'image/item/test5.png',1,3)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (6,2,'image/item/test6.png',0,3)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (7,1,'image/item/test7.png',1,4)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (8,2,'image/item/test8.png',0,4)")
        .executeUpdate();

    dbConnection
        .prepareStatement(
            "insert into recommend values (1,'청동기 모음','image/recommend/togi.png')")
        .executeUpdate();

    dbConnection.prepareStatement("insert into recommend_item values (1,1,1,4)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into recommend_item values (2,2,1,3)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into recommend_item values (3,3,1,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into recommend_item values (4,4,1,1)")
        .executeUpdate();

    for (int i = 0; i < 100; i++) {
      dbConnection.prepareStatement(
          "insert into block values (" + (i + 1) + "," + (i + 1) + ",null,1)")
          .executeUpdate();
    }
    dbConnection.prepareStatement("update block set i_num=1 where bl_num=91")
        .executeUpdate();
    dbConnection.prepareStatement("update block set i_num=2 where bl_num=100")
        .executeUpdate();
    for (int i = 100; i < 200; i++) {
      dbConnection.prepareStatement(
          "insert into block values (" + (i + 1) + "," + (i + 1) + ",null,2)")
          .executeUpdate();
    }
    dbConnection.prepareStatement("update block set i_num=3 where bl_num=200")
        .executeUpdate();
    dbConnection.prepareStatement("update block set i_num=4 where bl_num=191")
        .executeUpdate();

    dbConnection.prepareStatement("insert into share_block values (1,10,101)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into share_block values (2,101,10)")
        .executeUpdate();

    for (int i = 0; i < 100; i++) {
      if (i % 10 == 0 || i % 10 == 9 || i / 10 == 0 || i >= 90) {
        dbConnection
            .prepareStatement(
                "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
            .executeUpdate();
      }
    }
    
    for (int i=100;i<200;i++) {
      if (i % 10 == 0 || i % 10 == 9 || i / 110 == 0 || i >= 190) {
        dbConnection
            .prepareStatement(
                "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
            .executeUpdate();
      } else if(i == 110 && i == 111) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 121 && i == 122) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 132 && i == 133) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 143 && i == 144) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 154 && i == 155) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 165 && i == 166) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 176 && i == 177) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      } else if(i == 187 && i == 188) {
        dbConnection
        .prepareStatement(
            "insert into node values (" + (i + 1) + ", " + (i + 1) + ")")
        .executeUpdate();        
      }
    }
    
    // 커밋
    dbConnection.commit();
    dbConnection.setAutoCommit(prevAutoCommit);

    // 파일

    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/area/test1.png"),
        "image/area/test1.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/area/test2.png"),
        "image/area/test2.png");

    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test1.png"),
        "image/item/test1.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test2.png"),
        "image/item/test2.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test3.png"),
        "image/item/test3.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test4.png"),
        "image/item/test4.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test5.png"),
        "image/item/test5.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test6.png"),
        "image/item/test6.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test7.png"),
        "image/item/test7.png");
    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/item/test8.png"),
        "image/item/test8.png");

    ImageManager.writeImageFromByteString(
        ImageManager.readByteStringFromImage("reset/image/recommend/togi.png"),
        "image/recommend/togi.png");
  }
}
