package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.*;
import com.upgrad.FoodOrderingApp.service.businness.AddressService;
import com.upgrad.FoodOrderingApp.service.businness.CustomerService;
import com.upgrad.FoodOrderingApp.service.entity.AddressEntity;
import com.upgrad.FoodOrderingApp.service.entity.CustomerEntity;
import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import com.upgrad.FoodOrderingApp.service.exception.AddressNotFoundException;
import com.upgrad.FoodOrderingApp.service.exception.AuthorizationFailedException;
import com.upgrad.FoodOrderingApp.service.exception.SaveAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @Autowired
    private CustomerService customerService;

    //Endpoint for saving Address
    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorizationToken, @RequestBody(required = false) final SaveAddressRequest saveAddressRequest) throws AuthorizationFailedException, AddressNotFoundException, SaveAddressException {
        final String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        StateEntity stateEntity = addressService.getStateByUUID(saveAddressRequest.getStateUuid());
        AddressEntity addressEntity = new AddressEntity();
        addressEntity.setUuid(UUID.randomUUID().toString());
        addressEntity.setFlatBuilNo(saveAddressRequest.getFlatBuildingName());
        addressEntity.setLocality(saveAddressRequest.getLocality());
        addressEntity.setCity(saveAddressRequest.getCity());
        addressEntity.setPincode(saveAddressRequest.getPincode());
        addressEntity.setState(stateEntity);
        AddressEntity updatedAddressEntity = addressService.saveAddress(addressEntity, customerEntity);
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse().id(updatedAddressEntity.getUuid()).status("ADDRESS SUCCESSFULLY REGISTERED");
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    //End point to get all saved addresses
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddresses(@RequestHeader("authorization") final String authorizationToken) throws AuthorizationFailedException {
        final String accessToken = authorizationToken.split("Bearer ")[1];
        CustomerEntity customerEntity = customerService.getCustomer(accessToken);
        List<AddressEntity> addressEntities = addressService.getAllAddress(customerEntity);
        List<AddressList> addressLists = new ArrayList<AddressList>();
        for(AddressEntity addressEntity : addressEntities){
            AddressList addressList = new AddressList().id(UUID.fromString(addressEntity.getUuid()))
                    .flatBuildingName(addressEntity.getFlatBuilNo())
                    .locality(addressEntity.getLocality())
                    .city(addressEntity.getCity())
                    .pincode(addressEntity.getPincode())
                    .state(new AddressListState().id(UUID.fromString(addressEntity.getState().getUuid())).stateName(addressEntity.getState().getStateName()));
        addressLists.add(addressList);
        }
        AddressListResponse addressListResponse = new AddressListResponse().addresses(addressLists);
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    //End Point to Delete Saved Address
    @RequestMapping(method = RequestMethod.DELETE, path = "/address/{address_id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> deleteAddress(@RequestHeader("authorization") final String authorizationToken, @PathVariable("address_id") final String addressId) {
        AddressListResponse addressListResponse = new AddressListResponse();
        return new ResponseEntity<AddressListResponse>(addressListResponse, HttpStatus.OK);
    }

    //Endpoint to get all states
    @RequestMapping(method = RequestMethod.GET, path = "/states", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<StatesListResponse> getAllStates() {
        List<StateEntity> stateEntities = addressService.getAllStates();
        List<StatesList> statesLists = new ArrayList<StatesList>();
        for (StateEntity stateEntity : stateEntities) {
            StatesList statesList = new StatesList().id(UUID.fromString(stateEntity.getUuid())).stateName(stateEntity.getStateName());
            statesLists.add(statesList);
        }
        StatesListResponse statesListResponse = new StatesListResponse().states(statesLists);
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }
}
