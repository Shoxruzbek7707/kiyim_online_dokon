
package uz.pdp.kiyim_online_dokon.bot.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import uz.pdp.kiyim_online_dokon.bot.dto.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class BackendClient {

    private final WebClient backendWebClient;

    // AUTH
    public LoginResponse login(String username, String password) {
        LoginRequest req = new LoginRequest(username, password);
        return backendWebClient.post()
                .uri("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .retrieve()
                .bodyToMono(LoginResponse.class)
                .block();
    }

    // PRODUCTS
    public List<ProductDto> getProducts() {
        return backendWebClient.get()
                .uri("/products")
                .retrieve()
                .bodyToFlux(ProductDto.class)
                .collectList()
                .block();
    }

    public ProductDto getProductById(Long id) {
        return backendWebClient.get()
                .uri("/products/{id}", id)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> resp.bodyToMono(String.class).map(RuntimeException::new))
                .bodyToMono(ProductDto.class)
                .block();
    }


    // CART
    public void addToCart(String token, Long productId) {
        backendWebClient.post()
                .uri("/cart/add")
                .headers(h -> h.setBearerAuth(token))
                .bodyValue(new CartAddRequest(productId))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public List<CartItemDto> getCart(String token) {
        return backendWebClient.get()
                .uri("/cart")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(CartItemDto.class)
                .collectList()
                .block();
    }

    public OrderResponse checkout(String token) {
        return backendWebClient.post()
                .uri("/order/checkout")
                .headers(h -> h.setBearerAuth(token))
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
    }
}