package self.edu.shopbiz.Controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import self.edu.shopbiz.ShopbizApplication;
import self.edu.shopbiz.controller.CategoryContoller;
import self.edu.shopbiz.model.Category;
import self.edu.shopbiz.repository.CategoryRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by mpjoshi on 11/27/19.
 */

//@ExtendWith(SpringExtension.class)
//@WebMvcTest(CategoryContoller.class)
public class CategoryControllerTest {

//    @MockBean
//    CategoryRepository categoryRepository;
//
//    @Autowired
//    MockMvc mockMvc;
//
//    @Test
//    void testGetCategoryById() throws Exception {
//
//        Optional<Category> category = null;
//        given(categoryRepository.findById(any())).willReturn(category);
//
//        MvcResult result= mockMvc.perform(get("/api/categories/1"))
//                .andExpect(status().isOk())
//                .andReturn();
//
//        System.out.println(result.getResponse().getContentAsString());
//    }
}
