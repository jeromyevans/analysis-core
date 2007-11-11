package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;

/**
 * An entry in a UnionSet
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="analysis_UnionSetEntry")
public class UnionSetEntry extends AbstractEntity {

    private UnionSet unionSet;
    private AggregateSet aggregateSet;


    public UnionSetEntry(UnionSet unionSet, AggregateSet aggregateSet) {
        this.unionSet = unionSet;
        this.aggregateSet = aggregateSet;
    }


    /** Default constructor for ORM */
    public UnionSetEntry() {
    }

    @ManyToOne
    @JoinColumn(name="UnionSetId")
    public UnionSet getUnionSet() {
        return unionSet;
    }

    public void setUnionSet(UnionSet unionSet) {
        this.unionSet = unionSet;
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
