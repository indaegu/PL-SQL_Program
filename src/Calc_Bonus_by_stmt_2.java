import java.sql.*;
import java.util.Objects;
import java.util.Properties;

/**
 * @author SCM
 * @설명:
 * 모든 데이터를 가지고온다 (570만건)
 * statement 객체를 한번만 생성하여 모두 처리하고 객체 리소스를 반납한다.
 * 이하 1과 동일
 * @생성일: 2024-05-30 [오전 9:32]
 * @see
 */
public class Calc_Bonus_by_stmt_2 {
    public static void execute(Connection conn) throws SQLException {
        String sql = null; // SQL 쿼리를 담을 문자열 선언
        ResultSet rs = null; // 결과 집합을 담을 ResultSet 선언
        Statement stmt = null; // sql 선언을 담을 Statement 선언

        String id = null; // 사용자 id를 담을 변수
        int creditLimit = 0; // 사용자 한도를 담을 변수
        String email = null; // 사용자 email을 담을 변수
        String coupon = null; // 사용자 보너스 쿠폰을 담을 변수
        String address1 = null; // 사용자 위치를 담을 변수
        String gender = null; // 사용자 성별을 담을 변수
        Date referenceDate = Date.valueOf("2018-01-01"); // 가상머신 : 2023-01-01, 서버 : 2018-01-01

        try(Statement stmt2 = conn.createStatement()) { // try - with - resource 문으로 Statement 자동 close
            System.out.println();
            System.out.println("========Calc_Bonus_by_stmt_2 시작========");

            // 1단계 쿼리 생성 및 실행
            System.out.println("기존 BONUS_COUPON 데이터 삭제중...");
            sql = "TRUNCATE TABLE BONUS_COUPON"; // 보너스 테이블 데이터 삭제
            stmt = conn.createStatement(); // 쿼리 생성
            stmt.execute(sql); // 쿼리 실행

            long startTime = System.currentTimeMillis(); // 시작 시간 측정

            sql = "SELECT * FROM customer"; // 570만건 데이터 가져오기
            stmt = conn.createStatement(); // 쿼리 생성
            stmt.executeQuery(sql);// 쿼리 실행


            // 2단계 데이터 Fetch
            System.out.println("customer 데이터 패치중...");
            rs = stmt.getResultSet(); // 결과집합을 담아옴
            while (rs.next()){ // 570 만건의 데이터 만큼 반복
                if(rs.getDate("ENROLL_DT").after(referenceDate)){
                    id = rs.getString("ID");
                    email = rs.getString("EMAIL");
                    creditLimit = rs.getInt("CREDIT_LIMIT");
                    address1 = rs.getString("ADDRESS1");
                    gender = rs.getString("GENDER");
                    coupon = creditLimit < 1000 ? "AA" :
                            (creditLimit >= 1000 && creditLimit <= 2999) ? "BB" :
                                    (creditLimit >= 3000 && creditLimit <= 3999) ?
                                            (address1.contains("송파구 풍납1동") && gender.equals("F")) ? "C2" : "CC" :
                                            (creditLimit >= 4000) ? "DD" : null;

                    sql = "INSERT INTO BONUS_COUPON(YYYYMM, CUSTOMER_ID, EMAIL,COUPON, SEND_DT)" +
                            "values ('" + 202406 + "','" + id + "','" + email + "','" + coupon + "',sysdate)";
                    stmt2.executeLargeUpdate(sql);
                    conn.commit(); // 매번 commit
                }
            }

            // 3단계 종료 시간 기록
            long endTime = System.currentTimeMillis(); // 종료 시간 측정
            System.out.println("Calc_Bonus_by_stmt_2 경과 시간 : " + (endTime - startTime) + "ms");
            System.out.println("========Calc_Bonus_by_stmt_2 정상 종료========");
            System.out.println();

            // 4단계 검증 로직
            System.out.println("Calc_Bonus_by_stmt_2 검증 진행중...");
            sql = "SELECT COUPON, COUNT(*) AS count FROM bonus_coupon GROUP BY COUPON";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            // 결과 출력
            while (rs.next()) {
                String couponCode = rs.getString("COUPON");
                int countCoupon = rs.getInt("count");
                System.out.println("쿠폰 코드 [" + couponCode + "] 발행 개수: " + countCoupon);
            }

            // 총 쿠폰 발행 개수
            sql = "select count(*) as count  from bonus_coupon";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int countAll = rs.getInt("count");
                System.out.println("총 쿠폰 발행 개수: " + countAll);
            }

            System.out.println("Calc_Bonus_by_stmt_2 검증 완료");
            System.out.println();

            // 메일 보내기
            String mailContent = "걸린 시간 : ".concat(String.valueOf(endTime - startTime) + "ms");
            SendMail.goMail(SendMail.setting(new Properties(),"hanium124@naver.com","@hanium124"),
                    "Calc_Bonus_by_stmt_1 실행완료", mailContent);

        }catch (Exception e) {
            conn.rollback();
            e.printStackTrace(); // 에러 발생시 에러 메시지 출력
            System.out.println("Calc_Bonus_by_stmt_2 실행중 에러 발생 : " + e.getMessage());
            SendMail.goMail(SendMail.setting(new Properties(),"hanium124@naver.com","@hanium124"),
                    "Calc_Bonus_by_stmt_2 에러 발생", e.getMessage());
            LogWriter.writeLog("Calc_Bonus_by_stmt_2 에러 발생", e.getMessage());

        }finally {
            Objects.requireNonNull(stmt).close(); // 사용한 객체 반환
            Objects.requireNonNull(rs).close(); // 사용한 객체 반환
        }
    }
}
