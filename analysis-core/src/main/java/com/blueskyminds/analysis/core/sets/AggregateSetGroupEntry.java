package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;

/**
 * Records an entry in an AggregateSetGroup
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
@Entity
@Table(name="analysis_AggregateSetGroupEntry")
public class AggregateSetGroupEntry extends AbstractEntity {

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

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name="AggregateSetId")
    public AggregateSet getAggregateSet() {
        return aggregateSet;
    }

    public void setAggregateSet(AggregateSet aggregateSet) {
        this.aggregateSet = aggregateSet;
    }
}
