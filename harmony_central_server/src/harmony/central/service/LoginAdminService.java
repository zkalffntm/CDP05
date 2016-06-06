package harmony.central.service;

import java.io.IOException;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONException;

import harmony.central.controller.AccountController;
import harmony.central.controller.ExhibitionController;
import harmony.central.model.Account;
import harmony.central.model.Exhibition;
import harmony.common.AbstractService;

public class LoginAdminService extends AbstractService {

  /**
   * 
   * @param argument
   *          JSONArray = {id, password}
   * @return true or false
   * @throws SQLException
   *           SQL 관련 예외
   * @throws IOException
   *           IO 관련 예외
   * @throws JSONException
   *           JSON 관련 예외
   */
  @Override
  public Object doService(Object argument)
      throws SQLException, IOException, JSONException {

    // 계정 정보
    JSONArray accountInfo = (JSONArray) argument;
    String id = accountInfo.getString(0);
    String password = accountInfo.getString(1);
    // String ip = accountInfo.getString(2);

    Account account = AccountController.getAccountByIdAndPassword(id, password);
    if (account != null) {
      Exhibition exhibition = ExhibitionController
          .getExhibitionByNum(account.getExhibitionNum());
      if (exhibition != null /* && exhibition.getIp().equals(ip) */) {
        return true;
      }
    }

    return false;
  }
}
