package com.blueskyminds.analysis.core.sets.dao;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;

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

    private static final String QUERY_AGGREGATE_SET_GROUP_BY_KEY = "aggegrateSetGroup.byKey";
    private static final String QUERY_AGGREGATE_SET_BY_KEY = "aggegrateSet.byKey";
    private static final String PARAM_KEY = "keyValue";

    public AggregateSetDAO(EntityManager em) {
        super(em, AggregateSet.class);
    }

    
    /**
     * Lookup an aggegrate set group by its unique key (exact match)
     *
     * @param key
     * @return
     */
    public AggregateSetGroup findAggregateSetGroup(String key) {
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

    public AggregateSet findAggregateSet(String key) {
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
