package api.Models.ProductEntities;

import java.util.List;

public interface IProduct {
    public long getProductid();
    public String getProductname();
    public String getCategory();

    public List<String> getCharacteristicsList();
}
