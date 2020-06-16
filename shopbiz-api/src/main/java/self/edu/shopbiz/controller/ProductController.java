package self.edu.shopbiz.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import self.edu.shopbiz.model.Product;
import self.edu.shopbiz.service.ProductService;

import javax.annotation.security.RolesAllowed;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/11/19.
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(path="/products")
@Tag(name = "products")
public class ProductController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final int INITIAL_PAGE = 0;
    private static final int DEFAULT_SIZE = 10;

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping
    @Operation(description = "List all products", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = Product.class))), responseCode = "200")})
    public Page<Product> findAllProductsPageable(
            @Parameter(description = "The size of products in page to be returned")
            @RequestParam(value = "size", defaultValue = "4", required = false) Integer size,
            @Parameter(description = "Zero-based page index")
            @RequestParam(value = "page", required = false) Optional<Integer> page) {
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;


        Page<Product> products = productService.findAllProductsPageable(PageRequest.of(evalPage, size));
        return products;
    }


    @Operation(description = "Get product by id", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class)), responseCode = "200")})
    @GetMapping(path="/{id}")
    public Product findProduct(@PathVariable("id") Long id){
        return productService.findProductById(id);
    }


    @Operation(description = "Add a new product", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class)), responseCode = "200")})
    @PostMapping
    @RolesAllowed("MANAGE_PRODUCT")
    public Product addNewProduct(@RequestPart("multipartImage") MultipartFile multipartImage,
                                 @RequestPart(required = false) Product product) throws Exception {
        String fileName = multipartImage.getOriginalFilename();
        logger.info("Saving uploaded image: {}", fileName);

        return productService.save(multipartImage, product);
    }


    @Operation(description = "Update existing product", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class)), responseCode = "200")})
    @PutMapping(path="/{id}", consumes = "multipart/form-data")
    @RolesAllowed("MANAGE_PRODUCT")
    public Product editProduct(@RequestPart(value = "multipartImage", required = false) MultipartFile multipartImage,
                               @RequestPart(value = "info", required = false) Product product,
                               @PathVariable("id") Long productId) throws Exception {
        if(!productId.equals(product.getId())) {
           throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Product id mismatch");
        }

        if(null != multipartImage){
            String fileName = multipartImage.getOriginalFilename();
            logger.info("Saving uploaded image: " + fileName);
            return productService.save(multipartImage, product);
        }else{
            return productService.save(product);
        }
    }


    @Operation(description = "Delete Product", responses = {
            @ApiResponse(content = @Content(schema = @Schema(implementation = Product.class)), responseCode = "200")})
    @DeleteMapping(path="/{id}")
    @RolesAllowed("MANAGE_PRODUCT")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteProduct(productId);
    }


    @GetMapping(path="/search/byName")
    public Page<Product> findAllProductsByNamePageable(@RequestParam String name) {
        return productService.searchProductsByName(name, PageRequest.of(INITIAL_PAGE, DEFAULT_SIZE));
    }

}
