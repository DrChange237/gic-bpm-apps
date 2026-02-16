package com.change.gic.modules.business.specification;

import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InscriptionSpecifications {

    public static Specification<Inscription> dateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> cb.between(root.get("creationDate"), start, end);
    }

    // Exemple de spécification combinant plusieurs conditions optionnelles (alternative pour le service)
    public static Specification<Inscription> withDynamicQuery(String nameSearch) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("reference"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("firstName"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("lastName"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("firstNameConjoint"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("lastNameConjoint"), nameSearch));
            }

            if (nameSearch != null) {
                predicates.add(cb.equal(root.get("status"), InscriptionStatus.valueOf(nameSearch)));
            }

            if(predicates.isEmpty()){
                return null;
            }

            // Combine all predicates with AND
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
