package tools;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.util.Base64;

/**
 * This is a slightly edited version of ImageManager(by Seongjun Park)
 * for android compatibility.
 * 
 * @author Kyuho (original version - Seongjun Park)
 * @see https://github.com/zkalffntm/CDP05/blob/seongjun/harmony_admin_server/src/harmony/common/ImageManager.java
 */
public class ImageManager {

  /**
   * 이미지 파일 내용을 바이트로 변환 후 이를 String 타입으로 변환하여 반환
   * 
   * @param imageFileName
   *          이미지 파일 경로
   * @return 이미지 바이트 스트림
   * @throws IOException
   *           IO 관련 예외
   */
  public static String readByteStringFromImage(String imageFileName)
      throws IOException {
    if ("".equals(imageFileName)) {
      return imageFileName;
    }

    File file = new File(imageFileName);
    FileInputStream fileInputStream = new FileInputStream(file);
    byte[] buffer = new byte[(int) file.length()];

    fileInputStream.read(buffer);

    String byteString = Base64.encodeToString(buffer, Base64.DEFAULT);
    fileInputStream.close();

    return byteString;
  }

  /**
   * String을 바이트화한 후 이를 이미지 파일로 씀.
   * 
   * @param byteString
   *          바이트 변환 전 문자열
   * @param imageFileName
   *          이미지 파일 경로
   * @throws IOException
   *           IO 관련 예외
   */
  public static void writeImageFromByteString(String byteString,
      String imageFileName) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(
        new File(imageFileName));
    fileOutputStream.write(Base64.decode(byteString, Base64.DEFAULT));
    fileOutputStream.close();
  }
}