package com.ecommerce.backend.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long addressId;

}
