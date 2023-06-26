package com.brigi.service;

import org.springframework.stereotype.Service;

@Service
public interface SmsService {
    void sendMessage(final String message, final String phoneNumber);
}
