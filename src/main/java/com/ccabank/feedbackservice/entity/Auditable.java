package com.ccabank.feedbackservice.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import java.util.Date;

import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author : <a href="mailto:patrick.simo@cca-bank.com">Patrick SIMO</a>
 * @project : entity-service
 * @Package : com.ccabank.userservice.entity
 * <p>
 * @date: 16/06/2023
 * @time: 10:14
 * <p>
 * Created with IntelliJ IDEA To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable<T> {

  @Temporal(TIMESTAMP)
  @CreatedDate
  @Column(name = "CREATED_DATE", updatable = false)
  protected Date creationDate;

  @Column(name = "LASTMOD_DATE")
  @LastModifiedDate
  @Temporal(TIMESTAMP)
  protected Date lastModifiedDate;

  @CreatedBy
  @Column(name = "CREATED_BY")
  protected T createdBy;

  @LastModifiedBy
  @Column(name = "MODIFIED_BY")
  protected T modifiedBy;

  public Auditable() {
  }

  /**
   * Gets creationDate.
   *
   * @return value of creationDate
   */
  public Date getCreationDate() {
    return creationDate;
  }

  /**
   * Sets creationDate.
   *
   * @param creationDate value of creationDate
   */
  public void setCreationDate(Date creationDate) {
    this.creationDate = creationDate;
  }

  /**
   * Gets lastModifiedDate.
   *
   * @return value of lastModifiedDate
   */
  public Date getLastModifiedDate() {
    return lastModifiedDate;
  }

  /**
   * Sets lastModifiedDate.
   *
   * @param lastModifiedDate value of lastModifiedDate
   */
  public void setLastModifiedDate(Date lastModifiedDate) {
    this.lastModifiedDate = lastModifiedDate;
  }

  /**
   * Gets createdBy.
   *
   * @return value of createdBy
   */
  public T getCreatedBy() {
    return createdBy;
  }

  /**
   * Sets createdBy.
   *
   * @param createdBy value of createdBy
   */
  public void setCreatedBy(T createdBy) {
    this.createdBy = createdBy;
  }

  /**
   * Gets modifiedBy.
   *
   * @return value of modifiedBy
   */
  public T getModifiedBy() {
    return modifiedBy;
  }

  /**
   * Sets modifiedBy.
   *
   * @param modifiedBy value of modifiedBy
   */
  public void setModifiedBy(T modifiedBy) {
    this.modifiedBy = modifiedBy;
  }

  @Override
  public String toString() {
    return "Auditable{" +
        "creationDate=" + creationDate +
        ", lastModifiedDate=" + lastModifiedDate +
        ", createdBy=" + createdBy +
        ", modifiedBy=" + modifiedBy +
        '}';
  }
}
