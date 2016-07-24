import org.h2.tools.Console;

import java.sql.SQLException;

public class H2ConsoleWrapper {
    public static void main(String[] args) throws SQLException {
        // Console.main("-help");
        // Seems broken on my Mac.
        Console.main("-url", Config.JDBC_URL);
    }
}
