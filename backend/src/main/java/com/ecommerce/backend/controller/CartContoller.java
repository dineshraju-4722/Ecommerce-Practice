package com.ecommerce.backend.controller;

import com.ecommerce.backend.payload.CartDTO;
import com.ecommerce.backend.service.CartService;
import com.ecommerce.backend.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CartContoller {

    @Autowired
    private CartService cartService;

    @Autowired
    private AuthUtil authUtil;

    @PostMapping("/carts/products/{productId}/quantity/{quantity}")
    public ResponseEntity<CartDTO> addProductToCart(
            @PathVariable Long productId , @PathVariable int quantity
    ){
        CartDTO cartDTO = cartService.addProductToCart(productId,quantity);
        return new ResponseEntity<>(cartDTO, HttpStatus.CREATED);
    }

    @GetMapping("/carts")
    public ResponseEntity<List<CartDTO>> getCarts(){
        List<CartDTO> cartLists = cartService.getCarts();
        return new ResponseEntity<>(cartLists,HttpStatus.FOUND);
    }

    @GetMapping("/carts/users/cart")
    public ResponseEntity<CartDTO> getCart(){
        String loggedInEmail = authUtil.getLoggedInEmail();
        CartDTO cartDTO = cartService.getCart(loggedInEmail);
        return new ResponseEntity<>(cartDTO,HttpStatus.FOUND);
    }

}
