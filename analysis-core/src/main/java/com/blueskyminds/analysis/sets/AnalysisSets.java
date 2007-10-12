package com.blueskyminds.analysis.sets;

import com.blueskyminds.framework.DomainObjectList;

import javax.persistence.*;
import java.util.List;

/**
 * Contains all the different sets that the analysis engine should use
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
@Entity
public class AnalysisSets extends DomainObjectList<AggregateSet> {

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AnalysisSets with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new Set */
    public AggregateSet defineSet(AggregateSet series) {
        return super.create(series);
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of aggregate sets in this collection */
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name="AnalysisSetsEntry",
            joinColumns=@JoinColumn(name="AnalysisSetsId"),
            inverseJoinColumns = @JoinColumn(name="AggregateSetId")
    )
    public List<AggregateSet> getAggregateSets() {
        return super.getDomainObjects();
    }

    protected void setAggregateSets(List<AggregateSet> aggregateSets) {
        super.setDomainObjects(aggregateSets);
    }

    // ------------------------------------------------------------------------------------------------------
}
