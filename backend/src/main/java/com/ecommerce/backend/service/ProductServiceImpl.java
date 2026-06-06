package com.ecommerce.backend.service;

import com.ecommerce.backend.exceptions.APIException;
import com.ecommerce.backend.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.payload.ProductDTO;
import com.ecommerce.backend.payload.ProductResponse;
import com.ecommerce.backend.repositories.CategoryRepository;
import com.ecommerce.backend.repositories.ProductRepository;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements   ProductService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public CategoryRepository categoryRepository;

    @Override
    public ProductDTO addProduct(long categoryId, ProductDTO productDTO) {
        Category categoryFromDB = categoryRepository.findById(categoryId).orElseThrow(() ->
                 new ResourceNotFoundException( "Category" , "CategoryID",categoryId)
        );
        List<Product> productFromDB = productRepository.findByProductName(productDTO.getProductName());
        if(!productFromDB.isEmpty()){
            throw new APIException(" product Already Exists with  the name !!! ");
        }
        Product product = modelMapper.map(productDTO, Product.class);
        product.setCategory(categoryFromDB);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct,ProductDTO.class);

    }

    @Override
    public ProductResponse getProducts() {
        List<Product> productsFromDB = productRepository.findAll();
        return getProductResponse(productsFromDB);

    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {
        Category categoryFromDB = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException( "Category" , "CategoryID",categoryId)
        );
        List<Product> productsFromDB = productRepository.findByCategory(categoryFromDB);
            return getProductResponse(productsFromDB);
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword) {
         List<Product> productsFromDB = productRepository.findByProductNameLikeIgnoreCase( '%'+ keyword +'%');
         return getProductResponse(productsFromDB);
    }

    @NonNull
    private ProductResponse getProductResponse(List<Product> productsFromDB) {
        if( productsFromDB.isEmpty() ) throw new APIException("No Products Added Yet !!!");
        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> list = productsFromDB.stream().map(prod -> modelMapper.map(prod, ProductDTO.class)).toList();
        productResponse.setContent(list);
        return productResponse;
    }
}
