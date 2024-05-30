import java.sql.Connection;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author SCM
 * @설명: 쉽게 테스트 해보기 위한 main 클래스
 * 로컬에서 할때는 20230101 기준으로 조회
 * @생성일: 2024-05-30 [오전 9:20]
 * @see
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // 한번 Connection을 맺어 여러 테스트에 재사용함으로서 각기 다른 Connection 시간으로 인해 생기는 오차를 방지
        try (Connection conn = DBConnector.getConnectionLocal()) { // try - with - resource 문으로 Connection 자동
            // close
            Objects.requireNonNull(conn).setAutoCommit(false); // auto commit 해제
            String mode = "";
            while (!mode.equals("q")) {
                printMainMenu();
                mode = sc.nextLine();
                switch (mode) {
                    case "1" -> Calc_Bonus_by_stmt_1.execute(conn);
                    case "2" -> Calc_Bonus_by_stmt_2.execute(conn);
                    case "3" -> Calc_Bonus_by_stmt_3.execute(conn);
                    case "4" -> Calc_Bonus_by_stmt_4.execute(conn);
                    case "5" -> Calc_Bonus_by_stmt_5.execute(conn);
                    case "6" -> Calc_Bonus_by_pstmt_1.execute(conn);
                    case "7" -> Calc_Bonus_by_pstmt_2.execute(conn);
                    case "8" -> Calc_Bonus_by_Callstmt_1.execute(conn);
                    case "9" -> Calc_Bonus_by_Callstmt_2.execute(conn);
                    case "10" -> Calc_Bonus_by_Callstmt_3.execute(conn);
                    case "11" -> {
                        Calc_Bonus_by_stmt_1.execute(conn);
                        Calc_Bonus_by_stmt_2.execute(conn);
                        Calc_Bonus_by_stmt_3.execute(conn);
                        Calc_Bonus_by_stmt_4.execute(conn);
                        Calc_Bonus_by_stmt_5.execute(conn);
                        Calc_Bonus_by_pstmt_1.execute(conn);
                        Calc_Bonus_by_pstmt_2.execute(conn);
                        Calc_Bonus_by_Callstmt_1.execute(conn);
                        Calc_Bonus_by_Callstmt_2.execute(conn);
                        Calc_Bonus_by_Callstmt_3.execute(conn);
                    }
                    case "q" -> System.out.println("프로그램을 종료합니다.");
                    default -> System.out.println("잘못된 메뉴 선택입니다.");
                }
            }
        } catch (Exception e) {
            System.out.println("메뉴 선택 중 에러발생 : " + e.getMessage()); // 사용자용 에러 로그
        }
    }

    private static void printMainMenu() {
        System.out.print(
                """
                        ===== SQL 성능 테스트 프로그램 =====
                        1. Calc_Bonus_by_stmt_1 : 굉장히 오래걸리는 프로그램
                        2. Calc_Bonus_by_stmt_2 : Statement 객체를 한번만 생성하는 프로그램
                        3. Calc_Bonus_by_stmt_3 : Server에서 필요한 데이터만 가져오는 프로그램
                        4. Calc_Bonus_by_stmt_4 : 특정 Size만큼 insert 후 commit
                        5. Calc_Bonus_by_stmt_5 : Server에서 필요한 데이터를 가져올때 Fetch Size 만큼만 가져오기
                        6. Calc_Bonus_by_pstmt_1 : PrepareStatement로 Soft parsing 하는 프로그램
                        7. Calc_Bonus_by_pstmt_2 : Batch를 사용하는 프로그램
                        8. Calc_Bonus_by_Callstmt_1 : PL/SQL로 1개씩 Fetch 하는 프로그램
                        9. Calc_Bonus_by_Callstmt_2 : PL/SQL로 1000개씩 Fetch 하는 프로그램
                        10. Calc_Bonus_by_Callstmt_3 : 한방 쿼리
                        11. 모두 돌리기
                        q. 프로그램 종료
                        메뉴를 선택하세요:\s """
        );
    }
}