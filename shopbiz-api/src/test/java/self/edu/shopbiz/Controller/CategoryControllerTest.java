package self.edu.shopbiz.Controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import self.edu.shopbiz.controller.CategoryController;
import self.edu.shopbiz.model.Category;
import self.edu.shopbiz.repository.CategoryRepository;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by mpjoshi on 11/27/19.
 */

@AutoConfigureMockMvc
@ContextConfiguration(classes = { CategoryController.class,
        CategoryRepository.class})
@WebMvcTest(CategoryController.class)
public class CategoryControllerTest {

    private final static String TEST_USER_ID = "user-id-123";

    @MockBean
    CategoryRepository categoryRepository;

    @Autowired
    MockMvc mockMvc;

    Category validCategory;

    @BeforeEach
    void setUp() {
        validCategory = new Category(1,"CatName", "CatDesc");
    }

    @Test
    @WithMockUser
    void testGetCategoryById() throws Exception {

        Optional<Category> category = null;
        given(categoryRepository.findById(any())).willReturn(Optional.ofNullable(validCategory));

        MvcResult result= mockMvc.perform(MockMvcRequestBuilders.get("/categories/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}
