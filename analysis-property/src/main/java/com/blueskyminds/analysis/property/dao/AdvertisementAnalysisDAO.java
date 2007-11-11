package com.blueskyminds.analysis.property.dao;

import com.blueskyminds.framework.persistence.jpa.dao.AbstractDAO;
import com.blueskyminds.landmine.core.property.PropertyAdvertisement;

import javax.persistence.EntityManager;

/**
 * Implements queries to access the analysis of property advertisements
 *
 * Date Started: 11/11/2007
 * <p/>
 * History:
 */
public class AdvertisementAnalysisDAO extends AbstractDAO<PropertyAdvertisement> {

    public AdvertisementAnalysisDAO(EntityManager em) {
        super(em, PropertyAdvertisement.class);
    }
}
