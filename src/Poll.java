import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Poll {
    int id;
    String question;

    public Poll(ResultSet rs) throws SQLException {
        id = rs.getInt("pollid");
        question = rs.getString("question");
    }

    public ArrayList<HashMap<String,Object>> getChoicesAndVotes(Connection cn) throws SQLException {
        PreparedStatement stm = cn.prepareStatement(
                "select choice.choiceid, choice.text, count(vote.choiceid) as num\n" +
                "from choice left outer join vote on choice.choiceid = vote.choiceid\n" +
                "where choice.pollid=?\n" +
                "group by choice.choiceid;"
        );
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        ArrayList<HashMap<String,Object>> list = new ArrayList<>();
        while(rs.next()) {
            HashMap<String,Object> choice = new HashMap<>();
            choice.put("text", rs.getString("text"));
            choice.put("id", rs.getInt("choiceid"));
            choice.put("votes", rs.getInt("num"));
            list.add(choice);
        }
        return list;
    }

    public static Poll getById(Connection cn, int id) throws SQLException {
        PreparedStatement stm = cn.prepareStatement("SELECT * FROM poll WHERE pollid = ?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            return new Poll(rs);
        } else {
            return null;
        }
    }
}
