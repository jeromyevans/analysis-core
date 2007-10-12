package com.blueskyminds.analysis.property;

import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.landmine.core.property.Premise;
import com.blueskyminds.landmine.core.property.PremiseRegionMap;
import com.blueskyminds.enterprise.address.Country;
import com.blueskyminds.enterprise.address.Address;
import com.blueskyminds.enterprise.regionx.suburb.SuburbHandle;
import com.blueskyminds.enterprise.regionx.postcode.PostCodeHandle;
import com.blueskyminds.framework.persistence.*;
import com.blueskyminds.framework.persistence.query.QueryFactory;
import com.blueskyminds.framework.persistence.paging.QueryPager;
import com.blueskyminds.framework.persistence.spooler.DomainObjectSpooler;
import com.blueskyminds.framework.persistence.spooler.SpoolerException;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Calculates which Region(s) a real property is mapped to within a country
 *
 * NOTE: this should probably be extended to work with any regional hierarchy
 *
 * Date Started: 27/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class PropertyToRegionSpooler extends DomainObjectSpooler<Premise> {

    private Country country;

    public PropertyToRegionSpooler(EntityManager entityManager, QueryPager pager, Country country) {
        super(entityManager, pager, QueryFactory.createFindAllQuery(entityManager, Premise.class));
        this.country = country;
    }

    //private Set<Region> regionList;
// load the regions for this country
    //AddressDAO addressDAO = new AddressDAO();
    //regionList = addressDAO.getRegions(country);

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Iterates through all of the specified properties and allocates them to regions */
    protected void process(List<Premise> queryResults) throws SpoolerException {
        Set<RegionOLD> regions;
        PremiseRegionMap map;

        if (queryResults != null) {
            for (Premise property : (List<Premise>) queryResults) {
                regions = mapPropertyToRegions(property);
                if (regions != null) {
                    for (RegionOLD region : regions) {
                        map = new PremiseRegionMap(property, region);
                        em.persist(map);
                        em.flush();
                    }
                }
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------

    protected void onStart() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void onComplete() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void onError(PersistenceServiceException e) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    // ------------------------------------------------------------------------------------------------------

    /** Maps a property to the regions it belongs in.
     *
     * As a property always has a suburb and/or postcode, the regions it belongs to are derived upwards
     *  from these regions (one or both).
     *
     * todo: <instructions below not implemented yet - finding other regional hierarchies>
     * The method is quite slow because it tests whether the property is contained within any of the
     *  regional hierarchies defined for the country when it's found to be contained within a
     * suburb or postcode.  ie. it will naturally pickup the suburb, state and country regions from the
     * suburb, but then goes futher to identify other regional groups in the country that contain
     * the suburb and/or postcode
     *
     * The implementation relies on the Set to remove duplicates
     *
     * NOTE: This implementation uses the current address for the property
      */
    public Set<RegionOLD> mapPropertyToRegions(Premise property) {
        Address address;
        SuburbHandle suburb;
        PostCodeHandle postCode;
        Set<RegionOLD> ancestors;
        Set<RegionOLD> regions = new HashSet<RegionOLD>();

        address = property.getAddress();
        suburb = address.getSuburb();

        // todo: this needs to be enabled for the new region implementation
//        if (suburb != null) {
//            ancestors = suburb.getAncestors();
//            // add the suburb and all of its ancestors to the list
//            regions.add(suburb);
//            if (ancestors != null) {
//                regions.addAll(ancestors);
//            }
//        }
//
//        postCode = address.getPostCode();
//        if (postCode != null) {
//            ancestors = postCode.getAncestors();
//            // add the suburb and all of its ancestors to the list
//            regions.add(postCode);
//            if (ancestors != null) {
//                regions.addAll(ancestors);
//            }
//        }

        return regions;
    }

    // ------------------------------------------------------------------------------------------------------

}
