package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.entity.OrderEntity;
import com.upgrad.FoodOrderingApp.service.entity.PaymentEntity;
import org.hibernate.criterion.Order;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    public  PaymentEntity getPaymentByUUID(String paymentId){
        return null;
    }

    public List<PaymentEntity> getAllPaymentMethods(){
        return null;
    }
}
