package harmony.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 추천경로 클래스. JSON으로 전송하는 대신 직렬화하여 사용한다.
 * 
 * @author Seongjun Park
 * @since 2016/4/13
 * @version 2016/4/13
 */
public class RecommandRoute implements Serializable {
  private static final long serialVersionUID = 4833435859100442715L;
  private String description;
  private List<Integer> itemList = new ArrayList<Integer>();

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
