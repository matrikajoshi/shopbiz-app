package self.edu.shopbiz.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import self.edu.shopbiz.model.Category;
import self.edu.shopbiz.repository.CategoryRepository;

import java.util.Optional;

/**
 * Created by mpjoshi on 10/31/19.
 */

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/categories")
public class CategoryContoller {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    CategoryRepository categoryRepository;

    @GetMapping("/{id}")
    public @ResponseBody
    Optional<Category> getCategoryById(@PathVariable("id") Integer id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        return categoryOptional;
    }



}
