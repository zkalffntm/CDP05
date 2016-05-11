package harmony.admin.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import harmony.common.RecommandRoute;

/**
 * 
 * @author Seogjun Park
 * @since 2016/5/9
 * @version 2016/5/9
 */
public class TestRecommandMaker {
  /**
   * 
   * @param args
   * @throws IOException
   * @throws FileNotFoundException
   * @throws ClassNotFoundException
   */
  public static void main(String[] args)
      throws FileNotFoundException, IOException, ClassNotFoundException {
    // writeTest();
    readTest();
  }

  private static void writeTest() throws FileNotFoundException, IOException {
    // test create
    List<RecommandRoute> listRR = new ArrayList<RecommandRoute>();
    listRR.add(new RecommandRoute("test1", 1, 2, 3));
    listRR.add(new RecommandRoute("test2", 4, 5, 6, 7, 8));
    listRR.add(new RecommandRoute("test3", 9, 10, 11, 12, 13, 14, 15));
    RecommandRoute.save(listRR);
  }

  private static void readTest()
      throws FileNotFoundException, IOException, ClassNotFoundException {
    List<RecommandRoute> listRR = RecommandRoute.load();

    for (RecommandRoute rr : listRR) {
      System.out.println(rr.getDescription());
      for (int i : rr.getItemList()) {
        System.out.print(i + " ");
      }
      System.out.println();
    }
  }
}
