package t.controller;


import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import t.data.Product;
import t.data.ProductRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    private Product product = new Product();

    public void save() {
        System.out.println("Saving Product");
        productRepository.save(product);

        PrimeFaces.current().dialog().showMessageDynamic(new FacesMessage("Saved:"+product));
        product = new Product();
    }

    public void onReturnFromLevel1(SelectEvent event) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Data Returned", event.getObject().toString()));
    }

    public List<Product> getAllProduct() {
        List<Product> products = new ArrayList<>();
       productRepository.findAll().forEach(products::add);
       return products;
    }


    public Product getProduct() {
        return product;
    }

}
