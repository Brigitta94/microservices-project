package com.brigi;

import com.brigi.dto.NotificationResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationListener {

    @KafkaListener(topics = {"notificationTopic","topicTwo" }, groupId = "1")
    void listener(NotificationResponse data) {
        //TODO send email notification
        log.info(data.getMessage());
    }
}
