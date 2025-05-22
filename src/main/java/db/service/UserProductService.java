package db.service;

import db.repository.UserProductRepository;
import jakarta.transaction.Transactional;
import models.UserProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductService {
    private final UserProductRepository userProductRepository;

    @Autowired
    public UserProductService(UserProductRepository userProductRepository) {
        this.userProductRepository = userProductRepository;
    }

    public List<UserProducts> getProductByUserId(long userId) {
        return this.userProductRepository.getUserProductsByUserId(userId);
    }

    public UserProducts saveUserProducts(UserProducts userProducts) {
        return this.userProductRepository.save(userProducts);
    }

    public int deleteByProductI(long id) {
        return this.userProductRepository.deleteByProductId(id);

    }
    @Modifying
    @Transactional
    public int deleteAllByUserId(long id) {
        return this.userProductRepository.deleteAllByUserId(id);
    }
}
