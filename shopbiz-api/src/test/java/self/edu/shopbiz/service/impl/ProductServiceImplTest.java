package self.edu.shopbiz.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.util.JsonUtil;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

//    @BeforeEach
//    void setUp() {
//    }

    @Test
    void findAllProductsPageable() {
    }

    @Test
    void findProductById() {
    }

    @Test
    void save() {
    }

    @Test
    void testSave() {
    }

    @Test
    void deleteProduct() {
    }

    @Test
    public void whenSerializingJava8Date_thenCorrect() throws IOException {
        Product product = getProduct("Test");
        byte[] bytes = JsonUtil.toJson(product);
        String json = new String(bytes, "UTF-8");
        System.out.println(json);
    }

    private Product getProduct(String name) {
        Product product = new Product();
        product.setName(name);
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }
}