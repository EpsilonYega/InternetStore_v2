package api.Controllers;

import api.Main;
import api.Models.Basket;
import api.Service.BasketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/secured")
@CrossOrigin(origins = "http://localhost:3000")
public class SecuredController {
    private final BasketService basketService;
    @Autowired
    public SecuredController(BasketService basketService) {
        this.basketService = basketService;
    }
    @GetMapping("/main")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<String> getWorkedPage() {
        String message = "Вы зашли в защищенный в Main!";
        log.info("Вы зашли в защищенный Main!");
        return ResponseEntity.ok(message);
    }
    @GetMapping("/basket")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Basket>> getBasket() {
        if (Objects.equals(Main.currentUser, null)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(basketService.getBasket());
    }
    @PostMapping("/basket/drop/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity dropProductFromBasket(@PathVariable("id") long id) {
        basketService.dropProductFromBasket(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/products/{id}/toBasket")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity addProductToBasket(@PathVariable("id") long id) {
        String res = basketService.addProductToBasket(id);
        log.info(res);
        return ResponseEntity.ok(res);
    }
}
