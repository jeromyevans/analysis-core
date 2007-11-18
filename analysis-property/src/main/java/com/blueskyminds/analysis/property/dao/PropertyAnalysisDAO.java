package com.blueskyminds.analysis.property.dao;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.landmine.core.property.Premise;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * Implements queries to access analysis data for Premise's
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class PropertyAnalysisDAO extends AbstractDAO<Premise> {

    private static final String QUERY_ANALYSIS_DATA_SOURCE_BY_KEY = "analysisDataSource.byKey";
    private static final String PARAM_KEY = "keyValue";

    public PropertyAnalysisDAO(EntityManager em) {
        super(em, Premise.class);
    }

    /**
     * Lookup an analysis datasource by its unique key
     * 
     * @param key
     * @return
     */
    public AnalysisDataSource lookupAnalysisDataSource(String key) {
        Query query = em.createNamedQuery(QUERY_ANALYSIS_DATA_SOURCE_BY_KEY);
        query.setParameter(PARAM_KEY, key);

        List<AnalysisDataSource> results = query.getResultList();
        if (results.size() > 0) {
            return results.iterator().next();
        } else {
            return null;
        }
    }
}
