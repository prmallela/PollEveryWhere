import com.twilio.sdk.verbs.Message;
import com.twilio.sdk.verbs.TwiMLResponse;
import org.flywaydb.core.Flyway;
import spark.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static spark.Spark.*;

public class SparkDemo {
    static int counter = 0;
    static String msg,body;
    // MVC = Model, View, Controller
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Flyway flyway = new Flyway();
        flyway.setDataSource(Config.JDBC_URL, "", "");
        //flyway.clean(); // WARNING: deletes all tables and data!!!
        flyway.migrate();
        externalStaticFileLocation("D:/MyGitHub/PollEveryWhere/resource/templates/");
        Class.forName("org.h2.Driver");
        Connection cn = DriverManager.getConnection(Config.JDBC_URL);
        HandlebarsTemplateEngine engine = new HandlebarsTemplateEngine();

        PollIdRoute pollRoutes = new PollIdRoute(cn);
        get("/",(request, response) ->{
            return new ModelAndView(null, "home.html");
        },engine );
        get("/polls",pollRoutes.listPolls, engine);
        get("/polls/:pollID", pollRoutes, engine);
        post("/poll/:pollID/rm", pollRoutes.rmPoll);
        post("/poll/:pollID/addchoice", pollRoutes.addChoice);
        post("/poll/:pollId/choice/:choiceId/rm", pollRoutes.rmChoice);
        post("/poll/:pollId/choice/:choiceId/vote", pollRoutes.vote);
        post("/poll/question/addpoll",pollRoutes.addpoll);

        exception(NumberFormatException.class, (e, request, response) -> {
            response.status(404);
            response.body("Resource not found");
        });

        //For User Registration
        post("/registration",(request, response) -> {
            try{
            String fname = request.queryParams("fname");
            String lname = request.queryParams("lname");
            String email = request.queryParams("E-Mail");
            String password = request.queryParams("Password");
            SaltedHashedPassword origHashed = SaltedHashedPassword.generate(password);
            PreparedStatement rg = cn.prepareStatement("INSERT INTO registration (fname,lname,email,password) VALUES(?,?,?,?)");
            rg.setString(1,fname);
            rg.setString(2,lname);
            rg.setString(3,email);
            rg.setString(4,origHashed.toString());
            rg.executeUpdate();
            response.redirect("/sreg");

           }
            catch (Exception c)
            {
                response.redirect("/freg");
            }
            return null;

        });

        get("/registration",((request, response) ->{
            return new ModelAndView(null,"registration.html");
        } ),engine);

        get("/freg",((request, response) ->{
            return new ModelAndView(null,"failreg.html");
        } ),engine);


        get("/sreg",((request, response) ->{
            return new ModelAndView(null,"sucreg.html");
        } ),engine);

// FOR CHECKING EMAIL AND PASSWORD LOGIN GOTO /login

        get("/login",((request, response) ->{
            return new ModelAndView(null,"login.html");
        } ),engine);

        post("/logincheck",(request,response)->{
            String email = request.queryParams("E-Mail");
            String password = request.queryParams("Password");
            PreparedStatement vp = cn.prepareStatement("select email,password from registration where email=?");
            vp.setString(1,email);
            ResultSet va =vp.executeQuery();
            String passHashed="p";

                if (va.next()) {
                        passHashed = va.getString("password");

                    SaltedHashedPassword restoredHash = new SaltedHashedPassword(passHashed);
                    if (restoredHash.check(password)) {
                        request.session(true);
                        request.session().attribute("email", email);
                        response.redirect("/polls");
                    } else {
                        response.redirect("/logincheck");
                    }
                }
            else{
                response.redirect("/logincheck");
            }
            return null;
        });

        get("/logincheck",((request, response) ->{
            return new ModelAndView(null,"logincheck.html");
        } ),engine);

        get("/logout", (request, response) -> {
            request.session().removeAttribute("email");
            response.redirect("/login");
            return null;
        });

        /*
         CREATE TABLE incomingtexts
           ( id   integer primary key auto_increment
           , when timestamp not null default now()
           , sender varchar(32) not null
           , message varchar(500) not null
           );
         */

        post("/sms", (request, response) -> {
            // Print SMS data to the console log
            System.out.println("\nSMS @" + new Date());
            for(String h : request.headers()) {
                System.out.println(h + ": " + request.headers(h));
            }
            Map<String,String> q = splitQuery(request.body());
            String y="Body";
            for(String k : q.keySet()) {
                System.out.println(k + " = " + q.get(k));
                if (k.equals(y)) {
                    body = q.get(k);
                }
            }
            //Reading Text Code and VoteId

            //q.containsValue("pollid");

            // If Good reply Thanks for Voting
            //Else Reply Not Understood
            //Get POLLID, CHOICEID

            //Request have to be like this: Ex: POLLID=1&CHOICEID=1

            String POLLID,CHOICEID;
            try{
                if(body.contains("POLLID=") && body.contains("CHOICEID=")) {
                    POLLID = body.substring(body.indexOf('=') + 1, body.indexOf('&'));
                    CHOICEID = body.substring(body.length() - 1, body.length());
                    //System.out.println(POLLID+" "+CHOICEID);

                    PreparedStatement vc = cn.prepareStatement("select pollid,choiceid from vote where pollid=? and choiceid=?");
                    vc.setInt(1, Integer.parseInt(POLLID));
                    vc.setInt(2, Integer.parseInt(CHOICEID));
                    ResultSet res = vc.executeQuery();
                   // System.out.println(res);
                    if (!res.wasNull()) {
                        PreparedStatement stm = cn.prepareStatement("INSERT INTO vote(choiceid, pollid) VALUES (?,?)");
                        stm.setInt(1, Integer.parseInt(CHOICEID));
                        stm.setInt(2, Integer.parseInt(POLLID));
                        stm.executeUpdate();
                        msg = "Thanks for your vote";
                    } else {
                        msg = "Invalid PollId or ChoiceID";
                    }
                }
                else{
                    msg = "Not Understand";
                }

            }
            catch (Exception x){
                msg="Invalid PollId or ChoiceID";
            }
        /*CREATE TABLE receivedsms
                ( id   integer primary key auto_increment
                        , when timestamp not null default now()
                , sender varchar(32) not null
                , message varchar(500) not null
            ); */

            // Save SMS data to database
            PreparedStatement insertStm = cn.prepareStatement(
                    "INSERT INTO receivedsms (sender,message) "+
                            "VALUES (?, ?)"
            );
            insertStm.setString(1, q.get("From"));
            insertStm.setString(2, q.get("Body"));
            insertStm.execute();
            // Send a reply message
            TwiMLResponse tr = new TwiMLResponse();
            Message mesg = new Message(msg);
            tr.append(mesg);
            response.header("Content-Type", "application/xml");
            return tr.toXML();
        });
    }

    // Lightly modified from:
    // http://stackoverflow.com/questions/13592236/parse-a-uri-string-into-name-value-collection
    public static Map<String, String> splitQuery(String query) throws UnsupportedEncodingException {
        Map<String, String> query_pairs = new LinkedHashMap<String, String>();
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            query_pairs.put(
                    URLDecoder.decode(pair.substring(0, idx), "UTF-8"),
                    URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
            );
        }
        return query_pairs;
    }
}
