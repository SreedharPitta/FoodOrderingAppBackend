package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.common.CommonUtilsValidator;
import com.upgrad.FoodOrderingApp.service.dao.AddressDAO;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDAO;
import com.upgrad.FoodOrderingApp.service.dao.StateDAO;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerAddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private CustomerAddressDAO customerAddressDAO;


    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity) throws SaveAddressException {
        validateAddressFields(addressEntity);
        if(!validatePinCode(addressEntity.getPincode())){
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }
        AddressEntity updatedAddressEntity = addressDAO.saveAddress(addressEntity);

        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setAddress(addressEntity);
        customerAddressEntity.setCustomer(customerEntity);
        customerAddressDAO.saveCustomerAddress(customerAddressEntity);

        return null;
    }

    public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity){
        return null;
    }

    public AddressEntity deleteAddress(final AddressEntity addressEntity){
        return addressDAO.deleteAddress(addressEntity);
    }

    public List<StateEntity> getAllStates(){
        return null;
    }


    public AddressEntity getAddressByUUID(final String uuid, final CustomerEntity customerEntity){
        return null;
    }

    public StateEntity getStateByUUID(final String uuid) throws AddressNotFoundException {
        StateEntity stateEntity = stateDAO.getStateByUUID(uuid);
        if(stateEntity == null){
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        return stateEntity;
    }

    //This is to validate the fields of Address
    private void validateAddressFields(final AddressEntity addressEntity) throws SaveAddressException {
        if((addressEntity.getFlatBuilNo() == null || addressEntity.getFlatBuilNo().isEmpty())
            || (addressEntity.getLocality() == null || addressEntity.getLocality().isEmpty()) ||
                (addressEntity.getCity() == null || addressEntity.getCity().isEmpty()) ||
        (addressEntity.getPincode() == null || addressEntity.getPincode().isEmpty())
        ){
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }
    }

    //This is to validate the Pincode
    private boolean validatePinCode(final String pinCode) {
        if(pinCode.length() !=6){
            return false;
        }
        for(int i =0; i < pinCode.length(); i++){
            if(!Character.isDigit(pinCode.charAt(i))){
                return false;
            }
        }
        return true;
    }


}
