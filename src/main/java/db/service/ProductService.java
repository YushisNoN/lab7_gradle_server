package db.service;

import db.repository.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import models.Product;
import models.UserProducts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.TreeSet;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product createProduct(@Valid Product product) {
        return productRepository.save(product);
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Такого продукта не существует в базе данных"));
    }

    public long getLastId() {
        return productRepository.findMaxId().longValue();
    }

    public Product updateProduct(Product product) {
        return this.productRepository.save(product);
    }

    @Modifying
    @Transactional
    public int clearObjects(long userId) {
        return this.productRepository.deleteProductByUserId(userId);
    }
    public List<Product> getCollectionById(long id) {
        return this.productRepository.findProductsByUserId(id);
    }

    public List<Product> getAllCollection() {
        return this.productRepository.findAll();
    }

    @Modifying
    @Transactional
    public void deleteAllProducts(List<Product> products) {
        this.productRepository.deleteAll(products);
    }
}
