package com.upgrad.FoodOrderingApp.service.businness;

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
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    @Autowired
    private CustomerAuthDAO customerAuthDAO;

    //Customer Signup Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity saveCustomer(final CustomerEntity customerEntity) throws SignUpRestrictedException {

        if (!verifyValidEmail(customerEntity.getEmail())) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        if (!verifyValidContactNumber(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        if (!verifyStrongPassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }
        CustomerEntity existingCustomerEntity = customerDAO.getCustomerByContactNumber(customerEntity.getContactNumber());
        if (existingCustomerEntity != null) {
            throw new SignUpRestrictedException("SGR-001", "This contact number is already registered! Try other contact number.");
        }
        //To Encrypt Password from Customer
        String encryptedText[] = passwordCryptographyProvider.encrypt(customerEntity.getPassword());
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDAO.createCustomer(customerEntity);

    }

    //Customer Login Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerAuthEntity authenticate(String contactNumber, String password) throws AuthenticationFailedException {
        CustomerEntity customerEntity = customerDAO.getCustomerByContactNumber(contactNumber);
        if (customerEntity == null) {
            System.out.println("==== Empty Customer Entity ======");
            throw new AuthenticationFailedException("ATH-001", "This contact number has not been registered!");
        }
        final String encryptedInputPassword = passwordCryptographyProvider.encrypt(password, customerEntity.getSalt());
        if (!encryptedInputPassword.equals(customerEntity.getPassword())) {
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
    public CustomerAuthEntity logout(String accessToken) throws AuthorizationFailedException {
        CustomerEntity customerEntity = getCustomer(accessToken);
        CustomerAuthEntity customerAuthEntity = customerAuthDAO.getCustomerAuthToken(accessToken);
        //customerAuthEntity.setExpiresAt(ZonedDateTime.now());
        customerAuthEntity.setLogoutAt(ZonedDateTime.now());
        CustomerAuthEntity updatedCustomerAuthEntity = customerAuthDAO.updateCustomerAuth(customerAuthEntity);
        return updatedCustomerAuthEntity;
    }

    //Customer Details Update Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomer(CustomerEntity customerEntity) throws UpdateCustomerException, AuthorizationFailedException {
        return customerDAO.updateCustomer(customerEntity);
    }

    //To Update Password of the Customer
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity updateCustomerPassword(String oldPassword, String newPassword, CustomerEntity customerEntity) throws UpdateCustomerException, AuthorizationFailedException {
        if (!verifyStrongPassword(newPassword)) {
            throw new UpdateCustomerException("UCR-001", "Weak password!");
        }
        String oldPasswordEncrypted = passwordCryptographyProvider.encrypt(oldPassword, customerEntity.getSalt());
        if (!oldPasswordEncrypted.equals(customerEntity.getPassword())) {
            throw new UpdateCustomerException("UCR-004", "Incorrect old password!");
        }
        //To Encrypt New Password from Customer
        String encryptedText[] = passwordCryptographyProvider.encrypt(newPassword);
        customerEntity.setSalt(encryptedText[0]);
        customerEntity.setPassword(encryptedText[1]);
        return customerDAO.updateCustomer(customerEntity);
    }

    //Customer Auth Token Functionality
    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomer(String accessToken) throws AuthorizationFailedException {
        CustomerAuthEntity customerAuthEntity = customerAuthDAO.getCustomerAuthToken(accessToken);
        if (customerAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "Customer is not Logged in.");
        }
        if (customerAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "Customer is logged out. Log in again to access this endpoint.");
        }
        final ZonedDateTime now = ZonedDateTime.now();

        if (customerAuthEntity.getExpiresAt().compareTo(now) <= 0) {
            throw new AuthorizationFailedException("ATHR-003", "Your session is expired. Log in again to access this endpoint.");
        }
        return customerAuthEntity.getCustomer();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public CustomerEntity getCustomerByUuid(String customerUuid) {
        return customerDAO.getCustomerByUuid(customerUuid);
    }

    //To validate User Input Email
    public boolean verifyValidEmail(String emailAddress) {
        return emailAddress.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$");
    }

    //To validate User Contact No.
    public boolean verifyValidContactNumber(String contactNumber) {
        boolean validContactNumber = false;
        Pattern p = Pattern.compile("(0/91)?[7-9][0-9]{9}");
        Matcher m = p.matcher(contactNumber);
        if (m.find() && m.group().equals(contactNumber)) {
            validContactNumber = true;
        }
        return validContactNumber;
    }

    //To Validate Password according to given rules
    public boolean verifyStrongPassword(String password) {
        return password.matches("^(?=.*?[A-Z])(?=.*?[0-9])(?=.*?[#@$%&*!^]).{8,}$");
    }
}
