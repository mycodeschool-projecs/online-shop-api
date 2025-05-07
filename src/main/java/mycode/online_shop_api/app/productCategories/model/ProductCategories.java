package mycode.online_shop_api.app.productCategories.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import mycode.online_shop_api.app.categories.model.Category;
import mycode.online_shop_api.app.products.model.Product;

import java.io.Serializable;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "product_category")
@Entity(name = "ProductCategory")
public class ProductCategories implements Serializable {

    @Id
    @SequenceGenerator(
            name="product_category_sequence",
            sequenceName = "product_category_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "product_category_sequence"
    )

    @Column(
            name = "id"
    )
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @JsonManagedReference
    private Product product;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;



}
