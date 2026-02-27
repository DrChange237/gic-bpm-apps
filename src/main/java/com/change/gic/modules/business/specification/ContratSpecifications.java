package com.change.gic.modules.business.specification;

import com.change.gic.modules.business.entity.Contrat;
import com.change.gic.modules.business.entity.Inscription;
import com.change.gic.modules.business.enumeration.ContratStatus;
import com.change.gic.modules.business.enumeration.InscriptionStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContratSpecifications {

    public static Specification<Contrat> dateBetween(LocalDate start, LocalDate end) {
        return (root, query, cb) -> cb.between(root.get("creationDate"), start, end);
    }

    public static Specification<Contrat> archived(Boolean archived) {
        return (root, query, cb) -> cb.equal(root.get("archived"), archived);
    }

    // Exemple de spécification combinant plusieurs conditions optionnelles (alternative pour le service)
    public static Specification<Contrat> withDynamicQuery(String nameSearch) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("reference"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.equal(root.get("consultation").get("inscription").get("firstName"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.equal(root.get("consultation").get("inscription").get("lastName"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.equal(root.get("consultation").get("inscription").get("firstNameConjoint"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.equal(root.get("consultation").get("inscription").get("lastNameConjoint"), nameSearch));
            }

            if (nameSearch != null && !nameSearch.isEmpty()) {
                predicates.add(cb.like(root.get("status"), nameSearch));
            }

            if(predicates.isEmpty()){
                return null;
            }

            // Combine all predicates with AND
            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }
}
