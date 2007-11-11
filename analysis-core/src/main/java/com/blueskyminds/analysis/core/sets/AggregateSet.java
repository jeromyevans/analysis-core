package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.AbstractEntity;

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

    /** Get the calculated name of this set */
    @Transient
    public abstract String getName();

    // ------------------------------------------------------------------------------------------------------

    /** Evaluate whether the entity is in this Set */
    @Transient
    public abstract boolean isInSet(Object object);

    public String toString() {
        return getIdentityName();
    }

}
