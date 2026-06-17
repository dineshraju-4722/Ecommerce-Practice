package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO addAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();


    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressesByUser(Long loggedInUserId);
}
