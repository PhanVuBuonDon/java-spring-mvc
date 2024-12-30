package vn.hoidanit.laptopshop.controller.admin;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import vn.hoidanit.laptopshop.domain.Product;
import vn.hoidanit.laptopshop.service.ProductService;
import vn.hoidanit.laptopshop.service.UploadService;

@Controller
public class ProductController {

    private final ProductService productService;
    private final UploadService uploadService;

    public ProductController(ProductService productService, UploadService uploadService) {
        this.productService = productService;
        this.uploadService = uploadService;
    }

    @GetMapping("/admin/product")
    public String getTableProducts(Model model) {
        List<Product> allProducts = this.productService.getAllProducts();
        model.addAttribute("allProducts", allProducts);
        return "admin/product/show";
    }

    @GetMapping("/admin/product/create")
    public String getCreateProductPage(Model model) {
        model.addAttribute("newProduct", new Product());
        return "admin/product/create";
    }

    @PostMapping(value = "/admin/product/create")
    public String createProductPage(Model model,
            @ModelAttribute("newProduct") @Valid Product newProduct,
            BindingResult newProductBindingResult,
            @RequestParam("productFile") MultipartFile file) {

        System.out.println(newProduct);
        List<FieldError> errors = newProductBindingResult.getFieldErrors();
        for (FieldError error : errors) {
            System.out.println(">>>>>>>" + error.getField() + " - " +
                    error.getDefaultMessage());
        }

        // validate
        if (newProductBindingResult.hasErrors()) {
            return "admin/product/create";
        }
        String productFile = this.uploadService.handleSaveUploadFile(file,
                "product");

        newProduct.setImage(productFile);
        // save
        this.productService.handleSaveProduct(newProduct);
        return "redirect:/admin/product";// redirect vào url
    }
}
