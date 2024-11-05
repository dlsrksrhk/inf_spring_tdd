package com.example.demo.mock;

import com.example.demo.common.service.port.UuidHolder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUuidHolder implements UuidHolder {
    private final String random;

    @Override
    public String random() {
        return random;
    }
}
