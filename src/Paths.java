import spark.Request;

public class Paths {

    public final static String LIST_POLLS = "/polls";

    public static String listPolls() {
        return LIST_POLLS;
    }

    ////////////////////////////////////////

    public final static String SHOW_POLL = "/polls/:pollID";

    public static String showPoll(int pollID) {
        return SHOW_POLL.replaceFirst(":pollID", Integer.toString(pollID));
    }

    public static int showPollID(Request request) {
        return Integer.parseInt(request.params(":pollID"));
    }

    ////////////////////////////////////////////

    public final static String RM_CHOICE = "/hello-please-remove/:choiceId/from-question/:pollId";

    public static String rmChoice(int pollID, int choiceID) {
        return RM_CHOICE.replaceFirst(":pollId", Integer.toString(pollID))
                        .replaceFirst(":choiceId", Integer.toString(choiceID));
    }

    public static int rmChoicePollId(Request request)
    {
        return Integer.parseInt(request.params(":pollId"));
    }

    public static int rmChoiceId(Request request) {
        return Integer.parseInt(request.params(":choiceId"));
    }

}
