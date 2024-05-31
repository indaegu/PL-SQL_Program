import java.sql.*;
import java.util.Objects;
import java.util.Properties;

/**
 * @author SCM
 * @설명:
 * 하나의 sql로 모든 걸 처리
 * @생성일: 2024-05-30 [오전 9:38]
 * @see
 */
public class Calc_Bonus_by_Callstmt_3 {
    public static void execute(Connection conn) throws SQLException {
        String sql = null; // SQL 쿼리를 담을 문자열 선언
        ResultSet rs = null; // 결과 집합을 담을 ResultSet 선언
        Statement stmt = null; // sql 선언을 담을 Statement 선언
        CallableStatement cstmt = null; // 쿼리를 담을 CallableStatement 선언
        try {
            System.out.println();
            System.out.println("========Calc_Bonus_by_Callstmt_3 시작========");

            System.out.println("기존 BONUS_COUPON 데이터 삭제중...");
            sql = "TRUNCATE TABLE BONUS_COUPON"; // 보너스 테이블 데이터 삭제
            stmt = conn.createStatement(); // 쿼리 생성
            stmt.execute(sql); // 쿼리 실행

            long startTime = System.currentTimeMillis(); // 시작 시간 측정
            System.out.println("한방 쿼리 수행중...");
            sql =   "BEGIN " +
                    "INSERT INTO bonus_coupon (yyyymm, customer_id, email, coupon, credit_point, send_dt)\n" +
                    "SELECT\n" +
                    "  '202406' AS yyyymm,\n" +
                    "  c.id AS customer_id,\n" +
                    "  c.email AS email,\n" +
                    "  CASE\n" +
                    "    WHEN c.credit_limit < 1000 THEN 'AA'\n" +
                    "    WHEN c.credit_limit BETWEEN 1000 AND 2999 THEN 'BB'\n" +
                    "    WHEN c.credit_limit BETWEEN 3000 AND 3999 THEN\n" +
                    "      CASE\n" +
                    "        WHEN c.address1 LIKE '%송파구 풍납1동%' AND c.gender = 'F' THEN 'C2'\n" +
                    "        ELSE 'CC'\n" +
                    "      END\n" +
                    "    WHEN c.credit_limit >= 4000 THEN 'DD'\n" +
                    "    ELSE NULL\n" +
                    "  END AS coupon,\n" +
                    "  NULL AS credit_point, -- 이 필드는 예제에 포함되지 않은 값으로 NULL을 지정\n" +
                    "  SYSDATE AS send_dt -- 현재 날짜로 send_dt 설정\n" +
                    "FROM\n" +
                    "  customer c\n" +
                    "WHERE\n" +
                    "  c.enroll_dt >= TO_DATE('2013/01/01', 'YYYY/MM/DD');\n" +
                    "COMMIT;\n" +
                    "END;";

            cstmt = conn.prepareCall(sql); //(sql);
            cstmt.execute();

            // 3단계 종료 시간 기록
            long endTime = System.currentTimeMillis(); // 종료 시간 측정
            System.out.println("Calc_Bonus_by_Callstmt_3 경과 시간 : " + (endTime - startTime) + "ms");
            System.out.println("========Calc_Bonus_by_Callstmt_3 정상 종료========");
            System.out.println();

            // 4단계 검증 로직
            System.out.println("Calc_Bonus_by_Callstmt_3 검증 진행중...");
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

            System.out.println("Calc_Bonus_by_Callstmt_3 검증 완료");
            System.out.println();

            String mailContent = "걸린 시간 : ".concat(String.valueOf(endTime - startTime) + "ms");

            // 메일 보내기
            SendMail.goMail(SendMail.setting(new Properties(),"hanium124@naver.com","@hanium124"),
                    "Calc_Bonus_by_Callstmt_3 실행완료", mailContent);

        }catch (Exception e) {
            conn.rollback();
            System.out.println("Calc_Bonus_by_Callstmt_3 실행중 에러 발생 : " + e.getMessage());
            SendMail.goMail(SendMail.setting(new Properties(),"hanium124@naver.com","@hanium124"),
                    "Calc_Bonus_by_Callstmt_3 에러 발생", e.getMessage());

        }finally {
            Objects.requireNonNull(stmt).close(); // 사용한 객체 반환
            Objects.requireNonNull(rs).close(); // 사용한 객체 반환
            Objects.requireNonNull(cstmt).close(); // 사용한 객체 반환
        }
    }

}
