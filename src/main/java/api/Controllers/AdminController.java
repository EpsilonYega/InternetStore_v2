package api.Controllers;

import api.Main;
import api.Models.ProductEntities.Product;
import api.Models.WarehouseEntities.Warehouse;
import api.Service.ProductService;
import api.Service.WarehouseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Objects;

@RestController
@Slf4j
@RequestMapping("/admin")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {
    private final ProductService productService;
    private final WarehouseService warehouseService;
    @Autowired
    public AdminController(ProductService productService, WarehouseService warehouseService) {
        this.productService = productService;
        this.warehouseService = warehouseService;
    }
    @GetMapping("/adminPage")
    @CrossOrigin(origins = "http://localhost:3000")
    public String userAccess(Principal principal){
        if (principal == null)
            return null;
        // TODO: 02.12.2023 Исправить
        if (Objects.equals(Main.currentUser.getRole(), "ADMIN"))
            return principal.getName();
        return "Вам сюда нельзя!";
    }
    @PostMapping("/products/new")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity newProduct(@RequestBody Product product) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3")) {
            productService.newProduct(product);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/products/update/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity updateProduct(@PathVariable("id") long id, @RequestBody Product product) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3")) {
            productService.updateProduct(id, product);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/products/drop/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity dropProduct(@PathVariable("id") long id) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3")) {
            productService.dropProduct(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/warehouses")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<List<Warehouse>> getWarehouseList() {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3"))
            return ResponseEntity.ok(warehouseService.getWarehouseList());
        return ResponseEntity.badRequest().build();
    }
    @GetMapping("/warehouses/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable("id") long id) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3"))
            return ResponseEntity.ok(warehouseService.getWarehouseByID(id));
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/warehouses/new")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity newWarehouse(@RequestBody Warehouse warehouse) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3")) {
            warehouseService.newWarehouse(warehouse);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/warehouses/update/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity updateWarehouse(@PathVariable("id") long id, @RequestBody Warehouse warehouse) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3")) {
            warehouseService.updateWarehouse(id, warehouse);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    @PostMapping("/warehouses/drop/{id}")
    @CrossOrigin(origins = "http://localhost:3000")
    public ResponseEntity dropWarehouse(@PathVariable("id") long id) {
        if (Objects.equals(Main.currentUser.getUsername(), "admin") && Objects.equals(Main.currentUser.getEmail(), "admin@sorokastore.ru") && Objects.equals(Main.currentUser.getPassword(), "21232F297A57A5A743894A0E4A801FC3")) {
            warehouseService.dropWarehouse(id);
            log.info("Вы успешно удалили склад");
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
