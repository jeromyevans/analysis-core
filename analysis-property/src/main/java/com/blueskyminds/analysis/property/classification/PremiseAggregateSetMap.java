package com.blueskyminds.analysis.property.classification;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.framework.AbstractEntity;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * A mapping of a premise to an AggregateSet
 *
 * Mapping of Premises to AggregateSets is pre-calculated.
 *
 * Used to persist mapping data required by queries
 *
 * The entire mapping must be recalculated if the AggregateSet boundaries are changed, or of the Premise's
 *  attributes are updated
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="landmine_PremiseAggregateSetMap")
public class PremiseAggregateSetMap extends AbstractEntity {

    private Premise premise;
    private AggregateSet aggregateSet;

    public PremiseAggregateSetMap(Premise premise, AggregateSet aggregateSet) {
        this.premise = premise;
        this.aggregateSet = aggregateSet;
    }

    /** Default constructor for ORM */
    protected PremiseAggregateSetMap() {

    }

    /**
     * Initialise the PropertyAggregateSetMap with default attributes
     */
    private void init() {
    }


    /** Get the property that this entry corresponds to */
    @ManyToOne
    @JoinColumn(name = "PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    /** Get the aggregateSet that this entry maps to */
    @ManyToOne
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    public String toString() {
        return (getIdentityName()+" ("+ premise.getIdentityName()+":"+aggregateSet.getIdentityName()+")");
    }

}
