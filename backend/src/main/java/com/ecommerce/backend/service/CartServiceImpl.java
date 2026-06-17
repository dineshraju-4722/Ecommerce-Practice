package com.ecommerce.backend.service;

import com.ecommerce.backend.security.response.MessageResponse;
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
import jakarta.transaction.Transactional;
import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

        //404
        Product productFromDB = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("Product","ProductId",productId));
        CartItem cartItem = cartItemRepository.findByProduct_ProductIdAndCart_CartId(
          productId, cart.getCartId());
        if(cartItem!=null){throw new APIException("Product "+productFromDB.getProductName()+" Already exists in cart");
        }

        //Validations
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
        cart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);
        cart.setTotalPrice(cart.getTotalPrice()+(quantity*productFromDB.getSpecialPrice()) );
        cartRepository.save(cart);
        return getCartDTOFromCart(cart);
    }

    @Override
    public List<CartDTO> getCarts() {
        List<Cart> carts = cartRepository.findAll();
        if(carts.isEmpty())throw new APIException("No Cart exists");

        return carts.stream().map(this::getCartDTOFromCart).collect(Collectors.toList());
    }

    @Override
    public CartDTO getCart(String loggedInEmail) {
        Cart cart = cartRepository.findByUserEmail(loggedInEmail);
        if(cart==null)throw  new ResourceNotFoundException("Cart","Email",loggedInEmail);
        return getCartDTOFromCart(cart);
    }

    @Transactional
    @Override
    public CartDTO updateProductCart(Long productId, int quantity) {
        //404
        Cart userCart = cartRepository.findByUserEmail(authUtil.getLoggedInEmail());
        if (userCart == null) {
            throw new ResourceNotFoundException("userCart", "cart", authUtil.getLoggedInEmail());
        }
        Cart cart = cartRepository.findById(userCart.getCartId()).orElse(null);
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "ProductId", productId)
        );

        //Validate Boundaries
        if (product.getQuantity() == 0) throw new APIException(product.getProductName() + " is not available");
        CartItem cartItem = cartItemRepository.findByProduct_ProductIdAndCart_CartId(productId, cart.getCartId());
        if (cartItem == null) throw new APIException("Product " + product.getProductName() + " Not available in cart");
        if (product.getQuantity() < cartItem.getQuantity() + quantity)
            throw new APIException("Please make sure the quantity with in the available bounds");
        int newQuantity = cartItem.getQuantity() + quantity;
        if (newQuantity < 0)
            throw new APIException("Please make an order less than or equal to " + product.getQuantity() + ".");

        //deleteOrUpadte
        if (newQuantity == 0) {
            cart.setTotalPrice( cart.getTotalPrice() - (cartItem.getQuantity() * cartItem.getProductPrice()));
            cartItemRepository.deleteByProduct_ProductIdAndCart_CartId(productId, cart.getCartId());
        }
        else {
            cartItem.setProductPrice(product.getSpecialPrice());
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setDiscount(product.getDiscount());
            cart.setTotalPrice(cart.getTotalPrice() + (product.getSpecialPrice() * quantity));
            cartRepository.save(cart);
            CartItem updatedItem = cartItemRepository.save(cartItem);
        }
        return getCartDTOFromCart(cart);
    }

    @Transactional
    @Override
    public MessageResponse deleteProductFromCart(Long cartId, Long productId) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(
                () -> new ResourceNotFoundException("cart", "cartId", cartId));
        CartItem cartItemToRemove = cartItemRepository.findByProduct_ProductIdAndCart_CartId(productId,cartId);
        if (cartItemToRemove == null) {
            throw new ResourceNotFoundException("Product", "ProductId", productId);
        }
        cart.setTotalPrice(cart.getTotalPrice() - (cartItemToRemove.getQuantity() * cartItemToRemove.getProductPrice()));
        cartRepository.save(cart);
        cartItemRepository.deleteByProduct_ProductIdAndCart_CartId(productId,cartId);
        return new MessageResponse("CartItem with product " + cartItemToRemove.getProduct().getProductName() + " has been removed from cart");
    }

    private Cart createCart(){
        Cart cart = cartRepository.findByUserEmail(authUtil.getLoggedInEmail());
        if(cart!=null)return cart;
        cart = new Cart();
        cart.setTotalPrice(0.0);
        cart.setUser(authUtil.getLoggedInUser());
        return cartRepository.save(cart);
    }

    @NonNull
    private CartDTO getCartDTOFromCart(Cart cart) {
        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> productDTOS = cart.getCartItems().stream().map(item -> {
            ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setQuantity(item.getQuantity());
            return productDTO;
        }).collect(Collectors.toList());
        cartDTO.setProducts(productDTOS);
        return cartDTO;
    }
}

