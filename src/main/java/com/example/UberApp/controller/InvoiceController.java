package com.example.UberApp.controller;

import com.example.UberApp.DTOs.InvoiceDTO;
import com.example.UberApp.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoice")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    // 🧾 Get invoice for a ride
    @GetMapping("/{rideId}")
    public InvoiceDTO.InvoiceResponse getInvoice(@PathVariable Long rideId) {
        return invoiceService.generateInvoice(rideId);
    }
}