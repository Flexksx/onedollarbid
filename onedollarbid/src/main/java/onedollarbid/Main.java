package onedollarbid;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, String> env = System.getenv();

        Map<String, Object> properties = new HashMap<>();
        properties.put("jakarta.persistence.jdbc.url",
                "jdbc:postgresql://" + env.get("DB_HOST") + ":" + env.get("DB_PORT") + "/your_database");
        properties.put("jakarta.persistence.jdbc.user", env.get("DB_USER"));
        properties.put("jakarta.persistence.jdbc.password", env.get("DB_PASSWORD"));
        properties.put("jakarta.persistence.jdbc.driver", "org.postgresql.Driver");

        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("examplePU", properties);
        EntityManager entityManager = entityManagerFactory.createEntityManager();

        entityManager.getTransaction().begin();

    }
}