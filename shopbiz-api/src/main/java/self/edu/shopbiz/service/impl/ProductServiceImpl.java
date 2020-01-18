package self.edu.shopbiz.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import self.edu.shopbiz.exceptionUtil.ResourceNotFoundException;
import self.edu.shopbiz.model.Category;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.repository.CategoryRepository;
import self.edu.shopbiz.repository.ProductRepository;
import self.edu.shopbiz.service.ProductService;

import javax.transaction.Transactional;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by mpjoshi on 10/11/19.
 */

@Service
public class ProductServiceImpl implements ProductService{

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    public static final String FILE_UPLOAD_PATH = "image_uploads/";

    @Value("${upload-path}")
    String uploadPath;

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Page<Product> findAllProductsPageable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product findProductById(Long id){
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    @Override
    public Product save(MultipartFile multipartImage, Product product) throws Exception {
        String fileName = uploadPath + multipartImage.getOriginalFilename();
        File tempFile = new File(fileName);
        if (!multipartImage.isEmpty()) {
            try {
                byte[] bytes = multipartImage.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(tempFile));
                stream.write(bytes);
                stream.close();
            } catch (IOException e) {
                throw new Exception(e.getLocalizedMessage());
            }
        }
        LOGGER.info("Saved uploaded image temporarily");
        Category category = product.getCategory();
        Category category1 = categoryRepository.save(category);
        product.setCategory(category1);
        product.setImageUrl(multipartImage.getOriginalFilename());
        product.setUpdatedAt(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    public Product save(Product product) {
        Product dbProduct = productRepository.getOne(product.getId());
        //keep image path
        String imagePath = dbProduct.getImageUrl();
        if(StringUtils.isEmpty(product.getImageUrl())){
            product.setImageUrl(imagePath);
        }
//        Instant instant = Instant.now();
//        product.setLastUpdated(LocalDateTime.now());
        Product savedProduct = productRepository.save(product);
        return savedProduct;
    }

    @Override
    @Transactional
    public void deleteProduct(Long productId) {
        Product productById = this.findProductById(productId);
        productRepository.delete(productById);
    }

}
