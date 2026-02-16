package com.change.gic.modules.core.info;

import com.change.gic.modules.core.entity.Module;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
public class ProcessInfo {
    private String id;
    private ModuleInfo module;
    private String key;
    private String name;
    private String description;
}
