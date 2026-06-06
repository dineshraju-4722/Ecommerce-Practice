package com.ecommerce.backend.controller;

import com.ecommerce.backend.payload.ProductDTO;
import com.ecommerce.backend.payload.ProductResponse;
import com.ecommerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

   @PostMapping("/admin/categories/{categoryId}/prooduct")
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductDTO productDTO,
            @PathVariable long categoryId
   ) {
      ProductDTO savedProduct = productService.addProduct(categoryId,productDTO);
      return new ResponseEntity<>(savedProduct, HttpStatus.OK);
   }

   @GetMapping("/public/products")
    public ResponseEntity<ProductResponse> getProducts(){
       ProductResponse productsList = productService.getProducts();
       return new ResponseEntity<>(productsList,HttpStatus.OK);
   }
   @GetMapping("/public/categories/{categoryId}/products")
   public ResponseEntity<ProductResponse> getProductsBycategory(@PathVariable Long categoryId){
       ProductResponse productsList = productService.getProductsByCategory(categoryId);
       return new ResponseEntity<>(productsList,HttpStatus.OK);
   }

   @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(@PathVariable String keyword){
        ProductResponse productResponse = productService.getProductsByKeyword(keyword);
       return new ResponseEntity<>(productResponse,HttpStatus.OK);
   }
}
