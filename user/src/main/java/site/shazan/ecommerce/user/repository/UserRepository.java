package site.shazan.ecommerce.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.shazan.ecommerce.user.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}

