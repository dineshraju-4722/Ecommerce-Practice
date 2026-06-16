package com.ecommerce.backend.service;

import com.ecommerce.backend.util.AuthUtil;
import com.ecommerce.backend.exceptions.APIException;
import com.ecommerce.backend.exceptions.ResourceNotFoundException;
import com.ecommerce.backend.model.Cart;
import com.ecommerce.backend.model.CartItem;
import com.ecommerce.backend.model.Product;
import com.ecommerce.backend.payload.CartDTO;
import com.ecommerce.backend.payload.ProductDTO;
import com.ecommerce.backend.repositories.CartItemRepository;
import com.ecommerce.backend.repositories.CartRepository;
import com.ecommerce.backend.repositories.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CartServiceImpl implements  CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private AuthUtil authUtil;

    @Override
    public CartDTO addProductToCart(Long productId, int quantity) {
        Cart cart = createCart();
        Product productFromDB = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("Product","ProductId",productId));

        CartItem cartItem = cartItemRepository.findByProduct_ProductIdAndCart_CartId(
          productId, cart.getCartId());
        if(cartItem!=null){throw new APIException("Product "+productFromDB.getProductName()+" Already exists in cart");
        }
        if (productFromDB.getQuantity() == 0) {
            throw new APIException(productFromDB.getProductName() + " is not available");
        }
        if(productFromDB.getQuantity()<quantity){
            throw new APIException("Stock is Less than your preffered quantity");
        }

        cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(productFromDB);
        cartItem.setQuantity(quantity);
        cartItem.setDiscount(productFromDB.getDiscount());
        cartItem.setProductPrice( productFromDB.getSpecialPrice() );
        cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice()+(quantity*productFromDB.getSpecialPrice()) );
        cartRepository.save(cart);

        CartDTO cartDTO =  modelMapper.map(cart,CartDTO.class);

        List<CartItem> cartItems = cart.getCartItems();
        List<ProductDTO> productDTOStream = cartItems.stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        }).collect(Collectors.toList());
        cartDTO.setProducts(productDTOStream);
        return cartDTO;
    }

    @Override
    public List<CartDTO> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty())throw new APIException("No Cart exists");

        List<CartDTO> cartsDtos = carts.stream().map(cart -> {

            CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
            List<ProductDTO> cartProducts = cart.getCartItems().stream().map(item -> {
                        ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
                        productDTO.setQuantity(item.getQuantity());
                        return productDTO;
                    }).
                    collect(Collectors.toList());
            cartDTO.setProducts(cartProducts);
            return cartDTO;
        }).collect(Collectors.toList());

        return cartsDtos;
    }

    @Override
    public CartDTO getCart(String loggedInEmail) {
        Cart cart = cartRepository.findByUserEmail(loggedInEmail);
        if(cart==null)throw  new ResourceNotFoundException("Cart","Email",loggedInEmail);
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
        List<ProductDTO> productsDTOS = cart.getCartItems().stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        }).collect(Collectors.toList());
        cartDTO.setProducts(productsDTOS);
        return cartDTO;
    }

    private Cart createCart(){
        Cart cart = cartRepository.findByUserEmail(authUtil.getLoggedInEmail());
        if(cart!=null)return cart;
        cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.getLoggedInUser());
        return cartRepository.save(cart);
    }
}

