package com.blueskyminds.analysis.core.sets;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

/**
 * Records an entry in an AggregateSetGroup
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="analysis_AggregateSetGroupEntry")
public class AggregateSetGroupEntry {

    private AggregateSetGroup group;
    private AggregateSet aggregateSet;


    public AggregateSetGroupEntry(AggregateSetGroup group, AggregateSet aggregateSet) {
        this.group = group;
        this.aggregateSet = aggregateSet;
    }

    /** Default constructor for ORM */
    public AggregateSetGroupEntry() {
    }

    @ManyToOne
    @JoinColumn(name="GroupId")
    public AggregateSetGroup getGroup() {
        return group;
    }

    public void setGroup(AggregateSetGroup group) {
        this.group = group;
    }

    @ManyToOne
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }
}
