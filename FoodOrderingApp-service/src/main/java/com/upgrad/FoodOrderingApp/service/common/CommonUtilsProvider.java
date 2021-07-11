package com.upgrad.FoodOrderingApp.service.common;

import org.springframework.stereotype.Component;

@Component
public class CommonUtilsProvider {

    //To validate Sign Up Fields
    public boolean validateSignUpFields(String firstName, String emailAddress, String contactNumber, String password) {
        boolean validFields = true;
        if(firstName.isEmpty() || emailAddress.isEmpty() || contactNumber.isEmpty() || password.isEmpty()){
            validFields = false;
        }
        return validFields;
    }

    //To validate User Input Email
    public boolean verifyValidEmail(String emailAddress) {
        boolean validEmail = false;
        return  validEmail;
    }

    //To validate User Contact No.
    public boolean verifyValidContactNumber(String contactNumber) {
        boolean validContactNumber = false;

        return validContactNumber;
    }

    //To Validate Password according to given rules
    public boolean verifyStrongPassword(String password) {
        boolean strongPassword = false;

        return strongPassword;
    }
}
