package com.change.gic.modules.file.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "FI_FILE")
public class File extends Auditable implements Serializable {

    @Id
    @Column(name = "ID", nullable = false, length = 36)
    private String id;

    @NotNull
    @Column(name = "NAME", nullable = false)
    private String name;

    @NotNull
    @Column(name = "URL", nullable = false)
    private String url;

    @NotNull
    @Column(name = "TYPE", nullable = false)
    private String type;

    @NotNull
    @Column(name = "SIZE", nullable = false)
    private Long size;

//    @NotNull
    @Column(name = "DATA")
    private byte[] data;

    @Column(name = "PATH")
    private String path;

    @NotNull
    @Column(name = "PROJECT", nullable = false)
    private String project;

    @NotNull
    @Column(name = "IS_IN_FOLDER", nullable = false)
    private Boolean isInFolder;

    @Column(name = "IS_TEMP")
    private Boolean isTemp;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
    }
}
