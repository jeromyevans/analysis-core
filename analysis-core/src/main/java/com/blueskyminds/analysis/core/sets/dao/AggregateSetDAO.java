package com.blueskyminds.analysis.core.sets.dao;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.analysis.core.sets.AggregateSetGroup;
import com.blueskyminds.homebyfive.framework.core.persistence.jpa.dao.AbstractDAO;
import com.google.inject.Inject;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Collection;

/**
 * Implements queries for accessing aggregate sets
 *
 * Date Started: 11/11/2007
 * <p/>
 * Copyright (c) 2009 Blue Sky Minds Pty Ltd
 */
public class AggregateSetDAO extends AbstractDAO<AggregateSet> {

    private static final String QUERY_AGGREGATE_SET_GROUP_BY_KEY = "analysis.aggegrateSetGroup.byKey";
    private static final String QUERY_AGGREGATE_SET_BY_KEY = "analysis.aggegrateSet.byKey";
    private static final String QUERY_AGGREGATE_SET_BY_GROUP_AND_KEY = "analysis.aggegrateSet.byGroupAndKey";
    private static final String PARAM_KEY = "keyValue";
    private static final String PARAM_GROUP_KEY = "groupKey";

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
        Query query = em.createNamedQuery(QUERY_AGGREGATE_SET_GROUP_BY_KEY);
        query.setParameter(PARAM_KEY, key);
        return (AggregateSetGroup) firstInX(query.getResultList());        
    }

    /**
     * Lookup an AggregateSet by its unique key
     * @param key
     * @return
     */
    public AggregateSet lookupAggregateSet(String key) {
        Query query = em.createNamedQuery(QUERY_AGGREGATE_SET_BY_KEY);
        query.setParameter(PARAM_KEY, key);
        return firstIn(query.getResultList());
    }

    /**
     * Lookup an AggregateSet by its key in a group
     * @param groupKey
     *@param key @return
     */
    public AggregateSet lookupAggregateSet(String groupKey, String key) {
        Query query = em.createNamedQuery(QUERY_AGGREGATE_SET_BY_GROUP_AND_KEY);
        query.setParameter(PARAM_GROUP_KEY, groupKey);
        query.setParameter(PARAM_KEY, key);
        return firstIn(query.getResultList());
    }

}
