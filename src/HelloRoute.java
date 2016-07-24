import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateViewRoute;

import java.util.HashMap;

public class HelloRoute implements TemplateViewRoute {

    private static int counter = 0;

    @Override
    public ModelAndView handle(Request request, Response response) throws Exception {
        HashMap<String, String> data = new HashMap<String, String>();
        counter++;
        data.put("counter", Integer.toString(counter));
        return new ModelAndView(data, "hello.html");
    }
}
