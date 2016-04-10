package harmony.common;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 환경설정 등 dat파일의 내용을 읽는 클래스.<br>
 * 
 * 
 * @author Seongjun Park
 * @since 2016/4/9
 * @version 2016/4/10
 */
public class DatFileReader {
	private Map<String, String> dataMap = new HashMap<String, String>();

	/**
	 * 생성자. 파일을 읽어 데이터 해쉬맵에 저장한다.
	 * 
	 * @param fileName
	 *          dat 파일 이름
	 * @throws IOException
	 *           IO 관련 예외
	 */
	public DatFileReader(String fileName) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
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
