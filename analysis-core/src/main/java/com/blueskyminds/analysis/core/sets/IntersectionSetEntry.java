package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;

/**
 * An entry in an IntersectionSet
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="analysis_IntersectionSetEntry")
public class IntersectionSetEntry extends AbstractEntity {

    private IntersectionSet intersectionSet;
    private AggregateSet aggregateSet;


    public IntersectionSetEntry(IntersectionSet intersectionSet, AggregateSet aggregateSet) {
        this.intersectionSet = intersectionSet;
        this.aggregateSet = aggregateSet;
    }


    /** Default constructor for ORM */
    public IntersectionSetEntry() {
    }

    @ManyToOne
    @JoinColumn(name="IntersectionSetId")
    public IntersectionSet getIntersectionSet() {
        return intersectionSet;
    }

    public void setIntersectionSet(IntersectionSet intersectionSet) {
        this.intersectionSet = intersectionSet;
    }

    @ManyToOne
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    /**
     * Evaluate whether the specified object belongs in referenced set
     *
     * @param object
     * @return true if the domain belongs to the referenced set
     */
    @Transient
    public boolean isInSet(Object object) {
        return aggregateSet.isInSet(object);
    }
}
