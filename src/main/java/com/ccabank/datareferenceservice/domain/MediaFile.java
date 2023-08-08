package com.ccabank.datareferenceservice.domain;

import java.io.File;

/**
 * @author : <a href="mailto:herve.foudjo@cca-bank.com">Herve FOUDJO</a>
 * @project : datareferenceservice
 * @Package : com.ccabank.datareferenceservice.domain
 * <p>
 * @date: 08/08/2023
 * @time: 10:32
 * <p>
 * Created with IntelliJ IDEA
 * To change this template use File | Settings | File Templates.
 */
public class MediaFile {

    FileType type;
    private int id;
    private String path;

    public MediaFile() {
    }

    /**
     * Gets id.
     *
     * @return value of id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets type.
     *
     * @return value of type
     */
    public FileType getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type value of type
     */
    public void setType(FileType type) {
        this.type = type;
    }

    /**
     * Gets path.
     *
     * @return value of path
     */
    public String getPath() {
        return path;
    }

    /**
     * Sets path.
     *
     * @param path value of path
     */
    public void setPath(String path) {
        this.path = path;
    }

    public File getFile() {
        return new File(path);
    }
}
