package pl.sdacademy.eventaggregation.repositories;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.sdacademy.eventaggregation.domain.Role;
import pl.sdacademy.eventaggregation.domain.User;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class UserRepository {
 private final EntityManager entityManager;



    public UserRepository(EntityManager entityManager) {
        this.entityManager = entityManager;

    }

    public User create(final User user){
        user.setRole(Role.NORMAL_USER);
        entityManager.persist(user);
        return user;
    }

    public Optional<User> findByEmail(final String email){
        final List<User> result = entityManager.createQuery("SELECT u FROM users u WHERE u.email=:email", User.class)
                .setParameter("email", email)
                .getResultList();
        if(result.size() == 1){
            return Optional.of(result.get(0));
        }
        return Optional.empty();
    }
    public Optional<User> findByUsername(final String username){
        final List<User> result = entityManager.createQuery("SELECT u FROM users u WHERE u.username=:username", User.class)
                .setParameter("username", username)
                .getResultList();
        if(result.size() == 1){
            return Optional.of(result.get(0));
        }
        return Optional.empty();
    }





}
