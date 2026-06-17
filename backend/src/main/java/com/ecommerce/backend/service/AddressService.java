package com.ecommerce.backend.service;

import com.ecommerce.backend.payload.AddressDTO;
import com.ecommerce.backend.security.response.MessageResponse;

import java.util.List;

public interface AddressService {
    AddressDTO addAddress(AddressDTO addressDTO);

    List<AddressDTO> getAllAddresses();


    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAddressesByUser(Long loggedInUserId);

    AddressDTO updateAddress(AddressDTO addressDTO, Long addressId);

    MessageResponse deleteAddress(Long addressId);
}
