package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.PaymentDAO;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private PaymentDAO paymentDAO;

    public PaymentEntity getPaymentByUUID(String paymentId) {
        return null;
    }

    public List<PaymentEntity> getAllPaymentMethods() {
        return paymentDAO.getAllPaymentMethods();
    }
}
