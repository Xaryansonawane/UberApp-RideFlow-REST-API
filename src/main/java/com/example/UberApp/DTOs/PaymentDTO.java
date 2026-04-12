package com.example.UberApp.DTOs;

import lombok.*;

public class PaymentDTO {

    @Getter
    @Setter
    public static class PaymentRequest {
        private Long rideId;
        private String method;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class PaymentResponse {
        private String status;
        private String message;
    }
}