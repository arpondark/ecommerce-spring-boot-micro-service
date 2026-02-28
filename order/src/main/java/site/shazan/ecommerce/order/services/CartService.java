package site.shazan.ecommerce.order.services;

import site.shazan.ecommerce.order.clients.ProductServiceClient;
import site.shazan.ecommerce.order.clients.UserServiceClient;
import site.shazan.ecommerce.order.dtos.CartItemRequest;
import site.shazan.ecommerce.order.dtos.ProductResponse;
import site.shazan.ecommerce.order.dtos.UserResponse;
import site.shazan.ecommerce.order.models.CartItem;
import site.shazan.ecommerce.order.repositories.CartItemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final ProductServiceClient productServiceClient;
    private final UserServiceClient userServiceClient;

    public boolean addToCart(String userId, CartItemRequest request) {
        try {
            // Look for product
            ProductResponse productResponse = productServiceClient.getProductDetails(request.getProductId());
            if (productResponse == null || productResponse.getQuantity() < request.getQuantity()) {
                log.warn("Product not available or insufficient quantity for product ID: {}", request.getProductId());
                return false;
            }

            UserResponse userResponse = userServiceClient.getUserDetails(userId);
            if (userResponse == null) {
                log.warn("User not found: {}", userId);
                return false;
            }

            CartItem existingCartItem = cartItemRepository.findByUserIdAndProductId(userId, request.getProductId());
            if (existingCartItem != null) {
                // Update the quantity
                existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
                existingCartItem.setPrice(BigDecimal.valueOf(1000.00));
                cartItemRepository.save(existingCartItem);
            } else {
                // Create new cart item
                CartItem cartItem = new CartItem();
                cartItem.setUserId(userId);
                cartItem.setProductId(request.getProductId());
                cartItem.setQuantity(request.getQuantity());
                cartItem.setPrice(BigDecimal.valueOf(1000.00));
                cartItemRepository.save(cartItem);
            }
            return true;
        } catch (Exception e) {
            log.error("Error adding to cart for user: {} and product: {}", userId, request.getProductId(), e);
            return false;
        }
    }

    public boolean deleteItemFromCart(String userId, String productId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, productId);

        if (cartItem != null){
            cartItemRepository.delete(cartItem);
            return true;
        }
        return false;
    }

    public List<CartItem> getCart(String userId) {
        return cartItemRepository.findByUserId(userId);
    }

    public void clearCart(String userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
