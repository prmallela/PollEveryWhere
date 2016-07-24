import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Account;
import com.twilio.sdk.resource.instance.Message;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SmsSender {

    public static void main(String[] args) throws TwilioRestException {

        TwilioRestClient client = new TwilioRestClient(Config.ACCOUNT_SID, Config.AUTH_TOKEN);

        Account account = client.getAccount();

        MessageFactory messageFactory = account.getMessageFactory();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("To", Config.recipient)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("From", Config.twilioSender)); // Replace with a valid phone number for your account.
        params.add(new BasicNameValuePair("Body", "Where's Wallace?"));
        Message sms = messageFactory.create(params);
    }
}