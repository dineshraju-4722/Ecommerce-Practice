package com.ecommerce.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

    @NotBlank
    @Size(min=4 , message = "Street name must atleast 4 characters")
    private String street;

    @NotBlank
    @Size(min=4 , message = "Building name must atleast 4 characters")
    private String buildingName;

    @NotBlank
    @Size(min=4 , message = "City name must atleast 4 characters")
    private String city;

    @NotBlank
    @Size(min=4 , message = "State name must atleast 3 characters")
    private String state;

    @NotNull
    @Min(value = 100000 , message = "must be 3 or greater")
    private int pinCode;

    @NotBlank
    @Size(min = 3 , message = "Country name must be atleast 3 characters")
    private String country;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String buildingName, String city, String state, int pinCode,String country) {
        this.street = street;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.pinCode = pinCode;
        this.country=country;
    }
}
