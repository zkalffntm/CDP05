package harmony.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 추천경로 클래스. JSON으로 전송하는 대신 직렬화하여 사용한다.
 * 
 * @author Seongjun Park
 * @since 2016/4/13
 * @version 2016/5/9
 */
public class RecommandRoute implements Serializable {
  public static final String DEFAULT_RECOMMAND_ROUTE_FILE = "dat"
      + File.separator + "recomm.dat";
  private static final long serialVersionUID = 4833435859100442715L;
  private String description;
  private List<Integer> itemList = new ArrayList<Integer>();

  /**
   * 
   */
  public RecommandRoute() {
  }

  /**
   * 
   * @param description
   * @param items
   */
  public RecommandRoute(String description, Integer... items) {
    this.description = description;
    this.itemList = new ArrayList<Integer>(Arrays.asList(items));
  }

  /**
   * 
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public static List<RecommandRoute> load()
      throws FileNotFoundException, IOException, ClassNotFoundException {
    return load(DEFAULT_RECOMMAND_ROUTE_FILE);
  }

  /**
   * 
   * @param filePath
   * @return
   * @throws FileNotFoundException
   * @throws IOException
   * @throws ClassNotFoundException
   */
  @SuppressWarnings("unchecked")
  public static List<RecommandRoute> load(String filePath)
      throws FileNotFoundException, IOException, ClassNotFoundException {
    ObjectInputStream ois = new ObjectInputStream(
        new FileInputStream(filePath));
    List<RecommandRoute> recommandRouteList = (List<RecommandRoute>) ois
        .readObject();
    ois.close();
    return recommandRouteList;
  }

  /**
   * 
   * @param recommandRouteList
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void save(List<RecommandRoute> recommandRouteList)
      throws FileNotFoundException, IOException {
    save(recommandRouteList, DEFAULT_RECOMMAND_ROUTE_FILE);
  }

  /**
   * 
   * @param recommandRouteList
   * @param filePath
   * @throws FileNotFoundException
   * @throws IOException
   */
  public static void save(List<RecommandRoute> recommandRouteList,
      String filePath) throws FileNotFoundException, IOException {
    ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(filePath));
    oos.writeObject(recommandRouteList);
    oos.flush();
    oos.close();
  }

  public static List<RecommandRoute> convertJsonToList(JSONArray jsonArray)
      throws JSONException {
    List<RecommandRoute> rrList = new ArrayList<RecommandRoute>();
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject json = jsonArray.getJSONObject(i);
      RecommandRoute rr = new RecommandRoute();

      // set description
      rr.setDescription(json.getString("description"));

      // set item number list
      JSONArray itemJsonArray = json.getJSONArray("itemList");
      for (int j = 0; j < itemJsonArray.length(); j++) {
        rr.getItemList().add(itemJsonArray.getInt(j));
      }

      // add a entry to list
      rrList.add(rr);
    }

    return rrList;
  }

  public String getDescription() {
    return this.description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Integer> getItemList() {
    return this.itemList;
  }
}
