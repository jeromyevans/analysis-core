package com.blueskyminds.analysis.property.classification;

import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.region.service.RegionService;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.postcode.PostCodeHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
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
 * A SpoolerTask that creates denormalized mappings between a premise and the regions that contain it.
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PremiseRegionSpoolerTask implements SpoolerTask<Premise> {

    private static final Log LOG = LogFactory.getLog(PremiseRegionSpoolerTask.class);
    
    private RegionService regionService;
    private EntityManager em;

    public PremiseRegionSpoolerTask(EntityManager em, RegionService regionService) {
        this.em = em;
        this.regionService = regionService;
    }

    /** Iterates through all of the specified properties and allocates them to regions */
    public void process(List<Premise> queryResults) throws SpoolerException {
        Set<RegionHandle> regions;
        PremiseRegionMap map;

        if (queryResults != null) {
            for (Premise property : queryResults) {
                regions = mapPropertyToRegions(property);
                if (regions != null) {
                    for (RegionHandle region : regions) {
                        map = new PremiseRegionMap(property, region);
                        em.persist(map);
                        LOG.info(map);
                    }
                }
            }
            em.flush();
        }
    }

    /**
     * Creates denormalized mappings between a premise and the regions it belongs in.
     *
     * As a property always has a suburb and/or postcode, the regions it belongs to are derived upwards
     *  from these regions (one or both).
     *
     * NOTE: This implementation uses the current address for the property
      */
    public Set<RegionHandle> mapPropertyToRegions(Premise property) {
        Address address;
        SuburbHandle suburb;
        PostCodeHandle postCode;
        Set<RegionHandle> ancestors = new HashSet<RegionHandle>();

        address = property.getAddress();
        suburb = address.getSuburb();
        postCode = address.getPostCode();

        if (suburb != null) {
            ancestors.add(suburb);
            ancestors.addAll(regionService.listAncestors(suburb));
        } else {
            if (postCode != null) {
                ancestors.add(postCode);
                ancestors.addAll(regionService.listAncestors(postCode));
            }
        }

        return ancestors;
    }

    // ------------------------------------------------------------------------------------------------------

}
