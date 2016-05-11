package harmony.admin.test;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import harmony.common.RecommandRoute;

public class Test {
  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    List<RecommandRoute> listRR = new ArrayList<>();
    listRR.add(new RecommandRoute("test1", 1, 2, 3));
    listRR.add(new RecommandRoute("test2", 4, 5, 6, 7));
    listRR.add(new RecommandRoute("test3", 8, 9));

    JSONObject json = new JSONObject();
    json.put("value", listRR);

    List<RecommandRoute> listRR2 = RecommandRoute
        .convertJsonToList(json.getJSONArray("value"));

    for(RecommandRoute rr : listRR2) {
      System.out.println(rr.getDescription());
      for(int item : rr.getItemList()) {
        System.out.print(item + " ");
      }
      System.out.println();
    }

  }
}
