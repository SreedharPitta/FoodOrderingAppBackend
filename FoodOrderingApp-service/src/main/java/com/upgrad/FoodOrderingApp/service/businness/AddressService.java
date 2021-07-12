package com.upgrad.FoodOrderingApp.service.businness;

import com.upgrad.FoodOrderingApp.service.dao.AddressDAO;
import com.upgrad.FoodOrderingApp.service.dao.CustomerAddressDAO;
import com.upgrad.FoodOrderingApp.service.dao.StateDAO;
import com.upgrad.FoodOrderingApp.service.entity.*;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService {

    @Autowired
    private AddressDAO addressDAO;

    @Autowired
    private StateDAO stateDAO;

    @Autowired
    private CustomerAddressDAO customerAddressDAO;

    @Autowired
    private OrderService orderService;

    //To save Address
    @Transactional(propagation = Propagation.REQUIRED)
    public AddressEntity saveAddress(final AddressEntity addressEntity, final CustomerEntity customerEntity) throws SaveAddressException {
        validateAddressFields(addressEntity);
        if (!validatePinCode(addressEntity.getPincode())) {
            throw new SaveAddressException("SAR-002", "Invalid pincode");
        }
        AddressEntity updatedAddressEntity = addressDAO.saveAddress(addressEntity);

        //This will update the Customer Address Entity
        CustomerAddressEntity customerAddressEntity = new CustomerAddressEntity();
        customerAddressEntity.setAddress(addressEntity);
        customerAddressEntity.setCustomer(customerEntity);
        customerAddressDAO.saveCustomerAddress(customerAddressEntity);

        return updatedAddressEntity;
    }

    public List<AddressEntity> getAllAddress(final CustomerEntity customerEntity) {
        List<AddressEntity> addressEntities = new ArrayList<AddressEntity>();
        List<CustomerAddressEntity> customerAddressEntities = customerAddressDAO.getAllAddress(customerEntity);
        for(CustomerAddressEntity customerAddressEntity : customerAddressEntities){
            addressEntities.add(customerAddressEntity.getAddress());
        }
        return addressEntities;
    }

    public AddressEntity deleteAddress(final AddressEntity addressEntity) {
        List<OrderEntity> orders = orderService.getOrdersByAddress(addressEntity);
        if(!orders.isEmpty()){
            addressEntity.setActive(0);
            return addressDAO.updateAddress(addressEntity);
        }
        return addressDAO.deleteAddress(addressEntity);
    }

    public List<StateEntity> getAllStates() {
        return stateDAO.getAllStates();
    }


    public AddressEntity getAddressByUUID(final String uuid, final CustomerEntity customerEntity) throws AddressNotFoundException {
        if(uuid == null || uuid.isEmpty()){
            throw new AddressNotFoundException("ANF-005", "Address id can not be empty");
        }
        AddressEntity addressEntity = addressDAO.getAddressByUuid(uuid);
        if(addressEntity == null){
            throw new AddressNotFoundException("ANF-003", "No address by this id");
        }
        CustomerAddressEntity customerAddressEntity = customerAddressDAO.getCustomerAddressByAddress(addressEntity);
        if(customerAddressEntity == null || !customerAddressEntity.getCustomer().equals(customerEntity)){
            throw new AddressNotFoundException("ATHR-004", "You are not authorized to view/update/delete any one else's address");
        }
        return addressEntity;
    }

    public StateEntity getStateByUUID(final String uuid) throws AddressNotFoundException {
        StateEntity stateEntity = stateDAO.getStateByUUID(uuid);
        if (stateEntity == null) {
            throw new AddressNotFoundException("ANF-002", "No state by this id");
        }
        return stateEntity;
    }

    //This is to validate the fields of Address
    private void validateAddressFields(final AddressEntity addressEntity) throws SaveAddressException {
        if ((addressEntity.getFlatBuilNo() == null || addressEntity.getFlatBuilNo().isEmpty())
                || (addressEntity.getLocality() == null || addressEntity.getLocality().isEmpty()) ||
                (addressEntity.getCity() == null || addressEntity.getCity().isEmpty()) ||
                (addressEntity.getPincode() == null || addressEntity.getPincode().isEmpty())
        ) {
            throw new SaveAddressException("SAR-001", "No field can be empty");
        }
    }

    //This is to validate the Pincode
    private boolean validatePinCode(final String pinCode) {
        if (pinCode.length() != 6) {
            return false;
        }
        for (int i = 0; i < pinCode.length(); i++) {
            if (!Character.isDigit(pinCode.charAt(i))) {
                return false;
            }
        }
        return true;
    }


}
