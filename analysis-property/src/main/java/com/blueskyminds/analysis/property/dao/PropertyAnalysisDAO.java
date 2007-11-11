package com.blueskyminds.analysis.property.dao;

import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.EntityManager;

/**
 * Implements queries to access analysis data for Premise's
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class PropertyAnalysisDAO extends AbstractDAO<Premise> {

    public PropertyAnalysisDAO(EntityManager em) {
        super(em, Premise.class);
    }
}
