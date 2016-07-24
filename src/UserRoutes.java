import spark.ModelAndView;
import spark.Route;
import spark.TemplateViewRoute;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserRoutes {

    private Connection cn;

    UserRoutes(Connection cn) {
        this.cn = cn;
    }

    TemplateViewRoute listPage = (request, response) -> {
        Statement q = cn.createStatement();
        ResultSet rs = q.executeQuery("SELECT * FROM student ORDER BY last, first");
        ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String, Object>>();
        while(rs.next()) {
            HashMap<String,Object> student = new HashMap<String, Object>();
            student.put("id", rs.getInt("id"));
            student.put("first", rs.getString("first"));
            student.put("last", rs.getString("last"));
            student.put("created", rs.getDate("created"));
            list.add(student);
        }
        HashMap<String,Object> model = new HashMap<String, Object>();
        model.put("students", list);
        return new ModelAndView(model, "users.html");
    };

    Route detailPage = (request, response) -> {
        int id = Integer.parseInt(request.params(":id"));
        PreparedStatement stm = cn.prepareStatement("SELECT * FROM student WHERE id=?");
        stm.setInt(1, id);
        ResultSet rs = stm.executeQuery();
        if (rs.next()) {
            String name = rs.getString("first") + " " + rs.getString("last");
            Date created = rs.getTimestamp("created");
            return "<h1>" + name + "</h1><p>Created: " + created + "</p>";
        } else {
            response.status(404);
            return "Student not found";
        }
    };

}
