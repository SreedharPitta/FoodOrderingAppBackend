package com.upgrad.FoodOrderingApp.service.common;

import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.exception.AuthenticationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SignUpRestrictedException;
import com.upgrad.FoodOrderingApp.service.exception.UpdateCustomerException;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommonUtilsValidator {

    //To validate Sign Up Fields
    public boolean validateSignUpFields(String firstName, String emailAddress, String contactNumber, String password) {
        boolean validFields = true;
        if ((firstName == null || firstName.isEmpty()) || (emailAddress == null || emailAddress.isEmpty()) || (contactNumber == null || contactNumber.isEmpty()) || (password == null || password.isEmpty())) {
            validFields = false;
        }
        return validFields;
    }

    //To validate User Input Email
    public boolean verifyValidEmail(String emailAddress) {
        boolean validEmail = false;
        String emailRegex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (emailAddress.matches(emailRegex)) {
            validEmail = true;
        }
        return validEmail;
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
        boolean strongPassword = false;
        boolean lengthValid = false;
        boolean upperCasePresent = false;
        boolean numberPresent = false;
        boolean specialCharacterPresent = false;

        if (password.length() >= 8) {
            lengthValid = true;
        }
        if (password.matches("(?=.*[0-9]).*")) {
            numberPresent = true;
        }
        if (password.matches("(?=.*[A-Z]).*")) {
            upperCasePresent = true;
        }
        if (password.matches("(?=.*[#@$%&*!^]).*")) {
            specialCharacterPresent = true;
        }
        if (lengthValid && upperCasePresent && numberPresent && specialCharacterPresent) {
            strongPassword = true;
        }
        return strongPassword;
    }

    public void validateCustomerUpdateRequest(String firstName) throws UpdateCustomerException {
        if (firstName == null || firstName.isEmpty()) {
            throw new UpdateCustomerException("UCR-002", "First name field should not be empty");
        }
    }

    public void validateUpdatePasswordRequest(String oldPassword, String newPassword) throws UpdateCustomerException {
        if ((oldPassword == null || oldPassword.isEmpty()) || (newPassword == null || newPassword.isEmpty())) {
            throw new UpdateCustomerException("UCR-003", "No field should be empty");
        }
    }

    public void validateSignUpRequest(CustomerEntity customerEntity) throws SignUpRestrictedException {

        if (!validateSignUpFields(customerEntity.getFirstName(), customerEntity.getEmail(), customerEntity.getContactNumber(), customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-005", "Except last name all fields should be filled");
        }
        if (!verifyValidEmail(customerEntity.getEmail())) {
            throw new SignUpRestrictedException("SGR-002", "Invalid email-id format!");
        }
        if (!verifyValidContactNumber(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-003", "Invalid contact number!");
        }
        if (!verifyStrongPassword(customerEntity.getPassword())) {
            throw new SignUpRestrictedException("SGR-004", "Weak password!");
        }
    }
}
