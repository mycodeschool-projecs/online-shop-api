package mycode.online_shop_api.app.products.repository;

import mycode.online_shop_api.app.products.mocks.ProductMockData;
import mycode.online_shop_api.app.products.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("Should find product by name")
    void testFindByName() {
        Product gamingLaptop = ProductMockData.createGamingLaptop();
        productRepository.save(gamingLaptop);

        Optional<Product> found = productRepository.findByName(gamingLaptop.getName());

        assertTrue(found.isPresent());
        assertEquals(gamingLaptop.getName(), found.get().getName());
        assertEquals(gamingLaptop.getPrice(), found.get().getPrice());
    }

    @Test
    @DisplayName("Should return products sorted ascending by price")
    void testSortedAsc() {
        productRepository.saveAll(ProductMockData.createSampleProducts());

        Optional<List<Product>> resultOpt = productRepository.sortedAsc();

        assertTrue(resultOpt.isPresent());
        List<Product> sortedAsc = resultOpt.get();
        assertEquals(3, sortedAsc.size());
        assertEquals("Cheap Phone", sortedAsc.get(0).getName());
        assertEquals("Expensive Phone", sortedAsc.get(2).getName());
    }

    @Test
    @DisplayName("Should return products sorted descending by price")
    void testSortedDesc() {
        productRepository.saveAll(ProductMockData.createSampleProducts());

        Optional<List<Product>> resultOpt = productRepository.sortedDesc();

        assertTrue(resultOpt.isPresent());
        List<Product> sortedDesc = resultOpt.get();
        assertEquals(3, sortedDesc.size());
        assertEquals("Expensive Phone", sortedDesc.get(0).getName());
        assertEquals("Cheap Phone", sortedDesc.get(2).getName());
    }

    @Test
    @DisplayName("Should save and retrieve product by ID")
    void testSaveAndFindById() {
        Product cheapLaptop = ProductMockData.createCheapLaptop();
        Product savedProduct = productRepository.save(cheapLaptop);

        Optional<Product> found = productRepository.findById(savedProduct.getId());

        assertTrue(found.isPresent());
        assertEquals("Cheap Laptop", found.get().getName());
    }
}