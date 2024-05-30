import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author SCM
 * @설명:
 * @생성일: 2024-05-30 [오전 09:30]
 * @see
 */
public class DBConnector {


    /**
     * KOPO_DA2 서버에 접속하는 메서드
     * @return
     */
    public static Connection getConnectionKOPO_DA2() {
        // 로컬 데이터베이스 연결 정보
        String url = "jdbc:oracle:thin:@192.168.217.206:1521/KOPODA";
        String user = "da2410";
        String password = "da10";

        try {
            System.out.println("KOPO_DA2 DBMS에 연결합니다.");
            // 데이터베이스 연결 및 반환
            return DriverManager.getConnection(url, user, password); // 로컬 연결 객체 반환
        } catch (SQLException e) {
            System.out.println("KOPO_DA2 데이터베이스 연결 오류 : " + e.getMessage());
            return null;
        }
    }

    /**
     * 로컬 가상머신에 접속하는 메서드
     * @return
     */
    public static Connection getConnectionLocal() {
        // 로컬 데이터베이스 연결 정보
        String url = "jdbc:oracle:thin:@192.168.119.119:1521/dinkdb";
        String user = "scott";
        String password = "tiger";

        try {
            System.out.println("Local DBMS에 연결합니다.");
            // 데이터베이스 연결 및 반환
            return DriverManager.getConnection(url, user, password); // 로컬 연결 객체 반환
        } catch (SQLException e) {
            System.out.println("Local 데이터베이스 연결 오류 : " + e.getMessage());
            return null;
        }
    }

    /**
     * 클라우드 DB에 접속하는 메서드
     * @return
     */
    public static Connection getConnectionCloud() {
        // 클라우드 데이터베이스 연결 정보(전자 지갑)
        String urlCloud = "jdbc:oracle:thin:@(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.ap-seoul-1.oraclecloud.com))(connect_data=(service_name=g56e711c2a2b221_dinkdb_medium.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))";
        String userCloud = "DA2410";
        String passwordCloud = "Data2410";
        try {
            System.out.println("Cloud DBMS에 연결합니다.");
            // 데이터베이스 연결 및 반환
            return DriverManager.getConnection(urlCloud, userCloud, passwordCloud); // 클라우드 연결 객체 반환
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Cloud 데이터베이스 연결 오류 : " + e.getMessage());
            return null;
        }
    }





}
