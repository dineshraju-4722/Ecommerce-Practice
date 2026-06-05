package com.ecommerce.backend.service;

import com.ecommerce.backend.exceptions.APIException;
import com.ecommerce.backend.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.model.Category;
import com.ecommerce.backend.payload.CategoryDTO;
import com.ecommerce.backend.payload.CategoryResponse;
import com.ecommerce.backend.repositories.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories() {
        List<Category>  categories = categoryRepository.findAll();

        if(categories.isEmpty()){
            throw new APIException("No category created till now!!");
        }

        List<CategoryDTO> categoryDTOs = categories.stream().
                map(category -> modelMapper.map(category, CategoryDTO.class)).
                toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setContent(categoryDTOs);
        return categoryResponse;

    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        Category category= modelMapper.map(categoryDTO,Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB !=null){
            throw new APIException("Category with "+category.getCategoryName()+" already Exists !!");
        }
        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {
        Category categoryFromDB = categoryRepository.findById(categoryId).orElse(null);
        if(categoryFromDB ==null){
            throw new ResourceNotFoundException("Category", "categoryId",categoryId);
        }
        categoryRepository.delete(categoryFromDB);
        return modelMapper.map(categoryFromDB,CategoryDTO.class);

    }

    @Override
    public CategoryDTO updateCategory(Long categoryId, CategoryDTO categoryDTO) {
        Category categoryFromDB = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category","CategoryId",categoryId) );
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        Category updatedCategory = categoryRepository.save(category);
        return modelMapper.map(updatedCategory,CategoryDTO.class);
    }
}
