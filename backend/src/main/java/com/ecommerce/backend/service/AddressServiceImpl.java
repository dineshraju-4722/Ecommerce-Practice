package com.ecommerce.backend.service;

import com.ecommerce.backend.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.model.Address;
import com.ecommerce.backend.model.User;
import com.ecommerce.backend.payload.AddressDTO;
import com.ecommerce.backend.repositories.AddressRepository;
import com.ecommerce.backend.security.response.MessageResponse;
import com.ecommerce.backend.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements  AddressService {


    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AuthUtil authUtil;


    @Override
    public AddressDTO addAddress(AddressDTO addressDTO) {
        Address address = modelMapper.map(addressDTO, Address.class);
        User user = authUtil.getLoggedInUser();
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() {
        List<Address> addresses = addressRepository.findAll();
        return addresses.stream().map( address -> modelMapper.map(address, AddressDTO.class) ).collect(Collectors.toList());
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));
        return modelMapper.map(address, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddressesByUser(Long loggedInUserId) {
        List<Address> addressesList = addressRepository.findByUser_Id(loggedInUserId);
        return addressesList.stream().map( address -> modelMapper.map(address, AddressDTO.class) ).collect(Collectors.toList());
    }

    @Override
    public AddressDTO updateAddress(AddressDTO addressDTO, Long addressId) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new ResourceNotFoundException("Address", "AddressId", addressId));
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setState(addressDTO.getState());
        address.setBuildingName(addressDTO.getBuildingName());
        address.setStreet(addressDTO.getStreet());
        address.setPinCode(addressDTO.getPinCode());
        addressRepository.save(address);
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public MessageResponse deleteAddress(Long addressId) {
        addressRepository.findById(addressId).orElseThrow( ()-> new ResourceNotFoundException("address","addressId",addressId) );
        addressRepository.deleteById(addressId);
        return new MessageResponse("Address deleted Successfullt");
    }


}
