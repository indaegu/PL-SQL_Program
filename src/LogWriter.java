import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * @author SCM
 * @설명:
 * @생성일: 2024-05-31 [오전 9:38]
 * @see
 */
public class LogWriter {
    /*
        로그를 작성해주는 함수
     */
    public static void writeLog(String project, String err){
        BufferedWriter writer = null;
        try{
            // 1. 파일 객체 생성
            File file = new File("C:\\intelijproject\\Calc_Bonus_2460340010_SungChangMin\\src\\Log.txt");

            // 2. 파일 존재여부 체크 및 생성
            if (!file.exists()) {
                file.createNewFile();
            }

            // 3. Buffer를 사용해서 File에 write할 수 있는 BufferedWriter 생성
            FileWriter fw = new FileWriter(file, true);
            writer = new BufferedWriter(fw);

            //4. 시간 포멧 설정
            LocalTime now = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH시 mm분 ss초");
            String formatedNow = now.format(formatter);

            //5. 에러 로그 작성
            writer.write(formatedNow + " : " + project +  " -> " + err + "\n");
            System.out.println("로그 작성 완료");
        }
        catch (Exception e){
            System.out.println("로그 작성중 에러 발생 : " + e.getMessage());
        }
        finally {
            try {
                if (writer != null) {
                    writer.close(); // BufferedWriter를 닫아줍니다.
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("로그 파일 닫기 실패 : " + e.getMessage());
            }
        }
    }
}
