package self.edu.shopbiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.service.ProductService;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/11/19.
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int INITIAL_PAGE = 0;

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(path="/products")
    public List<Product> findAllProductsPageable(
            @RequestParam("page") Optional<Integer> page) {
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Product> products = productService.findAllProductsPageable(PageRequest.of(evalPage, 4));
        List<Product> productsContent = products.getContent();
        return productsContent;
    }

    @GetMapping(path="/products/{id}")
    public Product findProduct(@PathVariable("id") Long id){
        return productService.findProductById(id);
    }

    @PostMapping(path="/products")
    @RolesAllowed("ADD_PRODUCT")
    public Product addNewProduct(@RequestPart("multipartImage") MultipartFile multipartImage,
                                 @RequestPart(required = false) Product product) throws Exception {
        String fileName = multipartImage.getOriginalFilename();
        logger.info("Saving uploaded image: {}", fileName);

        return productService.save(multipartImage, product);
    }

    @PutMapping(path="/products/{id}", consumes = "multipart/form-data")
    @RolesAllowed("ADD_PRODUCT")
    public Product editProduct(@RequestPart(value = "multipartImage", required = false) MultipartFile multipartImage,
                               @RequestPart(value = "info", required = false) Product product,
                               @PathVariable("id") Long id) throws Exception {

        if(null != multipartImage){
            String fileName = multipartImage.getOriginalFilename();
            logger.info("Saving uploaded image: " + fileName);
            return productService.save(multipartImage, product);
        }else{
            return productService.save(product);
        }
    }

}
