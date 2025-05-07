package mycode.online_shop_api.app.categories.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import mycode.online_shop_api.app.productCategories.model.ProductCategories;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static jakarta.persistence.GenerationType.SEQUENCE;

@AllArgsConstructor
@ToString
@NoArgsConstructor
@Data
@Getter
@Setter
@Builder
@Table(name = "category")
@Entity(name = "Category")
public class Category implements Serializable {

    @Id
    @SequenceGenerator(
            name = "category_sequence",
            sequenceName = "category_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "category_sequence"
    )
    @Column(name = "id")
    private int id;

    @NotBlank(message = "Category cannot be empty")
    @Column(name = "name", nullable = false, columnDefinition = "TEXT")
    private String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private Set<ProductCategories> productCategories = new HashSet<>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Category> subcategories = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;


    public void addSubcategory(Category subcategory) {
        this.subcategories.add(subcategory);
        subcategory.setParent(this);
    }

    public void removeSubcategory(Category subcategory) {
        this.subcategories.remove(subcategory);
        subcategory.setParent(null);
    }

    private void addProductCategory(ProductCategories productCategory) {
        this.productCategories.add(productCategory);
        productCategory.setCategory(this);
    }

    private void removeProductCategory(ProductCategories productCategory) {
        this.productCategories.remove(productCategory);
        productCategory.setCategory(null);
    }
}
