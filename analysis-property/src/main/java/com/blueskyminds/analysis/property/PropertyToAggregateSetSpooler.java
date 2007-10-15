package com.blueskyminds.analysis.property;

import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.query.QueryFactory;
import com.blueskyminds.framework.persistence.paging.QueryPager;
import com.blueskyminds.framework.persistence.spooler.DomainObjectSpooler;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.analysis.sets.AggregateSet;
import com.blueskyminds.analysis.sets.AnalysisSets;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Calculates which AggregateSet(s) a Premise belongs to
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PropertyToAggregateSetSpooler extends DomainObjectSpooler<Premise> {

    private AnalysisSets analysisSets;

    public PropertyToAggregateSetSpooler(EntityManager entityManager, QueryPager pager, AnalysisSets analysisSets) {
        super(entityManager, pager, QueryFactory.createFindAllQuery(entityManager, Premise.class));
        this.analysisSets = analysisSets;
    }

    // ------------------------------------------------------------------------------------------------------

    protected void onStart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void onComplete() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void onError(PersistenceServiceException e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Iterates through all of the specified properties and allocates them to regions */
    protected void process(List<Premise> queryResults) throws SpoolerException {
        Set<AggregateSet> aggregateSets;
        PremiseAggregateSetMap map;

        if (queryResults != null) {
            for (Premise property : queryResults) {
                aggregateSets = mapPropertyToAggregateSets(property);
                if (aggregateSets != null) {
                    for (AggregateSet aggregateSet : aggregateSets) {
                        map = new PremiseAggregateSetMap(property, aggregateSet);
                        em.persist(map);
                        em.flush();
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    /** Determines which AggregateSets this real property belongs to */
    private Set<AggregateSet> mapPropertyToAggregateSets(Premise property) {
        Set<AggregateSet> included = new HashSet<AggregateSet>();
        for (AggregateSet aggregateSet : analysisSets.getAggregateSets()) {
            if (aggregateSet.isInSet(property)) {
                included.add(aggregateSet);
            }
        }
        return included;
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------


}
