package com.ccabank.signservice.dto.sign;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

/**
 * @author : <a href="mailto:marcelin.hamidou@cca-bank.com">Marcelin HAMIDOU NDAM</a>
 * @project : file-service
 * @Package : com.ccabank.fileservice.dto
 * <p>
 * @date: 12/08/2023
 * @time: 11:35
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
@ApiModel
public class FileDto implements Serializable {

    private String id;
    private String name;
    private String url;
    private long size;
    private String type;
    private String project;

    public FileDto() {
    }

    /**
     * Gets id.
     *
     * @return value of id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id value of id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets name.
     *
     * @return value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name value of name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets url.
     *
     * @return value of url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url value of url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Gets size.
     *
     * @return value of size
     */
    public long getSize() {
        return size;
    }

    /**
     * Sets size.
     *
     * @param size value of size
     */
    public void setSize(long size) {
        this.size = size;
    }

    /**
     * Gets type.
     *
     * @return value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type value of type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets project.
     *
     * @return value of project
     */
    public String getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project value of project
     */
    public void setProject(String project) {
        this.project = project;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("FileDto{");
        sb.append("id='").append(id).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", size=").append(size);
        sb.append(", type='").append(type).append('\'');
        sb.append(", project='").append(project).append('\'');
        sb.append('}');
        return sb.toString();
    }
}

