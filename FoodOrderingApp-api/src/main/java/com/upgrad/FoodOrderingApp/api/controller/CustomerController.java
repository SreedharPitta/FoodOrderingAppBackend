package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    //Endpoint for Customer Sign In
    @RequestMapping(method = RequestMethod.POST, path = "/customer/signup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SignupCustomerResponse> signup(@RequestBody(required = false) final SignupCustomerRequest signupCustomerRequest) throws SignUpRestrictedException {
        CustomerEntity customerEntity = customerService.signup(signupCustomerRequest.getFirstName(), signupCustomerRequest.getLastName(), signupCustomerRequest.getEmailAddress(), signupCustomerRequest.getContactNumber(), signupCustomerRequest.getPassword());
        SignupCustomerResponse signupCustomerResponse = new SignupCustomerResponse().id(customerEntity.getUuid()).status("CUSTOMER SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SignupCustomerResponse>(signupCustomerResponse, HttpStatus.CREATED);
    }

    //Endpoint for Customer Login
    @RequestMapping(method = RequestMethod.POST, path = "/customer/login", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LoginResponse> login(@RequestHeader("authorization") final String authorizationToken) throws AuthenticationFailedException {
        CustomerAuthEntity customerAuthEntity = customerService.authenticateCustomer(authorizationToken);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        LoginResponse loginResponse = new LoginResponse().id(customerEntity.getUuid()).firstName(customerEntity.getFirstName()).lastName(customerEntity.getLastName()).contactNumber(customerEntity.getLastName()).emailAddress(customerEntity.getEmail())
                .message("LOGGED IN SUCCESSFULLY");
        HttpHeaders headers = new HttpHeaders();
        headers.add("access_token", customerAuthEntity.getAccessToken());
        //To Expose Headers as part of Response
        List<String> header = new ArrayList<String>();
        header.add("access_token");
        headers.setAccessControlExposeHeaders(header);
        return new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
    }

    //Endpoint for Customer Logout
    @RequestMapping(method = RequestMethod.POST, path = "/customer/logout", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("authorization") final String authorizationToken) throws AuthorizationFailedException {
        final CustomerEntity customerEntity = customerService.logout(authorizationToken);
        LogoutResponse logoutResponse = new LogoutResponse().id(customerEntity.getUuid()).message("LOGGED OUT SUCCESSFULLY");
        return new ResponseEntity<LogoutResponse>(logoutResponse, HttpStatus.OK);
    }

    //Endpoint for Updating customer details
    @RequestMapping(method = RequestMethod.PUT, path = "/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdateCustomerResponse> updateCustomer(@RequestHeader("authorization") final String authorizationToken, @RequestBody(required = false) final UpdateCustomerRequest updateCustomerRequest) throws AuthenticationFailedException, UpdateCustomerException {
        CustomerEntity customerEntity = customerService.updateCustomer(authorizationToken, updateCustomerRequest.getFirstName(), updateCustomerRequest.getLastName());
        UpdateCustomerResponse updateCustomerResponse = new UpdateCustomerResponse().id(customerEntity.getUuid()).firstName(customerEntity.getFirstName()).lastName(customerEntity.getLastName()).status("CUSTOMER DETAILS UPDATED SUCCESSFULLY");
        return new ResponseEntity<>(updateCustomerResponse, HttpStatus.OK);
    }

    //Endpoint for updating customer Password
    @RequestMapping(method = RequestMethod.PUT, path = "/customer/password", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UpdatePasswordResponse> updatePassword(@RequestHeader("authorization") final String authorizationToken, @RequestBody(required = false) final UpdatePasswordRequest updatePasswordRequest) throws AuthenticationFailedException, UpdateCustomerException {
        CustomerEntity customerEntity = customerService.updatePassword(authorizationToken, updatePasswordRequest.getOldPassword(), updatePasswordRequest.getNewPassword());
        UpdatePasswordResponse updatePasswordResponse = new UpdatePasswordResponse().id(customerEntity.getUuid()).status("CUSTOMER PASSWORD UPDATED SUCCESSFULLY");
        return new ResponseEntity<UpdatePasswordResponse>(updatePasswordResponse, HttpStatus.OK);
    }
}
