package com.blueskyminds.analysis.core.sets.dao;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;

import javax.persistence.EntityManager;

/**
 * Implements queries for accessing aggregate sets
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class AggregateSetDAO extends AbstractDAO<AggregateSet> {

    public AggregateSetDAO(EntityManager em) {
        super(em, AggregateSet.class);
    }
}
