package com.ecommerce.backend.controller;

import com.ecommerce.backend.payload.AddressDTO;
import com.ecommerce.backend.security.response.MessageResponse;
import com.ecommerce.backend.service.AddressService;
import com.ecommerce.backend.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AddressController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/address")
    public ResponseEntity<AddressDTO> addAddress(@RequestBody AddressDTO addressDTO){
         AddressDTO responseAddressDTO = addressService.addAddress(addressDTO);
        return new ResponseEntity<>(responseAddressDTO, HttpStatus.CREATED);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses(){
        List<AddressDTO> addressDTOS =  addressService.getAllAddresses();
        return new ResponseEntity<>(addressDTOS , HttpStatus.OK);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId ){
        AddressDTO addressDTO = addressService.getAddressById(addressId);
        return new ResponseEntity<>(addressDTO,HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<List<AddressDTO>> getAddressesByUser(){
        Long loggedInUserId = authUtil.getLoggedInUserId();
        List<AddressDTO> addressDTOS =  addressService.getAddressesByUser(loggedInUserId);
        return new ResponseEntity<>(addressDTOS , HttpStatus.OK);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody AddressDTO addressDTO,@PathVariable Long addressId){
        AddressDTO addressDTOResponse = addressService.updateAddress( addressDTO ,addressId );
        return new ResponseEntity<>(addressDTOResponse,HttpStatus.OK);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<MessageResponse> deleteAddress(@PathVariable Long addressId){
        MessageResponse messageResponse = addressService.deleteAddress(addressId);
        return new ResponseEntity<>(messageResponse,HttpStatus.OK);
    }

}
