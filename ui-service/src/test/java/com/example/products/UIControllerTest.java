package com.example.products;

import com.example.products.model.Category;
import com.example.products.model.Product;
import com.example.products.web.UIController;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UIControllerTest {

    @Mock
    private ProductService productService;

    @Mock
    private SalesService salesService;

    @Mock
    private UserSession userSession;

    @Mock
    private Model model;

    @InjectMocks
    private UIController uiController;

    @Mock
    private HttpServletResponse response;

    @Test
    void displayProductList_shouldAddProductsAndReturnView() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Test");
        product.setDescription("Test");
        product.setCategory(Category.ACCESSORIES);
        product.setPrice(10.0);

        List<Product> products = List.of(product);
        when(productService.getAllProducts()).thenReturn(products);
        when(userSession.getUsername()).thenReturn("R");

        String view = uiController.displayProductList(model, response);

        verify(model).addAttribute("productList", products);
        verify(model).addAttribute("username", "R");
        assertEquals("productList", view);
    }

    @Test
    void createProduct_success() {
        Product p = new Product();
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String view = uiController.createProduct(p, redirectAttributes);

        verify(productService).addProduct(p);
        assertEquals("redirect:/", view);
        assertEquals("PRODUCT_CREATED_SUCCESS", redirectAttributes.getFlashAttributes().get("message"));
    }

    @Test
    void createProduct_error() {
        Product p = new Product();
        doThrow(new RuntimeException("Failed")).when(productService).addProduct(p);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String view = uiController.createProduct(p, redirectAttributes);

        assertEquals("redirect:/add-product", view);
        assertEquals("Failed to add product", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void editProduct_success() {
        when(userSession.isAuthenticated()).thenReturn(true);
        when(userSession.getJwtToken()).thenReturn("jwt-token");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        Product p = new Product();

        String result = uiController.editProduct(1L, p, redirectAttributes);

        verify(productService).updateProduct(1L, p, "jwt-token");
        assertEquals("redirect:/", result);
        assertEquals("PRODUCT_EDITED_SUCCESSFULLY", redirectAttributes.getFlashAttributes().get("message"));
    }

    @Test
    void editProduct_notLoggedIn() {
        when(userSession.isAuthenticated()).thenReturn(false);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String result = uiController.editProduct(1L, new Product(), redirectAttributes);

        assertEquals("redirect:/login", result);
        assertEquals("Please login first", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void search_shouldReturnProductList() {
        String keyword = "test";
        List<Product> products = List.of(new Product());
        when(productService.search(keyword)).thenReturn(products);

        String view = uiController.search(keyword, model);

        verify(model).addAttribute("productList", products);
        verify(model).addAttribute("keyword", keyword);
        assertEquals("productList", view);
    }

    @Test
    void search_shouldHandleException() {
        when(productService.search(anyString())).thenThrow(new RuntimeException("Search failed"));
        String view = uiController.search("fail", model);
        verify(model).addAttribute(eq("error"), contains("Unexpected error"));
        assertEquals("productList", view);
    }

    @Test
    void deleteProduct_success() {
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String view = uiController.deleteProduct(1L, redirectAttributes);

        verify(productService).deleteProduct(1L);
        assertEquals("redirect:/", view);
        assertEquals("PRODUCT_DELETED_SUCCESSFULLY", redirectAttributes.getFlashAttributes().get("message"));
    }

    @Test
    void deleteProduct_failure() {
        doThrow(new RuntimeException("Not found")).when(productService).deleteProduct(1L);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String view = uiController.deleteProduct(1L, redirectAttributes);

        assertEquals("redirect:/", view);
        assertEquals("PRODUCT_DELETE_FAILED", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void showEditProductPage_success() {
        when(userSession.isAuthenticated()).thenReturn(true);
        when(userSession.getJwtToken()).thenReturn("jwt-token");
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        Product product = new Product();
        when(productService.findProductById(1L, "jwt-token")).thenReturn(product);

        String view = uiController.showEditProductPage(1L, model, redirectAttributes);

        verify(model).addAttribute("productItem", product);
        assertEquals("editProduct", view);
    }

    @Test
    void showEditProductPage_notLoggedIn() {
        when(userSession.isAuthenticated()).thenReturn(false);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String view = uiController.showEditProductPage(1L, model, redirectAttributes);

        assertEquals("redirect:/login", view);
        assertEquals("Please login first", redirectAttributes.getFlashAttributes().get("error"));
    }

    @Test
    void sellProduct_success_shouldRedirectWithSuccessMessage() {
        Long productId = 1L;
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String viewName = uiController.sellProduct(productId, redirectAttributes);

        verify(salesService).sellProduct(productId);
        assertEquals("redirect:/", viewName);
        assertEquals("PRODUCT_SOLD_SUCCESS", redirectAttributes.getFlashAttributes().get("message"));
    }

    @Test
    void sellProduct_failure_shouldRedirectWithErrorMessage() {
        Long productId = 2L;
        String errorMessage = "Insufficient stock";

        doThrow(new RuntimeException(errorMessage)).when(salesService).sellProduct(productId);
        RedirectAttributes redirectAttributes = new RedirectAttributesModelMap();

        String viewName = uiController.sellProduct(productId, redirectAttributes);

        assertEquals("redirect:/", viewName);
        assertEquals(errorMessage, redirectAttributes.getFlashAttributes().get("error"));
    }
}