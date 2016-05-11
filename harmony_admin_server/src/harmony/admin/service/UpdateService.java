package harmony.admin.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import harmony.admin.database.DbLiteral;
import harmony.common.AbstractService;

/**
 * 현재 관리자 서버가 가지고 있는 전시물 정보, 블록 정보, 비콘세기율 정보,<br>
 * 지도 정보, 공유 블록 정보를 제공함.
 * 
 * @author Seonjun Park
 * @since 2016/5/8
 * @version 2016/5/8
 */
public class UpdateService extends AbstractService {

  /**
   * DB에 있는 전시물, 블록, 비콘세기율, 지도, 공유블록 정보를 제공함.
   * 
   * @param argument
   *          사용 안 함
   * @return Object[6][][]
   * @throws SQLException
   *           SQL 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   * @throws IOException
   *           IO 관련 예외
   */
  @Override
  protected Object doQuery(Object argument)
      throws SQLException, JSONException, IOException {

    // [0]은 item, [1]은 block, [2]는 ratio_offset,
    // [3]은 map, [4]는 share_block, [5]는 recommend
    Object[][][] retval = new Object[5][][];
    retval[0] = this.getItems();
    retval[1] = this.getBlocks();
    retval[2] = this.getRatioOffsets();
    retval[3] = this.getMaps();
    retval[4] = this.getShareBlocks();
    retval[5] = this.getRecommends();

    return retval;
  }

  /**
   * 
   * @return Object[][3] = {{i_num, m_num, bl_num}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getItems() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();

    // 쿼리 실행
    String sql = "select " + DbLiteral.I_NUM + ", " + DbLiteral.M_NUM + ", "
        + DbLiteral.BL_NUM + " from " + DbLiteral.ITEM;
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    while (resultSet.next()) {
      Object[] objArr = new Object[3];
      objArr[0] = resultSet.getInt(DbLiteral.I_NUM);
      objArr[1] = resultSet.getInt(DbLiteral.M_NUM);
      objArr[2] = resultSet.getInt(DbLiteral.BL_NUM);
      objArrList.add(objArr);
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][3]);
  }

  /**
   * 
   * @return Object[][2] = {{m_num, bl_num}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getBlocks() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();

    // 쿼리 실행
    String sql = "select " + DbLiteral.M_NUM + ", " + DbLiteral.BL_NUM
        + " from " + DbLiteral.BLOCK;
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    while (resultSet.next()) {
      Object[] objArr = new Object[2];
      objArr[0] = resultSet.getInt(DbLiteral.M_NUM);
      objArr[1] = resultSet.getInt(DbLiteral.BL_NUM);
      objArrList.add(objArr);
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][2]);
  }

  /**
   * 
   * @return Object[][4] = {{m_num, bl_num, be_minor, ro_ratio}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getRatioOffsets() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();

    // 쿼리 실행
    String sql = "select " + DbLiteral.M_NUM + ", " + DbLiteral.BL_NUM + ", "
        + DbLiteral.BE_MINOR + ", " + DbLiteral.RO_RATIO + " from "
        + DbLiteral.RATIO_OFFSET;
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    while (resultSet.next()) {
      Object[] objArr = new Object[4];
      objArr[0] = resultSet.getInt(DbLiteral.M_NUM);
      objArr[1] = resultSet.getInt(DbLiteral.BL_NUM);
      objArr[2] = resultSet.getInt(DbLiteral.BE_MINOR);
      objArr[3] = resultSet.getFloat(DbLiteral.RO_RATIO);
      objArrList.add(objArr);
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][4]);
  }

  /**
   * 
   * @return Object[][2] = {{m_num, m_name}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getMaps() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();

    // 쿼리 실행
    String sql = "select " + DbLiteral.M_NUM + ", " + DbLiteral.M_NAME
        + " from " + DbLiteral.MAP;
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    while (resultSet.next()) {
      Object[] objArr = new Object[2];
      objArr[0] = resultSet.getInt(DbLiteral.M_NUM);
      objArr[1] = resultSet.getString(DbLiteral.M_NAME);
      objArrList.add(objArr);
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][2]);
  }

  /**
   * 
   * @return Object[][4] = {{m_num1, bl_num1, m_num2, bl_num2}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getShareBlocks() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();

    // 쿼리 실행
    String sql = "select " + DbLiteral.M_NUM1 + ", " + DbLiteral.BL_NUM1 + ", "
        + DbLiteral.M_NUM2 + ", " + DbLiteral.BL_NUM2 + " from "
        + DbLiteral.SHARE_BLOCK;
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    while (resultSet.next()) {
      Object[] objArr = new Object[4];
      objArr[0] = resultSet.getInt(DbLiteral.M_NUM1);
      objArr[1] = resultSet.getInt(DbLiteral.BL_NUM1);
      objArr[2] = resultSet.getInt(DbLiteral.M_NUM2);
      objArr[3] = resultSet.getInt(DbLiteral.BL_NUM2);
      objArrList.add(objArr);
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][4]);
  }

  /**
   * 
   * @return Object[][] = {{r_num, r_content, i_num1, i_num2, ...}, ...}
   * @throws SQLException
   *           SQL 관련 예외
   */
  private Object[][] getRecommends() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();

    // 쿼리 실행
    String sql = "select " + DbLiteral.R + "." + DbLiteral.R_NUM + ", "
        + DbLiteral.RI + "." + DbLiteral.SEQ_NUM + ", " + DbLiteral.R + "."
        + DbLiteral.R_CONTENT + ", " + DbLiteral.RI + "." + DbLiteral.I_NUM
        + " from " + DbLiteral.RECOMMEND + " as " + DbLiteral.R + ", "
        + DbLiteral.RECOMMEND_ITEM + " as " + DbLiteral.RI + " where "
        + DbLiteral.R + "." + DbLiteral.R_NUM + "=" + DbLiteral.RI + "."
        + DbLiteral.R_NUM + " order by " + DbLiteral.R_NUM + ", "
        + DbLiteral.SEQ_NUM;
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    List<Object> objList = null;
    int prev = -1;
    while (resultSet.next()) {
      int rNum = resultSet.getInt(DbLiteral.R_NUM);
      if (prev != rNum) {
        if (prev != -1) {
          objArrList
              .add((Object[]) objList.toArray(new Object[objList.size()]));
        }
        objList = new ArrayList<Object>();
        objList.add(rNum);
        objList.add(resultSet.getString(DbLiteral.R_CONTENT));
      }
      objList.add(resultSet.getInt(DbLiteral.I_NUM));
      prev = rNum;
    }
    if (objList != null) {
      objArrList.add((Object[]) objList.toArray(new Object[objList.size()]));
    }

    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][]);
  }
}
