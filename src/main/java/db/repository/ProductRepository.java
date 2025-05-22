package db.repository;
import models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.TreeSet;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN UserProducts up ON p.id = up.productId WHERE up.userId = :userId")
    List<Product> findProductsByUserId(@Param("userId") long userId);

    @Query("SELECT MAX(p.id) FROM Product p")
    Long findMaxId();

    @Query("SELECT p FROM Product p JOIN UserProducts up ON p.id = up.productId WHERE up.userId =: userId")
    List<Product> findProductsById(@Param("userId") long id);


    @Modifying
    @Query("DELETE FROM Product p WHERE p.id IN (SELECT up.productId FROM UserProducts up WHERE up.userId=:userId)")
    int deleteProductByUserId(@Param("userId") long id);
}
