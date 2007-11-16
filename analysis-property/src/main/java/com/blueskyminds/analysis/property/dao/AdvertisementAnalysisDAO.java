package com.blueskyminds.analysis.property.dao;

import com.blueskyminds.analysis.core.sets.AggregateSet;
import com.blueskyminds.enterprise.regionx.RegionHandle;
import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.framework.tools.filters.FilterTools;
import com.blueskyminds.landmine.core.property.AskingPrice;
import com.blueskyminds.landmine.core.property.PropertyAdvertisement;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Implements queries to access the analysis of property advertisements
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class AdvertisementAnalysisDAO extends AbstractDAO<PropertyAdvertisement> {

    private static final String QUERY_ADVERTISEMENTS_MOST_RECENT_PRICE = "analysis.advertisement.mostRecentPrice";

    public AdvertisementAnalysisDAO(EntityManager em) {
        super(em, PropertyAdvertisement.class);
    }

    /**
     * Get the set of all all advertisements in the specified region
     *
     * @return
     */
    public Set<AskingPrice> lookupMostRecentPrice(PropertyAdvertisementTypes type,
                                                  RegionHandle region,
                                                  AggregateSet aggregateSet,
                                                  Date startDate,
                                                  Date endDate) {
        Query query = em.createNamedQuery(QUERY_ADVERTISEMENTS_MOST_RECENT_PRICE);
        query.setParameter("type", type);
        query.setParameter("region", region);
        query.setParameter("aggregateSet", aggregateSet);
        query.setParameter("startDate", startDate, TemporalType.DATE);
        query.setParameter("endDate", endDate, TemporalType.DATE);
        return new HashSet<AskingPrice>(FilterTools.getNonNull(query.getResultList()));
    }
}
