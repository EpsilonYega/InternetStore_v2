package api.Models.WarehouseEntities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "warehouse", schema = "public", catalog = "internet_store_db")
public class Warehouse implements IWarehouse{
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private long id;
    @Column(name = "warehouseAddress")
    private String warehouseAddress;
    public Warehouse(){}
    public Warehouse(String warehouseAddress) {
        this.warehouseAddress = warehouseAddress;
    }
}
