package pl.balutek.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.balutek.model.Cart;
import pl.balutek.repo.CartRepository;

@RestController
@RequestMapping("/cart")
public class CartController {

    private CartRepository cartRepository;

    @Autowired
    public CartController(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @GetMapping(path = "/all")
    public List<Cart> allCarts() {
        return cartRepository.findAll();
    }

}
