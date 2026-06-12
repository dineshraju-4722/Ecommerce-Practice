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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements   ProductService{

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    public ProductRepository productRepository;
    @Autowired
    public CategoryRepository categoryRepository;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

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
    public ProductResponse getProducts(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Product> productsPage = productRepository.findAll(pageDetails);
        List<Product> productsFromDB =productsPage.getContent();
        return getProductResponse(productsFromDB,productsPage);

    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Category categoryFromDB = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException( "Category" , "CategoryID",categoryId)
        );
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productsPage = productRepository.findByCategory(categoryFromDB,pageDetails);
        List<Product> productsFromDB = productsPage.getContent();
            return getProductResponse(productsFromDB, productsPage);
    }

    @Override
    public ProductResponse getProductsByKeyword(String keyword, int pageNumber, int pageSize, String sortBy, String sortOrder) {
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc")?
                Sort.by(sortBy).ascending():
                Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);

        Page<Product> productsPage = productRepository.findByProductNameLikeIgnoreCase( '%'+ keyword +'%',pageDetails);
         List<Product> productsFromDB = productsPage.getContent();
        return getProductResponse(productsFromDB, productsPage);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "ProductId", productId)
        );
       String newFilePath = fileService.uploadImage(path,image);
       product.setImage(newFilePath);
        Product save = productRepository.save(product);
        return modelMapper.map(save,ProductDTO.class);

    }



    @NonNull
    private ProductResponse getProductResponse(List<Product> productsFromDB, Page<Product> productsPage) {
        if( productsFromDB.isEmpty() ) throw new APIException("No Products Added Yet !!!");
        ProductResponse productResponse = new ProductResponse();
        List<ProductDTO> list = productsFromDB.stream().map(prod -> modelMapper.map(prod, ProductDTO.class)).toList();
        productResponse.setContent(list);

        productResponse.setLastPage(productsPage.isLast());
        productResponse.setPageNumber(productsPage.getNumber());
        productResponse.setPageSize(productsPage.getSize());
        productResponse.setTotalPages(productsPage.getTotalPages());
        productResponse.setTotalElements(productsPage.getTotalElements());



        return productResponse;
    }
}
