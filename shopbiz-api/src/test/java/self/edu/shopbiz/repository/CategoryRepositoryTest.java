package self.edu.shopbiz.repository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import self.edu.shopbiz.model.Category;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Created by mpjoshi on 10/31/19.
 */


@ExtendWith(MockitoExtension.class)
public class CategoryRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    public void testGetCategoryById(){

        Category returnCategory = new Category();

        when(categoryRepository.findById(any())).thenReturn(Optional.of(returnCategory));

        Optional<Category> categoryOptional = categoryRepository.findById(1);

        assertTrue(categoryOptional.isPresent());
    }


}
