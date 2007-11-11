package com.blueskyminds.framework.analysis;

import com.blueskyminds.analysis.core.sets.*;
import com.blueskyminds.analysis.property.PremiseAggregateSetMap;
import com.blueskyminds.enterprise.address.PlainTextAddress;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.enterprise.regionx.country.CountryHandle;
import com.blueskyminds.enterprise.regionx.suburb.SuburbHandle;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.datetime.Timespan;
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
public class AnalysisTestTools {

    private static final Log LOG = LogFactory.getLog(AnalysisTestTools.class);

    private EntityManager em;

    public AnalysisTestTools(EntityManager em) {
        this.em = em;
    }

    public AggregateSetGroup createAnalysisGroup() {
        AggregateSet vacantLand = new SimpleAggregateSet("type", PropertyTypes.Land);
        AggregateSet houses = new SimpleAggregateSet("type", PropertyTypes.House);
        AggregateSet semis = new SimpleAggregateSet("type", PropertyTypes.Semi);
        AggregateSet apartments = new SimpleAggregateSet("type", PropertyTypes.Apartment);
        AggregateSet units = new SimpleAggregateSet("type", PropertyTypes.Unit);
        AggregateSet villas = new SimpleAggregateSet("type", PropertyTypes.Villa);

        AggregateSet studio = new SimpleAggregateSet("bedrooms", 0);
        AggregateSet oneBed = new SimpleAggregateSet("bedrooms", 1);
        AggregateSet twoBed = new SimpleAggregateSet("bedrooms", 2);
        AggregateSet threeBed = new SimpleAggregateSet("bedrooms", 3);
        AggregateSet fourBed = new SimpleAggregateSet("bedrooms", 4);
        AggregateSet fiveBed = new SimpleAggregateSet("bedrooms", 5);

        AggregateSet oneBath = new SimpleAggregateSet("bathrooms", 1);
        AggregateSet twoBath = new SimpleAggregateSet("bathrooms", 2);

        AggregateSetGroup aggregateSetGroup = new AggregateSetGroup();

        AggregateSet detached = new UnionSet(houses, semis, villas);
        AggregateSet unitsOrApartments = new UnionSet(apartments, units);

        aggregateSetGroup.includeSet(detached);
        aggregateSetGroup.includeSet(unitsOrApartments);
        aggregateSetGroup.includeSet(vacantLand);

        aggregateSetGroup.includeSet(houses);
        aggregateSetGroup.includeSet(new IntersectionSet(houses, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, threeBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, threeBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, fourBed));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, twoBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, twoBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet(houses, fiveBed));
        aggregateSetGroup.includeSet(semis);
        aggregateSetGroup.includeSet(new IntersectionSet(semis, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet(semis, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet(semis, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet(semis, fourBed));
        aggregateSetGroup.includeSet(villas);
        aggregateSetGroup.includeSet(new IntersectionSet(villas, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet(villas, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet(villas, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet(villas, fourBed));

        aggregateSetGroup.includeSet(apartments);
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, studio));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, threeBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, threeBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, fourBed));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, fourBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet(apartments, fourBed, twoBath));

        aggregateSetGroup.includeSet(units);
        aggregateSetGroup.includeSet(new IntersectionSet(units, oneBed));
        aggregateSetGroup.includeSet(new IntersectionSet(units, twoBed));
        aggregateSetGroup.includeSet(new IntersectionSet(units, threeBed));
        aggregateSetGroup.includeSet(new IntersectionSet(units, threeBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet(units, threeBed, twoBath));
        aggregateSetGroup.includeSet(new IntersectionSet(units, fourBed));
        aggregateSetGroup.includeSet(new IntersectionSet(units, fourBed, oneBath));
        aggregateSetGroup.includeSet(new IntersectionSet(units, fourBed, twoBath));

        return aggregateSetGroup;
    }

    // ------------------------------------------------------------------------------------------------------

    /**
     * Setup default analysis gret groups
     * @return
     */
    public AggregateSetGroup initialiseAnalysisGroups() {
        AggregateSetGroup analysisGroup = createAnalysisGroup();

        em.persist(analysisGroup);
        em.flush();

        return analysisGroup;
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
    public PersistenceQuery searchPropertiesInRegion(RegionOLD region) {
        DetachedCriteria criteria = DetachedCriteria.forClass(PremiseRegionMap.class);
        criteria.add(Expression.eq("region", region));
      //  return new HibernateCriteriaImpl(criteria);
        return null;
    }
    @Deprecated
    public PersistenceQuery searchRegionByName(String name) {
        DetachedCriteria criteria = DetachedCriteria.forClass(RegionOLD.class);
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

    public List<Premise> findPropertiesInRegion(RegionOLD region) {
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

    public RegionOLD findRegionByName(String name) {
        RegionOLD region= null;
        try {

            PersistenceService gateway = getPersistenceService();

            region = gateway.findOne(RegionOLD.class, searchRegionByName(name));

        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
        return region;
    }

    public void printPropertiesInRegion(RegionOLD region) {
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
                    secondAdvertisement.setDateListed(DateTools.addTimespan(advertisement.getDateListed(), new Timespan(1, PeriodTypes.Fortnight)));
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

    public void mapPropertiesToRegions() {
        CountryHandle australia = null;
        australia = new AddressDAO(getPersistenceService()).findCountry("AUS");
// todo: enable
        //PropertyToRegionSpooler propertyToRegionSpooler = new PropertyToRegionSpooler(persistenceService, australia);
        //propertyToRegionSpooler.start();
    }

    public void mapPropertiesToAggregateSets() {
        AggregateSetGroup analysisSets = initialiseAnalysisGroups();
        CountryHandle australia = null;
        australia = new AddressDAO(getPersistenceService()).findCountry("AUS");
        // todo: enable
        //PremiseAggregateSetSpoolerTask propertyToAggregateSetSpooler = new PremiseAggregateSetSpoolerTask(persistenceService, analysisSets);
        //propertyToAggregateSetSpooler.start();
    }

    public List<RegionOLD> findAllRegions() {
        List<RegionOLD> regions = null;
        PersistenceService gateway = getPersistenceService();
         try {
            regions = gateway.findAll(RegionOLD.class);
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
