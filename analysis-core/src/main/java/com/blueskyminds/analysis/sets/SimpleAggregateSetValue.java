package com.blueskyminds.analysis.sets;

import javax.persistence.*;

/**
 * Date Started: 17/06/2007
 * <p/>
 * History:
 * <p/>
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="SimpleAggreateSetValue")
public class SimpleAggregateSetValue {

    private AggregateSet aggregateSet;
    private String value;

    public SimpleAggregateSetValue(AggregateSet aggregateSet, String value) {
        this.aggregateSet = aggregateSet;
        this.value = value;
    }

    /** Default constructor for ORM */
    protected SimpleAggregateSetValue() {
    }

    @ManyToOne
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    @Basic
    @Column(name="Value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}