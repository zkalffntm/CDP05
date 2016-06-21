package harmony.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 환경설정 등 dat파일의 내용을 관리하는 클래스.<br>
 * dat 파일 내의 key 값과 value 사이의 '='로 구분하여 해쉬맵에 데이터를 저장한다.<br>
 * <br>
 * - 아래는 dat류 들의 파일 내용 형식 -<br>
 * <br>
 * {@code ...}<br>
 * {@code notice=www.google.com}<br>
 * {@code update=234234234}<br>
 * {@code ...}
 * 
 * @author Seongjun Park
 * @since 2016/4/9
 * @version 2016/4/10
 */
public class DatFileManager {
  public static final String DEFAULT_CONFIG_FILE = "dat" + File.separator
      + "config.dat";
  private String fileName;
  private Map<String, String> dataMap = new HashMap<String, String>();

  /**
   * 생성자. "dat\config.dat" 파일을 읽어 데이터 해쉬맵에 저장한다.
   * 
   * @throws IOException
   *           IO 관련 예외
   */
  public DatFileManager() throws IOException {
    this(DEFAULT_CONFIG_FILE);
  }

  /**
   * 생성자. 파일을 읽어 데이터 해쉬맵에 저장한다.
   * 
   * @param fileName
   *          dat 파일 이름
   * @throws IOException
   *           IO 관련 예외
   */
  public DatFileManager(String fileName) throws IOException {
    this.fileName = fileName;
    BufferedReader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(fileName)));
    String line = null;

    // 라인 별로 읽음
    while ((line = reader.readLine()) != null) {
      int index = -1;
      if ((index = line.indexOf('=')) < 1 || index >= line.length() - 1) {
        continue;
      }
      this.dataMap.put(line.substring(0, index), line.substring(index + 1));
    }

    reader.close();
  }

  /**
   * @throws FileNotFoundException
   *           파일 열기 예외
   */
  public void updateFile() throws FileNotFoundException {
    PrintWriter writer = new PrintWriter(
        new OutputStreamWriter(new FileOutputStream(this.fileName)));

    for (Entry<String, String> data : this.dataMap.entrySet()) {
      writer.println(data.getKey() + ":" + data.getValue());
      writer.flush();
    }

    writer.close();
  }

  /**
   * 파일 데이터 해쉬맵에 필요 데이터를 넣는다.
   * 
   * @param key
   *          데이터의 키 값
   * @param value
   *          데이터의 내용 값
   */
  public void putData(String key, String value) {
    this.dataMap.put(key, value);
  }

  /**
   * 파일 데이터 해쉬맵에서 필요 데이터를 가져온다.
   * 
   * @param key
   *          데이터의 키 값
   * @return 데이터의 내용 값
   */
  public String getData(String key) {
    return this.dataMap.get(key);
  }
}
