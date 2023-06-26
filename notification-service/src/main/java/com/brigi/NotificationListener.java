package com.brigi;

import com.brigi.dto.NotificationResponse;
import com.brigi.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {
    @Autowired
    SmsService smsService;

    @KafkaListener(topics = {"notificationTopic"}, groupId = "1")
    void listener(NotificationResponse data) {
        smsService.sendMessage(data.getMessage(), data.getPhoneNumber());
    }
}
