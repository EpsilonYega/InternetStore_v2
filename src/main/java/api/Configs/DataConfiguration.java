package api.Configs;

import api.Models.Basket;
import api.Models.User;
import api.Models.ProductEntities.Product;
import api.Models.WarehouseEntities.Warehouse;
import api.Repositories.DataAccessLayer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceContext;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class DataConfiguration {
    private SessionFactory sessionFactory = null;

//    @Bean
//    public SessionFactory sessionFactory() {
//        return sessionFactory;
//    }

    @Autowired
    public DataConfiguration(EntityManager entityManager) {
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpa-tutorial");
//        EntityManager entityManager = emf.createEntityManager();
        Session session = entityManager.unwrap(org.hibernate.Session.class);
        sessionFactory = session.getSessionFactory();
    }
    @Bean
    public DataAccessLayer dataAccessLayer(){
        return new DataAccessLayer(sessionFactory);
    }

}
