package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.CommonUtilsProvider;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAuthDAO;
import com.upgrad.FoodOrderingApp.service.dao.CustomerDAO;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAuthEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.UUID;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private CustomerAuthDAO customerAuthDAO;

    @Autowired
    private CommonUtilsProvider commonUtilsProvider;

    //Customer Signup Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity signup(String firstName, String lastName, String emailAddress, String contactNumber, String password) throws SignUpRestrictedException {
        CustomerEntity existingCustomerEntity = customerDAO.getCustomerByContactNumber(contactNumber);
        if(existingCustomerEntity !=null){
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        if(commonUtilsProvider.validateSignUpFields(firstName, emailAddress, contactNumber, password)){
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if(!commonUtilsProvider.verifyValidEmail(emailAddress)){
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        if(!commonUtilsProvider.verifyValidContactNumber(contactNumber)){
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        if(!commonUtilsProvider.verifyStrongPassword(password)){
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }
        final CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setUuid(UUID.randomUUID().toString());
        customerEntity.setFirstName(firstName);
        customerEntity.setLastName(lastName);
        customerEntity.setEmail(emailAddress);
        customerEntity.setContactNumber(contactNumber);
        //To Encrypt Password from Customer
        String encryptedText[] = passwordCryptographyProvider.encrypt(password);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDAO.createCustomer(customerEntity);
    }

    //Customer Login Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticateCustomer(String authorizationToken) throws AuthenticationFailedException {
        //To check whether Token has basic followed by Encoded String of contact number and password
        if(!authorizationToken.contains("Basic ")){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        byte[] decodedBytes = Base64.getDecoder().decode(authorizationToken.split("Basic ")[1]);
        String decodedString = new String(decodedBytes);
       //This is to check whether the decoded String contains split b/w the contact number and password
        if(!decodedString.contains(":")){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        String[] decodedArray = decodedString.split(":");
        //This will check that the length of the Decoded array is only 2
        if(decodedArray.length > 2){
            throw new AuthenticationFailedException("ATH-003", "Incorrect format of decoded customer name and password");
        }
        final String contactNumber = decodedArray[0];
        final String password = decodedArray[1];
        CustomerEntity customerEntity = customerDAO.getCustomerByContactNumber(contactNumber);
        if(customerEntity == null){
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }
        final String encryptedInputPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());
        if(!encryptedInputPassword.equals(customerEntity.getPassword())){
            throw new AuthenticationFailedException("ATH-002", "Invalid Credentials");
        }
        JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(encryptedInputPassword);
        CustomerAuthEntity customerAuthEntity = new CustomerAuthEntity();
        customerAuthEntity.setUuid(UUID.randomUUID().toString());
        customerAuthEntity.setCustomer(customerEntity);
        final ZonedDateTime now = ZonedDateTime.now();
        final ZonedDateTime expiresAt = now.plusHours(8);
        customerAuthEntity.setAccessToken(jwtTokenProvider.generateToken(customerEntity.getUuid(), now, expiresAt));
        customerAuthEntity.setLoginAt(now);
        customerAuthEntity.setExpiresAt(expiresAt);
        return customerAuthDAO.createCustomerAuth(customerAuthEntity);
    }

    //Customer Logout Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity logout(String authorizationToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = authenticateCustomerSession(authorizationToken);
        customerAuthEntity.setExpiresAt(ZonedDateTime.now());
        customerAuthEntity.setLogoutAt(ZonedDateTime.now());
        CustomerAuthEntity updatedCustomerAuthEntity = customerAuthDAO.updateCustomerAuth(customerAuthEntity);
        return updatedCustomerAuthEntity.getCustomer();
    }

    //Customer Details Update Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(String authorizationToken, String firstName, String lastName) throws AuthenticationFailedException, UpdateCustomerException {
        if(firstName.isEmpty()){
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
        CustomerAuthEntity customerAuthEntity = authenticateCustomer(authorizationToken);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        customerEntity.setFirstName(firstName);
        customerEntity.setLastName(lastName);
        return customerDAO.updateCustomer(customerEntity);
    }

    //To Update Password of the Customer
    public CustomerEntity updatePassword(String authorizationToken, String oldPassword, String newPassword) throws UpdateCustomerException, AuthenticationFailedException {
        if(oldPassword.isEmpty() || newPassword.isEmpty()){
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
        CustomerAuthEntity customerAuthEntity = authenticateCustomer(authorizationToken);
        CustomerEntity customerEntity = customerAuthEntity.getCustomer();
        if(!commonUtilsProvider.verifyStrongPassword(newPassword)){
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        String oldPasswordEncrypted = passwordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
        if(!oldPasswordEncrypted.equals(customerEntity.getPassword())){
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
        //To Encrypt New Password from Customer
        String encryptedText[] = passwordCryptographyProvider.encrypt(newPassword);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDAO.updateCustomer(customerEntity);
    }

    //Customer Auth Token Functionality
    public CustomerAuthEntity authenticateCustomerSession(String authorizationToken) throws AuthorizationFailedException {
        String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerAuthEntity customerAuthEntity = customerAuthDAO.getCustomerAuthToken(accessToken);
        if(customerAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if(customerAuthEntity.getLogoutAt() !=null){
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();

        if(customerAuthEntity.getExpiresAt().compareTo(now) <=0){
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity;
    }
}
