import java.sql.*;
import java.util.Random;

public class InsertData {

    static String[] firstNames = {"Alan", "Ada", "Charles", "Jane", "Patrick", "Dev"};

    static String[] lastNames = {
           "Turing", "Babbage", "Smith", "Lovelace", "Krishnamurthi", "Stewart", "Jones", "O'Reilly"};

    public static void main(String[] args)
            throws ClassNotFoundException, SQLException {
        Random rng = new Random();
        Class.forName("org.h2.Driver");
        Connection conn = DriverManager.getConnection(Config.JDBC_URL);
//        Statement stm = conn.createStatement();
        String randomFirstName = firstNames[rng.nextInt(firstNames.length)];
        String randomLastName = lastNames[rng.nextInt(lastNames.length)];
        PreparedStatement stm = conn.prepareStatement(
                "INSERT INTO student (first, last) "+
                "VALUES (?, ?)");
        stm.setString(1, randomFirstName);
        stm.setString(2, randomLastName);
        int numRows = stm.executeUpdate();
        if(numRows > 0) {
            System.out.printf("Added %s %s\n",
                    randomFirstName, randomLastName);
        }
        else {
            System.out.println("Hmmm, numRows <= 0!!!");
        }
    }
}
