package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Endpoint for Customer Sign In
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody final SignupCustomerRequest signupCustomerRequest) {
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id("sample").status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }

    //Endpoint for Customer Login
    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader final String authorizationToken) {
        LoginResponse loginResponse = new LoginResponse().id("sample").id("").message("LOGGED IN SUCCESSFULLY").firstName("").lastName("").emailAddress("").contactNumber("");
        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
    }

    //Endpoint for Customer Logout
    @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader final String authorizationToken) {
        LogoutResponse logoutResponse = new LogoutResponse().id("").message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }

    //Endpoint for Updating customer details
    @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> update(@RequestBody final UpdateCustomerRequest updateCustomerRequest) {
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id("").firstName("").lastName("").status("");
        return new ResponseEntity<>(updateCustomerResponse, HttpStatus.OK);
    }

    //Endpoint for updating customer Password
    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestBody final UpdatePasswordRequest updatePasswordRequest) {
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id("").status("");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }
}
