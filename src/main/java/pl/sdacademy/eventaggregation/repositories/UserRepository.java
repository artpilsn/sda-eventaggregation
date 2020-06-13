package pl.sdacademy.eventaggregation.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.User;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class UserRepository {
 private final EntityManager entityManager;

    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public User create(final User user){
        entityManager.persist(user);
        return user;
    }
}
