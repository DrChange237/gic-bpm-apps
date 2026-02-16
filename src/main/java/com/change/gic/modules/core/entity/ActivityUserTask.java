package com.change.gic.modules.core.entity;


import com.change.gic.modules.core.converter.MapConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashMap;

@Getter
@Setter
@Entity
@Table(name = "ACTIVITY_USER_TASK")
public class ActivityUserTask extends Auditable{

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(length = 36, nullable = false, updatable = false, name = "ID")
    private String id;

    @Column(nullable = false)
    private String taskDefinitionKey;

    @Column(nullable = true, columnDefinition = "text")
    private String documentation;

    @Column(name = "MAP_VARIABLE", columnDefinition = "TEXT")
    @Convert(converter = MapConverter.class)
    private HashMap<String, String> mapVariable;

}
