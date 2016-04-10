package harmony.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 이미지 파일 입출력을 보조하는 클래스
 * 
 * @author Seongjun Park
 * @since 2016/4/10
 * @since 2016/4/10
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
	public static String readByteStringFromImage(String imageFileName) throws IOException {
		File file = new File(imageFileName);
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];

		fileInputStream.read(buffer);

		String byteString = Base64.getEncoder().encodeToString(buffer);
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
	public static void writeImageFromByteString(String byteString, String imageFileName) throws IOException {
		FileOutputStream fileOutputStream = new FileOutputStream(new File(imageFileName));
		fileOutputStream.write(Base64.getDecoder().decode(byteString));
		fileOutputStream.close();
	}
}
