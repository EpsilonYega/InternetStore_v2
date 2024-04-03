package api.Repositories;

import api.Configs.HashPassword;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import api.Main;
import api.Models.Basket;
import api.Models.User;
import api.Models.ProductEntities.Product;
import api.Models.WarehouseEntities.Warehouse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


@Repository
@Getter
public class DataAccessLayer {
    private final SessionFactory sessionFactory;
    @Autowired
    public DataAccessLayer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    Session session = null;
    public String newUserToDatabase(User user) {
        session = sessionFactory.openSession();
        session.beginTransaction();
        String name = user.getUsername();

        Query query = session
                .createQuery("FROM User where username = :username")
                .setParameter("username", name);
        User userFrom = (User) query.uniqueResult();

        if (userFrom != null) {
            return "Выберите другое имя";
        }

        String useremail = user.getEmail();

        query = session
                .createQuery("FROM User where email = :email")
                .setParameter("email", useremail);
        userFrom = (User) query.uniqueResult();

        if (userFrom != null) {
            return "Выберите другую почту";
        }
        //TODO Удали
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = md5.digest(user.getPassword().getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        user.setPassword(builder.toString());

//        user.setPassword(userService.hashUserPassword(user.getPassword()));
        session.persist(user);
        session.getTransaction().commit();
        session.close();
        return "Победа)";
    }
    public User getUserFromDatabaseByUsername(String name){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Query query = session
                .createQuery("FROM User where username = :username")
                .setParameter("username", name);
        User userFrom = (User) query.uniqueResult();
        if (userFrom == null) {
            return null;
        }
        return userFrom;
    }
    public List<Basket> getBasketFromDatabase() {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        long userId = Main.currentUser.getUserid();
        User localUser = session.get(User.class, userId);
        List<Basket> result = session
                .createQuery("from Basket where user = :user")
                .setParameter("user", localUser)
                .list();
        return result;
    }
    public String newBasketToDatabase(Product product) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Basket> query = builder.createQuery(Basket.class);
        Root<Basket> root = query.from(Basket.class);
        query.select(root);
        List<Basket> basketList = session.createQuery(query).getResultList();

        for (Basket basket : basketList) {
            if (Objects.equals(basket.getProduct().getProductid(), product.getProductid())) {
                return "К сожалению, данный товар уже зарезервирован";
            }
        }

        session.persist(product.addProductToBasket());
        session.getTransaction().commit();
        session.close();
        return "Товар успешно добавлен";
    }
    public void dropProductFromBasketByID(long basketId) {
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        long userId = Main.currentUser.getUserid();
        Basket basket = session.get(Basket.class, basketId);
        if (basket.getUser().getUserid() == userId) {
            session.remove(basket);
        } else {
            System.out.println(" ");
        }
        session.getTransaction().commit();
        session.close();
    }

    public void newProductToDatabase(Product product){

        session = sessionFactory.openSession();
        session.getTransaction().begin();

        Warehouse localWarehouse = session.get(Warehouse.class, product.getWarehouseId());
        product.setWarehouse(localWarehouse);
        product.setWarehouseAddress(localWarehouse.getWarehouseAddress());

        session.persist(product);
        session.getTransaction().commit();
        session.close();
    }
    public List<Product> getProductsFromDatabase(){

        session = sessionFactory.openSession();
        session.getTransaction().begin();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
        query.select(root);

        return session.createQuery(query).getResultList();
    }
    public Product getProductFromDatabaseByID(long id){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session.get(Product.class, id);
    }
    public List<Product> searchProducts(String searchQuery) {

        session = sessionFactory.openSession();

        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

        Predicate namePredicate = builder.like(builder.lower(root.get("productname")), "%" + searchQuery.toLowerCase() + "%");
        Predicate categoryPredicate = builder.like(builder.lower(root.get("category")), "%" + searchQuery.toLowerCase() + "%");

        Predicate searchPredicate = builder.or(namePredicate, categoryPredicate);
        query.select(root).where(searchPredicate);

        return session.createQuery(query).getResultList();
    }

    public void updateProductFromDatabaseByID(long id, Product newProduct){

        session = sessionFactory.openSession();
        session.getTransaction().begin();

        Product product = session.get(Product.class, id);

        if (!(newProduct.getProductname() == null || newProduct.getProductname().isEmpty())) product.setProductname(newProduct.getProductname());

        if (!(newProduct.getImg() == null || newProduct.getImg().isEmpty())) product.setImg(newProduct.getImg());

        if (!(newProduct.getCategory() == null || newProduct.getCategory().isEmpty())) product.setCategory(newProduct.getCategory());

        if (!newProduct.getCharacteristicsList().isEmpty()) product.setCharacteristicsList(newProduct.getCharacteristicsList());

        if (!(newProduct.getPrice() == 0)) product.setPrice(newProduct.getPrice());

        if (!(newProduct.getWarehouseId() == 0)) {
            product.setWarehouseId(newProduct.getWarehouseId());
            Warehouse warehouse = session.get(Warehouse.class, newProduct.getWarehouseId());
            product.setWarehouse(warehouse);
            product.setWarehouseAddress(warehouse.getWarehouseAddress());
        }

        session.merge(product);
        session.getTransaction().commit();
        session.close();
    }
    public void dropProductFromDatabaseByID(long id){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Product product = session.get(Product.class, id);
        session.remove(product);
        session.getTransaction().commit();
        session.close();
    }

    public void newWarehouseToDatabase(Warehouse warehouse){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        session.persist(warehouse);
        session.getTransaction().commit();
        session.close();
    }
    public List<Warehouse> getWarehousesFromDatabase(){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Warehouse> query = builder.createQuery(Warehouse.class);
        Root<Warehouse> root = query.from(Warehouse.class);
        query.select(root);
        return session.createQuery(query).getResultList();
    }
    public Warehouse getWarehouseFromDatabaseByID(long id){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        return session.get(Warehouse.class, id);
    }

    public void updateWarehouseFromDatabaseByID(long id, Warehouse newWarehouse){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Warehouse warehouse = session.get(Warehouse.class, id);
        warehouse.setWarehouseAddress(newWarehouse.getWarehouseAddress());
        session.merge(warehouse);
        session.getTransaction().commit();
        session.close();
    }
    public void dropWarehouseFromDatabaseByID(long id){
        session = sessionFactory.openSession();
        session.getTransaction().begin();
        Warehouse warehouse = session.get(Warehouse.class, id);
        session.remove(warehouse);
        session.getTransaction().commit();
        session.close();
    }
    public String hashUserPassword(String password) {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] bytes = md5.digest(password.getBytes());
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02X", b));
        }
        return builder.toString();
    }
}
