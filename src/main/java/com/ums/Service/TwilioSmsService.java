package com.ums.Service;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
@Service

public class TwilioSmsService {
    @Value("${twilio.account_sid}")
    private String accountSid;

    @Value("${twilio.auth_token}")
    private String authToken;
    public void sendSms(String to, String body) {
        Twilio.init(accountSid, authToken);

        Message message = Message.creator(
                        new PhoneNumber(to),
                        new PhoneNumber("+17752957849"),
                        body)
                .create();

        System.out.println("Message sent successfully. SID: " + message.getSid());
    }
    }


