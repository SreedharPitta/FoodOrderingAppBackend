package com.upgrad.FoodOrderingApp.api.controller;

import com.upgrad.FoodOrderingApp.api.model.AddressListResponse;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressRequest;
import com.upgrad.FoodOrderingApp.api.model.SaveAddressResponse;
import com.upgrad.FoodOrderingApp.api.model.StatesListResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@CrossOrigin
@RequestMapping("/")
public class AddressController {

    //Endpoint for saving Address
    @RequestMapping(method = RequestMethod.POST, path = "/address", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<SaveAddressResponse> saveAddress(@RequestHeader("authorization") final String authorizationToken, @RequestBody final SaveAddressRequest saveAddressRequest) {
        SaveAddressResponse saveAddressResponse = new SaveAddressResponse();
        return new ResponseEntity<SaveAddressResponse>(saveAddressResponse, HttpStatus.CREATED);
    }

    //End point to get all saved addresses
    @RequestMapping(method = RequestMethod.GET, path = "/address/customer", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AddressListResponse> getAllSavedAddresses(@RequestHeader("authorization") final String authorizationToken) {
        AddressListResponse addressListResponse = new AddressListResponse();
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
    public ResponseEntity<StatesListResponse> getAllStates(@RequestHeader("authorization") final String authorizationToken) {
        StatesListResponse statesListResponse = new StatesListResponse();
        return new ResponseEntity<StatesListResponse>(statesListResponse, HttpStatus.OK);
    }
}
