package mycode.online_shop_api.app.categories.repository;

import mycode.online_shop_api.app.categories.model.Category;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Integer> {


    @EntityGraph(attributePaths = {"productCategories"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Category> findById(int id);

    @EntityGraph(attributePaths = {"productCategories"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Category> findByName(String name);

}