package mycode.online_shop_api.app.categories.repository;

import mycode.online_shop_api.app.categories.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    private Category mockCategory;

    @BeforeEach
    void setUp() {
        mockCategory = new Category();
        mockCategory.setName("Electronics");
        categoryRepository.save(mockCategory);
    }

    @Test
    void shouldFindCategoryById() {
        Optional<Category> foundCategory = categoryRepository.findById(mockCategory.getId());

        assertTrue(foundCategory.isPresent());
        assertEquals("Electronics", foundCategory.get().getName());
    }

    @Test
    void shouldFindCategoryByName() {
        Optional<Category> foundCategory = categoryRepository.findByName("Electronics");

        assertTrue(foundCategory.isPresent());
        assertEquals(mockCategory.getId(), foundCategory.get().getId());
    }

    @Test
    void shouldReturnEmptyWhenCategoryNotFoundById() {
        Optional<Category> foundCategory = categoryRepository.findById(999);

        assertFalse(foundCategory.isPresent());
    }

    @Test
    void shouldReturnEmptyWhenCategoryNotFoundByName() {
        Optional<Category> foundCategory = categoryRepository.findByName("NonExistingCategory");

        assertFalse(foundCategory.isPresent());
    }
}
