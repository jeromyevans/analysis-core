package com.blueskyminds.analysis.property.classification;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;
import com.blueskyminds.framework.persistence.spooler.SpoolerTask;
import com.blueskyminds.landmine.core.property.Premise;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Calculates which AggregateSet(s) a Premise belongs to
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PremiseAggregateSetSpoolerTask implements SpoolerTask<Premise> {

    private static final Log LOG = LogFactory.getLog(PremiseAggregateSetSpoolerTask.class);

    private AggregateSetGroup analysisSets;
    private EntityManager em;

    public PremiseAggregateSetSpoolerTask(EntityManager em, AggregateSetGroup analysisSets) {
        this.analysisSets = analysisSets;
        this.em = em;
    }

//    public PremiseAggregateSetSpoolerTask(EntityManager entityManager, QueryPager pager, AggregateSetGroup analysisSets) {
//        super(entityManager, pager, QueryFactory.createFindAllQuery(entityManager, Premise.class));
//        this.analysisSets = analysisSets;
//    }

    /**
     * Iterates through all of the specified premises and allocates them to aggregate sets
     **/
    public void process(List<Premise> queryResults) throws SpoolerException {
        Set<AggregateSet> aggregateSets;
        PremiseAggregateSetMap map;

        if (queryResults != null) {
            for (Premise property : queryResults) {
                aggregateSets = mapPropertyToAggregateSets(property);
                if (aggregateSets != null) {
                    for (AggregateSet aggregateSet : aggregateSets) {
                        map = new PremiseAggregateSetMap(property, aggregateSet);
                        em.persist(map);
                    }
                }
            }
            LOG.info(queryResults.size());
            em.flush();
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
