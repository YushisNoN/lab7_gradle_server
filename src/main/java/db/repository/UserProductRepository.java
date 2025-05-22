package db.repository;

import models.UserProducts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductRepository extends JpaRepository<UserProducts, Long> {

    List<UserProducts> getUserProductsByUserId(long id);

    int deleteByProductId(long id);

    int deleteAllByUserId(long id);

}
