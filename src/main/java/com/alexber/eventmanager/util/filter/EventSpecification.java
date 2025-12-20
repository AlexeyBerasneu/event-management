package com.alexber.eventmanager.util.filter;

import com.alexber.eventmanager.entity.event.EventEntity;
import com.alexber.eventmanager.entity.event.EventSearchRequestDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EventSpecification {

    public static Specification<EventEntity> byFilter(EventSearchRequestDto filter) {
        return ((root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.name() != null && !filter.name().isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("eventName")), "%" + filter.name().trim().toLowerCase() + "%"));
            }

            if (filter.costMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("cost"), filter.costMin()));
            }
            if (filter.costMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("cost"), filter.costMax()));
            }

            if (filter.durationMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("duration"), filter.durationMin()));
            }

            if (filter.durationMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("duration"), filter.durationMax()));
            }

            if (filter.placesMin() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("maxPlaces"), filter.placesMin()));
            }

            if (filter.placesMax() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("maxPlaces"), filter.placesMax()));
            }

            if (filter.dateStartAfter() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("date"), filter.dateStartAfter()));
            }

            if (filter.dateStartBefore() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("date"), filter.dateStartBefore()));
            }

            if (filter.locationId() != null) {
                predicates.add(cb.equal(root.get("location").get("id"), filter.locationId()));
            }

            if(filter.eventStatus() != null) {
                predicates.add(cb.equal(root.get("status"), filter.eventStatus()));
            }
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        });
    }
}
