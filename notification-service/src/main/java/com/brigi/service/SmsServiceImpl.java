package com.brigi.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SmsServiceImpl implements SmsService {
    @Value("${twilio.account.sid}")
    private String accountSid;
    @Value("${twilio.token}")
    private String accountToken;

    @Value("${twilio.phone.number}")
    private String myPhoneNumber;


    @Override
    public void sendMessage(final String message, final String phoneNumber) {
        Twilio.init(accountSid, accountToken);
        Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber("+14849468979"),
                message).create();
    }
}
