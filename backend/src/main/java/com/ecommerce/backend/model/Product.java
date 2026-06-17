package com.ecommerce.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.beans.Mergeable;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "products")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    private String productName;

    private String image;
    private String description;
    private Integer quantity;
    private double price;
    private double discount;
    private double specialPrice;

    @ManyToOne
    @JoinColumn(name= "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User user;

    @OneToMany(mappedBy = "product"  ,cascade = {
            CascadeType.MERGE,CascadeType.PERSIST
    },fetch = FetchType.EAGER)
    private List<CartItem> products = new ArrayList<>();

}
