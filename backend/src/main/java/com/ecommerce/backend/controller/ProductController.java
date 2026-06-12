package com.ecommerce.backend.controller;

import com.ecommerce.backend.config.AppConstants;
import com.ecommerce.backend.payload.ProductDTO;
import com.ecommerce.backend.payload.ProductResponse;
import com.ecommerce.backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    public ResponseEntity<ProductResponse> getProducts(
            @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER , required = false) int pageNumber,
            @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_SIZE , required = false) int pageSize,
            @RequestParam(name = "sortBy" , defaultValue = AppConstants.SORT_PRODUCTS_BY , required = false) String sortBy,
            @RequestParam(name = "sortOrder" , defaultValue = AppConstants.SORT_DIR , required = false) String sortOrder

   ){
       ProductResponse productsList = productService.getProducts(pageNumber,pageSize,sortBy,sortOrder);
       return new ResponseEntity<>(productsList,HttpStatus.OK);
   }
   @GetMapping("/public/categories/{categoryId}/products")
   public ResponseEntity<ProductResponse> getProductsBycategory(
           @PathVariable Long categoryId,
           @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER , required = false) int pageNumber,
           @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_SIZE , required = false) int pageSize,
           @RequestParam(name = "sortBy" , defaultValue = AppConstants.SORT_PRODUCTS_BY , required = false) String sortBy,
           @RequestParam(name = "sortOrder" , defaultValue = AppConstants.SORT_DIR , required = false) String sortOrder
           ){
       ProductResponse productsList = productService.getProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder);
       return new ResponseEntity<>(productsList,HttpStatus.OK);
   }

   @GetMapping("/public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getProductsByKeyword(
            @PathVariable String keyword,
            @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_NUMBER , required = false) int pageNumber,
            @RequestParam(name = "pageNumber" , defaultValue = AppConstants.PAGE_SIZE , required = false) int pageSize,
            @RequestParam(name = "sortBy" , defaultValue = AppConstants.SORT_PRODUCTS_BY , required = false) String sortBy,
            @RequestParam(name = "sortOrder" , defaultValue = AppConstants.SORT_DIR , required = false) String sortOrder
            ){
        ProductResponse productResponse = productService.getProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder);
       return new ResponseEntity<>(productResponse,HttpStatus.OK);
   }

   @PutMapping("/public/product/{productId}/image")
    public ResponseEntity<ProductDTO>   updateProductImage(
           @PathVariable("productId") Long productId,
           @RequestParam("image")MultipartFile image
           ) throws IOException {
       ProductDTO productResponse = productService.updateProductImage(productId,image);
       return new ResponseEntity<>(productResponse,HttpStatus.OK);
   }
}
