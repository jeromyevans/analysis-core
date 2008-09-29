package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;

import javax.persistence.*;

/**
 * An AggregateSet is used to group objects together for analysis
 *
 * Interface to a rule that determines whether a DomainObject belongs in a specific set
  *
 * Date Started: 19/08/2006
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name = "analysis_AggregateSet")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class AggregateSet extends AbstractEntity {

    private String key;

    protected AggregateSet(String key) {
        this.key = key;
    }

    protected AggregateSet() {
    }

    /**
     * Unique key used to identify this set
     * @return
     */
    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /** Evaluate whether the entity is in this Set */
    @Transient
    public abstract boolean isInSet(Object object);

    public String toString() {
        return getIdentityName();
    }


    /** Get the calculated name of this set */
    @Transient
    public abstract String getRuleName();
}
