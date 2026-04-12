package com.example.UberApp.service;

import com.example.UberApp.DTOs.InvoiceDTO;
import com.example.UberApp.model.Payment;
import com.example.UberApp.model.Ride;
import com.example.UberApp.repository.PaymentRepository;
import com.example.UberApp.repository.RideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final RideRepository rideRepository;
    private final PaymentRepository paymentRepository;

    // 🧾 Generate invoice for a ride
    public InvoiceDTO.InvoiceResponse generateInvoice(Long rideId) {

        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("RIDE NOT FOUND"));

        Payment payment = paymentRepository.findByRideId(rideId);

        InvoiceDTO.InvoiceResponse invoice = new InvoiceDTO.InvoiceResponse();

        // 🔗 Ride info
        invoice.setRideId(ride.getId());
        invoice.setRideStatus(ride.getStatus().name());
        invoice.setPickupLocation(ride.getPickupLocation());
        invoice.setDropLocation(ride.getDropLocation());
        invoice.setPenaltyApplied(ride.isPenaltyApplied());
        invoice.setCancelledBy(ride.getCancelledBy());

        // 👤 Rider
        invoice.setRiderName(ride.getRider().getName());
        invoice.setRiderEmail(ride.getRider().getEmail());

        // 🚗 Driver
        invoice.setDriverName(ride.getDriver().getName());

        // 💰 Payment
        if (payment != null) {
            invoice.setAmount(payment.getAmount());
            invoice.setPaymentMethod(payment.getMethod());
            invoice.setPaymentStatus(payment.getStatus().name());
        } else {
            invoice.setAmount(0.0);
            invoice.setPaymentMethod("N/A");
            invoice.setPaymentStatus("NOT PAID");
        }

        return invoice;
    }
}