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
            "insert into area values (1,'test1','image/area/test1')")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into area values (2,'test2','image/area/test2')")
        .executeUpdate();

    dbConnection
        .prepareStatement(
            "insert into item values (1,'test1','tester1','test_simple1','test_detail1','test_size1',1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item values (2,'test2','tester2','test_simple2','test_detail2','test_size2',1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item values (3,'test3','tester3','test_simple3','test_detail3','test_size3',2)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item values (4,'test4','tester4','test_simple4','test_detail4','test_size4',2)")
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
            "insert into item_image values (3,3,'image/item/test3.png',0,1)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (4,1,'image/item/test4.png',1,2)")
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
            "insert into item_image values (7,3,'image/item/test7.png',0,3)")
        .executeUpdate();
    dbConnection
        .prepareStatement(
            "insert into item_image values (8,1,'image/item/test8.png',1,4)")
        .executeUpdate();

    dbConnection
        .prepareStatement(
            "insert into recommend values (1,'test1','image/recommend/rising.png')")
        .executeUpdate();

    dbConnection.prepareStatement("insert into recommend_item values (1,1,1,4)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into recommend_item values (2,2,1,3)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into recommend_item values (3,3,1,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into recommend_item values (4,4,1,1)")
        .executeUpdate();

    dbConnection.prepareStatement("insert into block values (1,1,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (2,2,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (3,3,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (4,4,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (5,5,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (6,6,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (7,7,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (8,8,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (9,9,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (10,10,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (11,11,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (12,12,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (13,13,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (14,14,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (15,15,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (16,16,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (17,17,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (18,18,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (19,19,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (20,20,2,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (21,21,1,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (22,22,1,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (23,23,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (24,24,null,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (25,25,2,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (26,1,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (27,2,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (28,3,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (29,4,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (30,5,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (31,6,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (32,7,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (33,8,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (34,9,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (35,10,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (36,11,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (37,12,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (38,13,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (39,14,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (40,15,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (41,16,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (42,17,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (43,18,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (44,19,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (45,20,4,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (46,21,3,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (47,22,3,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (48,23,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (49,24,null,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into block values (50,25,4,2)")
        .executeUpdate();

    dbConnection.prepareStatement("insert into share_block values (1,5,26)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into share_block values (2,26,5)")
        .executeUpdate();

    dbConnection.prepareStatement("insert into node values (1,1)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (2,2)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (3,3)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (4,4)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (5,5)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (6,6)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (7,10)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (8,11)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (9,15)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (10,16)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (11,20)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (12,21)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (13,22)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (14,23)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (15,24)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (16,25)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (17,26)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (18,27)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (19,28)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (20,29)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (21,30)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (22,31)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (23,35)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (24,36)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (25,40)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (26,41)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (27,45)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (28,46)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (29,47)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (30,48)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (31,49)")
        .executeUpdate();
    dbConnection.prepareStatement("insert into node values (32,50)")
        .executeUpdate();

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

    ImageManager
        .writeImageFromByteString(
            ImageManager
                .readByteStringFromImage("reset/image/recommend/rising.png"),
            "image/recommend/rising.png");

  }
}
