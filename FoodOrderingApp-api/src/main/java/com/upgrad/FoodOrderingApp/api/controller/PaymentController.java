package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.PaymentListResponse;
import com.upgrad.FoodOrderingApp.api.model.PaymentResponse;
import com.upgrad.FoodOrderingApp.service.businness.PaymentService;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
@RequestMapping("/")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @RequestMapping(method = RequestMethod.GET, path = "/payment", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<PaymentListResponse> getAllPaymentMethods() {
        List<PaymentEntity> paymentEntities = paymentService.getAllPaymentMethods();
        PaymentListResponse paymentListResponse = new PaymentListResponse();
        List<PaymentResponse> paymentResponses = new ArrayList<PaymentResponse>();
        for (PaymentEntity paymentEntity : paymentEntities) {
            PaymentResponse paymentResponse = new PaymentResponse().id(UUID.fromString(paymentEntity.getUuid())).paymentName(paymentEntity.getPaymentName());
            paymentResponses.add(paymentResponse);
        }
        paymentListResponse.paymentMethods(paymentResponses);
        return new ResponseEntity<>(paymentListResponse, HttpStatus.OK);
    }
}
