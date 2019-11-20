package self.edu.shopbiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import self.edu.shopbiz.exceptionUtil.ResourceNotFoundException;
import self.edu.shopbiz.model.Category;
import self.edu.shopbiz.repository.CategoryRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by mpjoshi on 10/31/19.
 */

//(origins = "http://localhost:4200")
@RestController
@RequestMapping("/categories")
@Api(tags = {"Categories"})
public class CategoryContoller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories;
    }

    @GetMapping("/{id}")
    public @ResponseBody Category getCategoryById(@PathVariable("id") Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return category;
    }



}
