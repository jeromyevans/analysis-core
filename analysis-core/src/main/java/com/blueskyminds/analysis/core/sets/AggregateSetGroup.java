package com.blueskyminds.analysis.core.sets;

import com.blueskyminds.homebyfive.framework.core.AbstractEntity;
import com.blueskyminds.homebyfive.framework.core.tools.filters.FilterTools;
import com.blueskyminds.homebyfive.framework.core.transformer.Transformer;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private String key;
    private Set<AggregateSetGroupEntry> aggregateSets;

    public AggregateSetGroup(String key) {
        this.key = key;
        init();
    }

    /** Default constructor for ORM */
    protected AggregateSetGroup() {
        init();
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Initialise the AnalysisSets with default attributes
     */
    private void init() {
        aggregateSets = new HashSet<AggregateSetGroupEntry>();
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a new Set */
    public void includeSet(AggregateSet series) {
        aggregateSets.add(new AggregateSetGroupEntry(this, series));
    }

    /** Unique name of this group */
    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /** Get the list of aggregate sets in this collection */
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "group")
    protected Set<AggregateSetGroupEntry> getAggregateSetGroupEntries() {
        return aggregateSets;
    }

    protected void setAggregateSetGroupEntries(Set<AggregateSetGroupEntry> aggregateSets) {
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
