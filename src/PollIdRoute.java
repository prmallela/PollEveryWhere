import spark.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class PollIdRoute implements TemplateViewRoute {

    private Connection cn;

    PollIdRoute(Connection cn) {
        this.cn = cn;
    }


    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
        int pollID = Integer.parseInt(request.params(":pollID"));
        Poll poll = Poll.getById(cn, pollID);
        if(poll == null) {
            response.status(404);
            return new ModelAndView(null, "404.html");
        }
        ArrayList<HashMap<String,Object>> choices =
                poll.getChoicesAndVotes(cn);
        // Temporary: google image chart
        StringBuilder chart = new StringBuilder();
        chart.append("https://chart.googleapis.com/chart?");
        chart.append("cht=bhs"); // Horizontal bar chart
        chart.append("&chco=3385ff,FF0000|00FF00|0000FF"); // Colors
        chart.append("&chs=200x125"); // Size
        chart.append("&chxt=x,y");
        chart.append("&chd=t:");  // DATA
        boolean first = true;
        for(HashMap<String,Object> choice : choices) {
            if(!first) { chart.append(','); }
            else first = false;
            chart.append(choice.get("votes"));
        }
        chart.append("&chxl=1:");
        for(int i = choices.size()-1; i >= 0; i--) {
            chart.append("|" + choices.get(i).get("text"));
        }

        HashMap<String,Object> model = new HashMap<>();
        model.put("question", poll.question);
        model.put("choices", choices);
        model.put("pollid", pollID);
        model.put("chart", chart);
        try{
        PreparedStatement wu = cn.prepareStatement("select fname,lname from registration where email=?");
        wu.setString(1,request.session().attribute("email").toString());
        ResultSet wur=wu.executeQuery();
        String name="Stranger";
        if(wur.next()){
            name=wur.getString(1);
            name=name+" "+wur.getString(2);
        }
        model.put("name",name);
    }catch (Exception x){
        model.put("name",request.session().attribute("email"));

    }
        return new ModelAndView(model, "poll.html");
    }

    TemplateViewRoute listPolls = (request, response) -> {
        // Retrieve all polls
        PreparedStatement stm = cn.prepareStatement("SELECT * FROM poll");
        ResultSet rs = stm.executeQuery();
        HashMap<String, Object> model = new HashMap<>();
        ArrayList<HashMap<String,Object>> polls = new ArrayList<>();
        while(rs.next()) {
            HashMap<String,Object> onePoll = new HashMap<>();
            onePoll.put("question", rs.getString("question"));
            onePoll.put("id", rs.getInt("pollid"));
            polls.add(onePoll);
        } try{
        PreparedStatement wu = cn.prepareStatement("select fname,lname from registration where email=?");
        wu.setString(1,request.session().attribute("email").toString());
        ResultSet wur=wu.executeQuery();
        String name="Stranger";
        if(wur.next()){
            name=wur.getString(1);
            name=name+" "+wur.getString(2);
        }
        model.put("name",name);
        }catch (Exception x){
            model.put("name",request.session().attribute("email"));

        }
        model.put("polls", polls);
        return new ModelAndView(model, "polls.html");
    };

    Route addChoice = (request, response) -> {
        int pollID = Integer.parseInt(request.params(":pollID"));
        Map<String,String> q = SparkDemo.splitQuery(request.body());
        String choiceText = q.get("choice");
        PreparedStatement stm = cn.prepareStatement("INSERT INTO choice(text, pollid) VALUES (?,?)");
        stm.setString(1, choiceText);
        stm.setInt(2, pollID);
        stm.executeUpdate();
        response.redirect(Paths.showPoll(pollID));
        return null;
    };

    Route addpoll = (request, response) -> {
        Map<String,String> q = SparkDemo.splitQuery(request.body());
        String pollQues = q.get("Question");
        Random random = new Random();
        String TextMsg="TXT"+random.nextInt(100000);
        PreparedStatement stm = cn.prepareStatement("INSERT INTO poll(Question,TEXTMSG) VALUES (?,?)");
        stm.setString(1, pollQues);
        stm.setString(2, TextMsg);
        stm.executeUpdate();
        response.redirect("/polls");
        return null;
    };


    Route rmChoice = (request, response) -> {
        int pollID = Integer.parseInt(request.params(":pollId"));
        int choiceID = Integer.parseInt(request.params(":choiceId"));
        PreparedStatement stm = cn.prepareStatement(
                "DELETE FROM choice WHERE choiceid=?"
        );
        stm.setInt(1, choiceID);
        stm.executeUpdate(); // what if it fails?
        response.redirect(Paths.showPoll(pollID));
        return null;
    };

    Route rmPoll = (request, response) -> {
        int pollID = Integer.parseInt(request.params(":pollID"));
        PreparedStatement stm = cn.prepareStatement(
                "DELETE FROM poll WHERE pollid=?"
        );
        stm.setInt(1,pollID);
        stm.executeUpdate();
        response.redirect(Paths.LIST_POLLS);
        return null;
    };

    Route vote = (request, response) -> {
        int pollID = Integer.parseInt(request.params(":pollID"));
        int choiceID = Integer.parseInt(request.params(":choiceID"));
        int vc=1;
        PreparedStatement stm = cn.prepareStatement(
                "INSERT INTO vote (pollid, choiceid,count) VALUES (?,?,select count(pollid)+1 from vote where choiceid=? and pollid=?)"
        );
        stm.setInt(1, pollID);
        stm.setInt(2, choiceID);
        stm.setInt(3, choiceID);
        stm.setInt(4, pollID);
        stm.executeUpdate();
        PreparedStatement vote = cn.prepareStatement(
                "select count from vote where pollid=? and choiceid=?"
        );
        vote.setInt(2,choiceID);
        vote.setInt(1,pollID);
        ResultSet v =vote.executeQuery();
        if(v.next())
        {
        vc=v.getInt(1);
        }
        response.redirect(Paths.showPoll(pollID));
        return "Your vote has been recorded";
    };

}
