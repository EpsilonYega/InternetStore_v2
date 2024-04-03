package api.Service;

import api.Models.Basket;
import api.Repositories.DataAccessLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasketService {
    private final DataAccessLayer dataAccessLayer;
    @Autowired
    public BasketService(DataAccessLayer dataAccessLayer) {
        this.dataAccessLayer = dataAccessLayer;
    }
    public List<Basket> getBasket() {
        return dataAccessLayer.getBasketFromDatabase();
    }
    public String addProductToBasket(long productId) {
        return dataAccessLayer.newBasketToDatabase(dataAccessLayer.getProductFromDatabaseByID(productId));
    }
    public void dropProductFromBasket(long productId) {
        dataAccessLayer.dropProductFromBasketByID(productId);
    }

}
