package api.Models.ProductEntities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import api.Main;
import api.Models.Basket;
import api.Models.WarehouseEntities.Warehouse;
import java.util.List;

@Entity
@Data
@Table(name = "product", schema = "public", catalog = "internet_store_db")
public class Product implements IProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long productid;
    @Column(name = "productname")
    private String productname;
    @Column(name="img")
    private String img;
    @Column(name = "category")
    private String category;
    @ElementCollection
    @CollectionTable(name="Product_Characteristics", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "characteristicsList")
    private List<String> characteristicsList;
    @Column
    private double price;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "warehouse_id", referencedColumnName = "id")
    private Warehouse warehouse;

    @Column(name = "warehouse_id_number")
    private int warehouseId;

    private String warehouseAddress = "";

    public Product(){}

    public Product(String productName, String img, String category, List<String> characteristicsList, Warehouse warehouse, double price) {
        this.productname = productName;
        this.img = img;
        this.category = category;
        this.characteristicsList = characteristicsList;
        this.warehouse = warehouse;
        this.price = price;
    }
    public Basket addProductToBasket(){
        return new Basket(Main.currentUser, this, this.getProductname(), this.getPrice());
    }
}
