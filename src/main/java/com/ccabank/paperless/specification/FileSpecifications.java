package com.ccabank.paperless.specification;

import com.ccabank.paperless.entity.DocumentType;
import com.ccabank.paperless.entity.File;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FileSpecifications {

    public static Specification<File> dateBetween(LocalDateTime debut, LocalDateTime fin) {
        return (root, query, cb) -> cb.between(root.get("creationDate"), debut, fin);
    }

    // Exemple de spécification combinant plusieurs conditions optionnelles (alternative pour le service)
    public static Specification<File> withDynamicQuery(String reference, DocumentType type, String staff) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (reference != null && !reference.isEmpty()) {
                predicates.add(cb.like(root.get("request").get("reference"), reference));
            }

            if (type != null) {
                predicates.add(cb.equal(root.get("request").get("type"), type));
            }

            if (staff != null && !staff.isEmpty()) {
                predicates.add(cb.like(root.get("request").get("staff"), staff));
            }

            if(predicates.isEmpty()){
                return null;
            }

            // Combine all predicates with AND
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}
