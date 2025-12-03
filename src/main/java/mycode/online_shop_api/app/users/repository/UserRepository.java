package mycode.online_shop_api.app.users.repository;

import mycode.online_shop_api.app.users.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = {"orders"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findById(long id);

    @EntityGraph(attributePaths = {"orders"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, long id);

}
