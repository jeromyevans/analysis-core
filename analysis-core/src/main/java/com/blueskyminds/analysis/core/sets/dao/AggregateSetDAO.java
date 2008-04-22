package com.blueskyminds.analysis.core.sets.dao;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

/**
 * Implements queries for accessing aggregate sets
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class AggregateSetDAO extends AbstractDAO<AggregateSet> {

    private static final String QUERY_AGGREGATE_SET_GROUP_BY_KEY = "analysis.aggegrateSetGroup.byKey";
    private static final String QUERY_AGGREGATE_SET_BY_KEY = "analysis.aggegrateSet.byKey";
    private static final String PARAM_KEY = "keyValue";

    @Inject
    public AggregateSetDAO(EntityManager em) {
        super(em, AggregateSet.class);
    }

    
    /**
     * Lookup an aggregate set group by its unique key (exact match)
     *
     * @param key
     * @return
     */
    public AggregateSetGroup lookupAggregateSetGroup(String key) {
        Collection<AggregateSetGroup> groups;
        Query query = em.createNamedQuery(QUERY_AGGREGATE_SET_GROUP_BY_KEY);
        query.setParameter(PARAM_KEY, key);        
        groups = query.getResultList();
        if (groups.size() > 0) {
            return groups.iterator().next();
        } else {
            return null;
        }
    }

    /**
     * Lookup an AggregateSet by its key
     * @param key
     * @return
     */
    public AggregateSet lookupAggregateSet(String key) {
        Collection<AggregateSet> groups;
        Query query = em.createNamedQuery(QUERY_AGGREGATE_SET_BY_KEY);
        query.setParameter(PARAM_KEY, key);
        groups = query.getResultList();
        if (groups.size() > 0) {
            return groups.iterator().next();
        } else {
            return null;
        }
    }

}
