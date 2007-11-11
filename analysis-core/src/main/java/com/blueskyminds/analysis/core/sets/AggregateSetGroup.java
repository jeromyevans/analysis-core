package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.framework.AbstractEntity;
import com.blueskyminds.framework.transformer.Transformer;
import com.blueskyminds.framework.tools.filters.FilterTools;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * A group of AggregateSets for analysis
 *
 * Date Started: 19/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="analysis_AggregateSetGroup")
public class AggregateSetGroup extends AbstractEntity {

    private Set<AggregateSetGroupEntry> aggregateSets;

    public AggregateSetGroup() {
        aggregateSets = new HashSet<AggregateSetGroupEntry>();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AnalysisSets with default attributes
     */
    private void init() {
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new Set */
    public void defineSet(AggregateSet series) {
        aggregateSets.add(new AggregateSetGroupEntry(this, series));
    }

    // ------------------------------------------------------------------------------------------------------

    /** Get the list of aggregate sets in this collection */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "Group")
    protected Set<AggregateSetGroupEntry> getAggregateSetGroupEntries() {
        return aggregateSets;
    }

    protected void SetAggregateSetGroupEntries(Set<AggregateSetGroupEntry> aggregateSets) {
        this.aggregateSets = aggregateSets;
    }

    // ------------------------------------------------------------------------------------------------------

    @Transient
    public List<AggregateSet> getAggregateSets() {
        return FilterTools.getTransformed(aggregateSets, new Transformer<AggregateSetGroupEntry, AggregateSet>() {
            public AggregateSet transform(AggregateSetGroupEntry fromObject) {
                return fromObject.getAggregateSet();
            }
        });
    }
}
