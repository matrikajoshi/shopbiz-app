package self.edu.shopbiz.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
@Api(tags = {"Products"})
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int INITIAL_PAGE = 0;

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(value = "List of products",
            notes = "Returns first N products specified by the size parameter with page offset specified by page parameter.",
            response = Page.class)
    @GetMapping(path="/products")
    public Page<Product> findAllProductsPageable(
            @ApiParam("The size of products in page to be returned") @RequestParam(value = "size", defaultValue = "4", required = false) Integer size,
            @ApiParam("Zero-based page index") @RequestParam(value = "page") Optional<Integer> page) {
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;


        Page<Product> products = productService.findAllProductsPageable(PageRequest.of(evalPage, size));
        return products;
    }


    @ApiOperation(value = "Get product by id", response = Product.class)
    @GetMapping(path="/products/{id}")
    public Product findProduct(@PathVariable("id") Long id){
        return productService.findProductById(id);
    }

    @ApiOperation(value = "Add a new product", response = Product.class)
    @PostMapping(path="/products")
    @RolesAllowed("MANAGE_PRODUCT")
    public Product addNewProduct(@RequestPart("multipartImage") MultipartFile multipartImage,
                                 @RequestPart(required = false) Product product) throws Exception {
        String fileName = multipartImage.getOriginalFilename();
        logger.info("Saving uploaded image: {}", fileName);

        return productService.save(multipartImage, product);
    }

    @ApiOperation(value = "Update existing product", response = Product.class)
    @PutMapping(path="/products/{id}", consumes = "multipart/form-data")
    @RolesAllowed("MANAGE_PRODUCT")
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

    @ApiOperation(value = "Delete Product", response = Product.class)
    @DeleteMapping(path="/products/{id}")
    @RolesAllowed("MANAGE_PRODUCT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }

}
