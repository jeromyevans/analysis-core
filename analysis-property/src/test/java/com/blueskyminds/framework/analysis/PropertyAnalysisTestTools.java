package com.blueskyminds.framework.analysis;

import com.blueskyminds.analysis.AdvertisementAnalysisServiceImpl;
import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.analysis.core.sets.*;
import com.blueskyminds.analysis.property.classification.PremiseAggregateSetMap;
import com.blueskyminds.analysis.property.classification.PremiseRegionMap;
import com.blueskyminds.analysis.property.pricestatistics.AskingPriceDataSource;
import com.blueskyminds.enterprise.address.PlainTextAddress;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.region.RegionHandle;
import com.blueskyminds.enterprise.region.suburb.SuburbHandle;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.datetime.Interval;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;
import com.blueskyminds.framework.persistence.PersistenceService;
import com.blueskyminds.framework.persistence.PersistenceServiceException;
import com.blueskyminds.framework.persistence.PersistenceSession;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;
import com.blueskyminds.framework.tools.RandomTools;
import com.blueskyminds.landmine.core.property.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;

import javax.persistence.EntityManager;
import java.util.*;

/**
 * Methods helpful for testing the property analyis module
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
public class PropertyAnalysisTestTools {

    private static final Log LOG = LogFactory.getLog(PropertyAnalysisTestTools.class);

    private EntityManager em;
    public static final String GROUP_NAME = "Property";

    public PropertyAnalysisTestTools(EntityManager em) {
        this.em = em;
    }

    public static AggregateSetGroup createAnalysisGroup() {
        AggregateSet vacantLand = new PropertyValueSet("land", "type", PropertyTypes.Land);
        AggregateSet houses = new PropertyValueSet("houses", "type", PropertyTypes.House);
        AggregateSet semis = new PropertyValueSet("semis", "type", PropertyTypes.Semi);
        AggregateSet apartments = new PropertyValueSet("apartments", "type", PropertyTypes.Apartment);
        AggregateSet units = new PropertyValueSet("units", "type", PropertyTypes.Unit);
        AggregateSet villas = new PropertyValueSet("villas", "type", PropertyTypes.Villa);

        AggregateSet studio = new PropertyValueSet("Studio", "bedrooms", 0);
        AggregateSet oneBed = new PropertyValueSet("1bed", "bedrooms", 1);
        AggregateSet twoBed = new PropertyValueSet("2bed", "bedrooms", 2);
        AggregateSet threeBed = new PropertyValueSet("3bed", "bedrooms", 3);
        AggregateSet fourBed = new PropertyValueSet("4bed", "bedrooms", 4);
        AggregateSet fiveBed = new PropertyValueSet("5bed", "bedrooms", 5);

        AggregateSet oneBath = new PropertyValueSet("1bath", "bathrooms", 1);
        AggregateSet twoBath = new PropertyValueSet("2bath", "bathrooms", 2);

        AggregateSetGroup aggregateSetGroup = new AggregateSetGroup(GROUP_NAME);

        AggregateSet detached = new UnionSet("detached", houses, semis, villas);
        AggregateSet unitsOrApartments = new UnionSet("unitsOrApartments", apartments, units);

        aggregateSetGroup.includeSet(detached);
        aggregateSetGroup.includeSet(unitsOrApartments);
        aggregateSetGroup.includeSet(vacantLand);

        aggregateSetGroup.includeSet(houses);
        aggregateSetGroup.includeSet(new IntersectionSet("houses1Bed", houses, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet("houses2Bed", houses, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet("houses3Bed", houses, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet("houses3Bed1Bath", houses, threeBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet("houses3Bed2Bath", houses, threeBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet("houses4Bed", houses, fourBed));
        aggregateSetGroup.includeSet(new IntersectionSet("houses4Bed1Bath", houses, fourBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet("houses4Bed2Bath", houses, fourBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet("houses5Bed", houses, fiveBed));
        aggregateSetGroup.includeSet(semis);
        aggregateSetGroup.includeSet(new IntersectionSet("semis1Bed", semis, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet("semis2Bed", semis, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet("semis3Bed", semis, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet("semis4Bed", semis, fourBed));
        aggregateSetGroup.includeSet(villas);
        aggregateSetGroup.includeSet(new IntersectionSet("villas1Bed", villas, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet("villas2Bed", villas, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet("villas3Bed", villas, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet("villas4Bed", villas, fourBed));

        aggregateSetGroup.includeSet(apartments);
        aggregateSetGroup.includeSet(new IntersectionSet("apartmentsStudio", apartments, studio));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments1Bed", apartments, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments2Bed", apartments, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments3Bed", apartments, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments3Bed1Bath", apartments, threeBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments3Bed2Bath", apartments, threeBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments4Bed", apartments, fourBed));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments4Bed1Bath", apartments, fourBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet("apartments4Bed2Bath", apartments, fourBed, twoBath));

        aggregateSetGroup.includeSet(units);
        aggregateSetGroup.includeSet(new IntersectionSet("units1Bed", units, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet("units2Bed", units, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet("units3Bed", units, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet("units3Bed1Bath", units, threeBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet("units3Bed2Bath", units, threeBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet("units4Bed", units, fourBed));
        aggregateSetGroup.includeSet(new IntersectionSet("units4Bed1Bath", units, fourBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet("units4Bed2Bath", units, fourBed, twoBath));

        return aggregateSetGroup;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Setup default analysis gret groups
     * @return
     */
    public static  AggregateSetGroup initialiseAggregateSetGroups(EntityManager em) {
        AggregateSetGroup analysisGroup = createAnalysisGroup();

        em.persist(analysisGroup);
        em.flush();

        return analysisGroup;
    }

    /**
     * Setup the default analysis data sources
     *
     * @param em
     */
    public static void initialiseAnalysisDataSources(EntityManager em) {
        AnalysisDataSource privateTreaty = new AskingPriceDataSource(AdvertisementAnalysisServiceImpl.ASKING_PRICE_PRIVATE_TREATY_DATASOURCE, "Advertised Prices : Private Treaty", PropertyAdvertisementTypes.PrivateTreaty);
        AnalysisDataSource lease = new AskingPriceDataSource(AdvertisementAnalysisServiceImpl.ASKING_PRICE_LEASE_DATASOURCE, "Advertised Prices : Lease", PropertyAdvertisementTypes.Lease);

        em.persist(privateTreaty);
        em.persist(lease);
        em.flush();
    }

    /**
     * Generates a random property advertisement
     * @return
     */
    public PropertyAdvertisement createRandomPropertyAdvertisement() {
        PropertyAdvertisementTypes type = RandomTools.randomEnum(PropertyAdvertisementTypes.class);
        PropertyAdvertisement advertisement = new PropertyAdvertisement(type);

        advertisement.setAddress(new PlainTextAddress("1/22 Random Street, Neutral Bay NSW 2089"));
        advertisement.setDateListed(new Date());
        advertisement.setDescription("Sample description");

        switch (type) {
            case Auction:
                break;
            case Lease:
                advertisement.setPrice(new AskingPrice(new Money(RandomTools.randomDouble(60, 1000), Currency.getInstance("AUD")), PeriodTypes.Week));
                break;
            case PrivateTreaty:
                advertisement.setPrice(new AskingPrice(new Money(RandomTools.randomDouble(50000, 1000000), Currency.getInstance("AUD")), PeriodTypes.OnceOff));
                break;
            case Unknown:
                break;
        }

        PremiseAttributeSet attributeSet = new PremiseAttributeSet(RandomTools.randomDate(1900, 2005));
        attributeSet.setBedrooms(RandomTools.randomInt(0, 5));
        attributeSet.setBathrooms(RandomTools.randomInt(1, 3));
        attributeSet.setPropertyType(RandomTools.randomEnum(PropertyTypes.class));
        attributeSet.setBuildingArea(new Area(RandomTools.randomDouble(0, 250), UnitsOfArea.SquareMetre));
        advertisement.setAttributes(attributeSet);

        return advertisement;
    }

    /**
     * Generates the specified number of property advertisements and persists them
     *
     * @param count
     */
    public void initialiseRandomAdvertisements(int count) {
        for (int i = 0; i < count; i++) {
            em.persist(createRandomPropertyAdvertisement());
        }
        em.flush();
    }

    /** Presist a random bunch of properties */
    public void initialiseRandomProperties(int count) {
        for (int i = 0; i < count; i++) {
            em.persist(PremiseTestTools.createRandomPremise(null, em));
        }
        em.flush();
    }

    /** Creates a QueryBuilder that searches for a ... */
    @Deprecated
    public PersistenceQuery searchPropertiesInRegion(RegionHandle region) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PremiseRegionMap.class);
        criteria.add(Expression.eq("region", region));
      //  return new HibernateCriteriaImpl(criteria);
        return null;
    }
    @Deprecated
    public PersistenceQuery searchRegionByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(RegionHandle.class);
        criteria.add(Expression.eq("name", name));
        //return new HibernateCriteriaImpl(criteria);
        return null;
    }

    @Deprecated
    public PersistenceQuery searchPropertiesInAggegateSet(AggregateSet aggregateSet) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PremiseAggregateSetMap.class);
        criteria.add(Expression.eq("aggregateSet", aggregateSet));
        //return new HibernateCriteriaImpl(criteria);
        return null;
    }

    public List<Premise> findPropertiesInRegion(RegionHandle region) {
        List<Premise> properties = new LinkedList<Premise>();
        PersistenceService gateway = getPersistenceService();
        PersistenceSession session = null;

        try {
            //session = gateway.openSession();
            Collection<PremiseRegionMap> maps = gateway.find(PremiseRegionMap.class, searchPropertiesInRegion(region));

            for (PremiseRegionMap map : maps) {
                properties.add(map.getPremise());
            }

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        } finally  {
//            if (session != null) {
//                try {
//                    session.close();
//                } catch(PersistenceServiceException e) {
//                    //
//                }
//            }
        }
        return properties;
    }

    public List<Premise> findPropertiesInAggregateSet(AggregateSet aggregateSet) {
        List<Premise> properties = new LinkedList<Premise>();
        PersistenceService gateway = getPersistenceService();
        PersistenceSession session = null;

        try {
            //session = gateway.openSession();
            Collection<PremiseAggregateSetMap> maps = gateway.find(PremiseAggregateSetMap.class, searchPropertiesInAggegateSet(aggregateSet));

            for (PremiseAggregateSetMap map : maps) {
                properties.add(map.getPremise());
            }

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        } finally  {
//            if (session != null) {
//                try {
//                    session.close();
//                } catch(PersistenceServiceException e) {
//                    //
//                }
//            }
        }
        return properties;
    }

    public RegionHandle findRegionByName(String name) {
        RegionHandle region= null;
        try {

            PersistenceService gateway = getPersistenceService();

            region = gateway.findOne(RegionHandle.class, searchRegionByName(name));

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
        return region;
    }

    public void printPropertiesInRegion(RegionHandle region) {
        try {
            LOG.info("--- Listing properties in region ---");
            PersistenceSession session = getPersistenceService().openSession();
            List<Premise> properties = findPropertiesInRegion(region);
            LOG.info("Properties:"+properties.size());
            for (Premise p : properties) {
                p.print(System.out);
            }
            session.close();
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    public void printPropertiesInAggregateSet(AggregateSet aggregateSet) {
        try {
            LOG.info("--- Listing properties in AggregateSet ---");
            PersistenceSession session = getPersistenceService().openSession();
            List<Premise> properties = findPropertiesInAggregateSet(aggregateSet);
            LOG.info("Properties:"+properties.size());
            for (Premise p : properties) {
                p.print(System.out);
            }
            session.close();
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    /**
     * Persist a bunch of premises with advertisements
     *
     * @param region
     * @param type
     * @param count
     * @param firstYear
     * @param lastYear
     */
    public void initialiseRandomPropertiesWithAds(SuburbHandle region, PropertyAdvertisementTypes type, int count, int firstYear, int lastYear) {

        Premise premise;
        PropertyAdvertisement advertisement;
        PropertyAdvertisement secondAdvertisement;
        AskingPrice price;
        int properties = 0;
        int withAds = 0;
        int withTwoAds = 0;

        properties = 0;
        while (properties < count) {
           // PersistenceTransaction transaction = ps.currentTransaction();

            premise = PremiseTestTools.createRandomPremise(region, em);

            //gateway.save(premise);
            if (RandomTools.randomInt(0, 9) == 0) {
                // one in ten properties don't get an advertisement
            } else {
                advertisement = PremiseTestTools.createRandomPropertyAdvertisement(premise, type, firstYear, lastYear);
                premise.associateFeatures(advertisement.getAttributes());
                advertisement.associateWithPremise(premise);
                em.persist(advertisement);
                withAds++;
                if (RandomTools.randomInt(0, 4) == 0) {
                    // one in five have a second advertisement

                    // create another advertisement for this property at a lower price and slightly in the future
                    secondAdvertisement = advertisement.duplicate();
                    // override values
                    secondAdvertisement.setDateListed(DateTools.addTimespan(advertisement.getDateListed(), new Interval(1, PeriodTypes.Fortnight)));
                    price = advertisement.getPrice();
                    if (price != null) {
                        secondAdvertisement.setPrice(price.adjustedPrice(1.1));
                    } else {
                        secondAdvertisement.setPrice(null);
                    }

                    premise.associateFeatures(secondAdvertisement.getAttributes());
                    secondAdvertisement.associateWithPremise(premise);
                    em.persist(secondAdvertisement);
                    //gateway.save(premise);
                    withTwoAds++;
                }
            }
            em.persist(premise);
            properties++;
        }

        LOG.info("Created "+properties+" properties, "+withAds+" with ads, "+withTwoAds+" with two ads");
    }

    /**
     * Initialise a bunch of premises with advertisements
     *
     * @param count
     * @param firstYear
     * @param lastYear
     */
    public void initialiseRandomPropertiesWithAds(int count, int firstYear, int lastYear) {
        initialiseRandomPropertiesWithAds(null, null, count, firstYear, lastYear);
    }

    public List<RegionHandle> findAllRegions() {
        List<RegionHandle> regions = null;
        PersistenceService gateway = getPersistenceService();
         try {
            regions = gateway.findAll(RegionHandle.class);
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
        return regions;
    }

    public List<AggregateSet> findAllAggregateSets() {
        List<AggregateSet> aggregateSets = null;
        PersistenceService gateway = getPersistenceService();
         try {
            aggregateSets = gateway.findAll(AggregateSet.class);
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
        return aggregateSets;
    }

    @Deprecated
    private PersistenceService getPersistenceService() {
        return null;
    }
}
