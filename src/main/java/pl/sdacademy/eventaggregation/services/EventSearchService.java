package pl.sdacademy.eventaggregation.services;

import org.springframework.stereotype.Service;
import pl.sdacademy.eventaggregation.domain.Event;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Service
public class EventSearchService {

    private final EntityManager entityManager;

    public EventSearchService(final EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Event> searchEvents(final String title, final String host, final String address) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Event> query = builder.createQuery(Event.class);
        final Root<Event> root = query.from(Event.class);
        final Predicate predicate = preparePredicate(title, host, address, builder, root);
        final CriteriaQuery<Event> buildQuery = query.select(root).where(predicate);
        return entityManager.createQuery(buildQuery).getResultList();
    }

    private Predicate preparePredicate(final String title, final String host, final String address,
                                       final CriteriaBuilder builder, final Root<Event> root) {
        final List<Predicate> predicates = new ArrayList<>();
        if(nonNull(title)) {
            predicates.add(builder.like(root.get("title"), title));
        }
        if (nonNull(host)) {
            predicates.add(builder.like(root.get("host"), host));
        }
        if (nonNull(address)) {
            predicates.add(builder.like(root.get("address"), address));
        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }
}
