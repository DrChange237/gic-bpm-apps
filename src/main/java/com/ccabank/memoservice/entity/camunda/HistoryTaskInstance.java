package com.ccabank.memoservice.entity.camunda;


import lombok.Getter;
import lombok.Setter;
import org.camunda.bpm.engine.history.HistoricTaskInstance;

import javax.persistence.*;


@Entity
@Table(name = "act_hi_taskinst")
public class HistoryTaskInstance {

    @Id
    @Basic(optional = false)
    @Column(name = "id_", columnDefinition = "serial")
    private String id;

    @Basic(optional = true)
    @Column(name = "name_")
    private String name;

    @Basic(optional = true)
    @Column(name = "end_time_")
    private String endtime;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEndtime() {
        return endtime;
    }
}
