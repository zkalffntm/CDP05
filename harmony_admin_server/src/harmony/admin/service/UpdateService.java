package harmony.admin.service;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

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
   * @return 
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
    
    // [0]은 item, [1]은 block, [2]는 ratio_offset, [3]은 map, [4]는 share_block
    Object[][][] retval = new Object[5][][];
    retval[0] = this.getItems();
    retval[1] = this.getBlocks();
    retval[2] = this.getRatioOffsets();
    retval[3] = this.getMaps();
    retval[4] = this.getShareBlocks();
    
    return retval;    
  }
  
  /**
   * 
   * @return
   * @throws SQLException
   */
  private Object[][] getItems() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();
    
    // 쿼리 실행
    String sql = "select i_num, m_num, b_num from item";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    Object[] objArr = null;
    while (resultSet.next()) {
      objArr = new Object[3];
      objArr[0] = resultSet.getInt("i_num");
      objArr[1] = resultSet.getInt("m_num");
      objArr[2] = resultSet.getInt("b_num");
      objArrList.add(objArr);
    }
    
    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][3]);
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private Object[][] getBlocks() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();
    
    // 쿼리 실행
    String sql = "select m_num, b_num from block";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    Object[] objArr = null;
    while (resultSet.next()) {
      objArr = new Object[2];
      objArr[0] = resultSet.getInt("m_num");
      objArr[1] = resultSet.getInt("b_num");
      objArrList.add(objArr);
    }
    
    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][2]);
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private Object[][] getRatioOffsets() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();
    
    // 쿼리 실행
    String sql = "select m_num, b_num, ro_beacon_minor, ro_ratio from ratio_offset";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    Object[] objArr = null;
    while (resultSet.next()) {
      objArr = new Object[4];
      objArr[0] = resultSet.getInt("m_num");
      objArr[1] = resultSet.getInt("b_num");
      objArr[2] = resultSet.getInt("ro_beacon_minor");
      objArr[3] = resultSet.getFloat("ro_ratio");
      objArrList.add(objArr);
    }
    
    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][4]);
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private Object[][] getMaps() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();
    
    // 쿼리 실행
    String sql = "select m_num, m_name from map";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    Object[] objArr = null;
    while (resultSet.next()) {
      objArr = new Object[2];
      objArr[0] = resultSet.getInt("m_num");
      objArr[1] = resultSet.getString("m_name");
      objArrList.add(objArr);
    }
    
    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][2]);
  }

  /**
   * 
   * @return
   * @throws SQLException
   */
  private Object[][] getShareBlocks() throws SQLException {
    List<Object[]> objArrList = new ArrayList<Object[]>();
    
    // 쿼리 실행
    String sql = "select m_num1, b_num1, m_num2, b_num2 from share_block";
    PreparedStatement pstmt = this.getDbConnection().prepareStatement(sql);
    ResultSet resultSet = pstmt.executeQuery();

    // 결과 레코드를 객체에 저장
    Object[] objArr = null;
    while (resultSet.next()) {
      objArr = new Object[4];
      objArr[0] = resultSet.getInt("m_num1");
      objArr[1] = resultSet.getInt("b_num1");
      objArr[2] = resultSet.getInt("m_num2");
      objArr[3] = resultSet.getInt("b_num2");
      objArrList.add(objArr);
    }
    
    // List<Object[]> to Object[][]
    return (Object[][]) objArrList.toArray(new Object[objArrList.size()][4]);
  }
}
