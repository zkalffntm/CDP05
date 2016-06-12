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
   * �̹��� ���� ������ ����Ʈ�� ��ȯ �� �̸� String Ÿ������ ��ȯ�Ͽ� ��ȯ
   * 
   * @param imageFileName
   *          �̹��� ���� ���
   * @return �̹��� ����Ʈ ��Ʈ��
   * @throws IOException
   *           IO ���� ����
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
   * String�� ����Ʈȭ�� �� �̸� �̹��� ���Ϸ� ��.
   * 
   * @param byteString
   *          ����Ʈ ��ȯ �� ���ڿ�
   * @param imageFileName
   *          �̹��� ���� ���
   * @throws IOException
   *           IO ���� ����
   */
  public static void writeImageFromByteString(String byteString,
      String imageFileName) throws IOException {
    FileOutputStream fileOutputStream = new FileOutputStream(
        new File(imageFileName));
    fileOutputStream.write(Base64.decode(byteString, Base64.DEFAULT));
    fileOutputStream.close();
  }
}