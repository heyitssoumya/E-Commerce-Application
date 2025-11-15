package dev.soumya.ProductServiceApr25.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Category extends BaseModel {
    /*
    One category can have multiple products
     */
    @OneToMany(fetch = FetchType.EAGER)
    //If we do not use @JoinColumn here then spring data jpa will create a foreign key in the many
    //side table (Product table) and also create a mapping table. In this case we do not want a
    // mapping table as foreign key is sufficient. So using this annotation we tell spring to
    //create foreign key of id column of Category table in Product table with name category_id
    @JoinColumn(name = "category_id")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
