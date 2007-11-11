package com.blueskyminds.analysis.property;

import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.framework.AbstractDomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 * A mapping of a premise to AggregateSets
 *
 * Used to persist mapping data for use in queries
 *
 * The entire mapping must be recalculated if the AggregateSet boundaries are changed, or of the property's
 *  attributes are updated
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class PremiseAggregateSetMap extends AbstractDomainObject {

    private Premise premise;
    private AggregateSet aggregateSet;

    // ------------------------------------------------------------------------------------------------------

    public PremiseAggregateSetMap(Premise premise, AggregateSet aggregateSet) {
        this.premise = premise;
        this.aggregateSet = aggregateSet;
    }

    /** Default constructor for ORM */
    protected PremiseAggregateSetMap() {

    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the PropertyAggregateSetMap with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the property that this entry corresponds to */
    @ManyToOne
    @JoinColumn(name = "PremiseId")
    public Premise getPremise() {
        return premise;
    }

    public void setPremise(Premise premise) {
        this.premise = premise;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the aggregateSet that this entry maps to */
    @ManyToOne
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }

    public void print() {
        System.out.println(getIdentityName()+" ("+ premise.getIdentityName()+":"+aggregateSet.getIdentityName()+")");
    }

}
