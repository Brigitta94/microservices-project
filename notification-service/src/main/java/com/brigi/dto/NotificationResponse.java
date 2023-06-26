package com.brigi.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NotificationResponse {
    private String message;
    private String phoneNumber;
}
