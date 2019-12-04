package self.edu.shopbiz.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import self.edu.shopbiz.model.Product;

/**
 * Created by mpjoshi on 10/11/19.
 */


public interface ProductService {
    Page<Product> findAllProductsPageable(Pageable pageable);

    Product findProductById(Long id);

    Product save(MultipartFile multipartImage, Product product) throws Exception;

    Product save(Product product);

    void deleteProduct(Long productId);
}
