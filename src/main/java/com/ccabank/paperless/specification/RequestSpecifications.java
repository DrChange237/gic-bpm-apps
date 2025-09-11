package com.ccabank.paperless.specification;

import com.ccabank.paperless.entity.DocumentType;
import com.ccabank.paperless.entity.File;
import com.ccabank.paperless.entity.Request;
import com.ccabank.paperless.entity.RequestStatus;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RequestSpecifications {

    public static Specification<Request> dateBetween(LocalDate debut, LocalDate fin) {
        return (root, query, cb) -> cb.between(root.get("createdAt"), debut, fin);
    }

    // Exemple de spécification combinant plusieurs conditions optionnelles (alternative pour le service)
    public static Specification<Request> withDynamicQuery(String reference, DocumentType type, String staff, RequestStatus status) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (reference != null && !reference.isEmpty()) {
                predicates.add(cb.like(root.get("reference"), reference));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("type"), type));
            }

            if (staff != null && !staff.isEmpty()) {
                predicates.add(cb.like(root.get("staff"), staff));
            }

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if(predicates.isEmpty()){
                return null;
            }

            // Combine all predicates with AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
