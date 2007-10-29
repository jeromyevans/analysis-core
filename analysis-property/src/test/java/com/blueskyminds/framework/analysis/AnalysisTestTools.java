package com.blueskyminds.framework.analysis;

import com.blueskyminds.analysis.sets.*;
import com.blueskyminds.landmine.core.property.*;
import com.blueskyminds.analysis.property.PremiseAggregateSetMap;
import com.blueskyminds.enterprise.address.*;
import com.blueskyminds.enterprise.address.dao.AddressDAO;
import com.blueskyminds.framework.persistence.*;
import com.blueskyminds.framework.persistence.query.PersistenceQuery;
import com.blueskyminds.framework.measurement.Area;
import com.blueskyminds.framework.measurement.UnitsOfArea;
import com.blueskyminds.framework.datetime.PeriodTypes;
import com.blueskyminds.framework.datetime.DateTools;
import com.blueskyminds.framework.datetime.Timespan;
import com.blueskyminds.enterprise.pricing.Money;
import com.blueskyminds.framework.tools.csv.CsvOptions;
import com.blueskyminds.framework.tools.csv.CsvTextReader;
import com.blueskyminds.framework.tools.ResourceLocator;
import com.blueskyminds.framework.tools.RandomTools;
import com.blueskyminds.enterprise.region.RegionOLD;
import com.blueskyminds.enterprise.AddressTestTools;
import com.blueskyminds.enterprise.regionx.country.CountryHandle;
import com.blueskyminds.enterprise.regionx.suburb.SuburbHandle;

import java.util.*;
import java.io.IOException;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Methods helpful for testing the analyis package
 *
 * Date Started: 25/08/2006
 *
 * History:
 *
 * ---[ Blue Sky Minds Pty Ltd ]------------------------------------------------------------------------------
 */
public class AnalysisTestTools {

    private static final Log LOG = LogFactory.getLog(AnalysisTestTools.class);

    private PersistenceService persistenceService;


    public AnalysisTestTools(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public AnalysisSets createAnalysisSet() {
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

        AnalysisSets analysisSets = new AnalysisSets();

        AggregateSet detached = new UnionSet(houses, semis, villas);
        AggregateSet unitsOrApartments = new UnionSet(apartments, units);

        analysisSets.defineSet(detached);
        analysisSets.defineSet(unitsOrApartments);
        analysisSets.defineSet(vacantLand);

        analysisSets.defineSet(houses);
        analysisSets.defineSet(new IntersectionSet(houses, oneBed));
        analysisSets.defineSet(new IntersectionSet(houses, twoBed));
        analysisSets.defineSet(new IntersectionSet(houses, threeBed));
        analysisSets.defineSet(new IntersectionSet(houses, threeBed, oneBath));
        analysisSets.defineSet(new IntersectionSet(houses, threeBed, twoBath));
        analysisSets.defineSet(new IntersectionSet(houses, fourBed));
        analysisSets.defineSet(new IntersectionSet(houses, twoBed, oneBath));
        analysisSets.defineSet(new IntersectionSet(houses, twoBed, twoBath));
        analysisSets.defineSet(new IntersectionSet(houses, fiveBed));
        analysisSets.defineSet(semis);
        analysisSets.defineSet(new IntersectionSet(semis, oneBed));
        analysisSets.defineSet(new IntersectionSet(semis, twoBed));
        analysisSets.defineSet(new IntersectionSet(semis, threeBed));
        analysisSets.defineSet(new IntersectionSet(semis, fourBed));
        analysisSets.defineSet(villas);
        analysisSets.defineSet(new IntersectionSet(villas, oneBed));
        analysisSets.defineSet(new IntersectionSet(villas, twoBed));
        analysisSets.defineSet(new IntersectionSet(villas, threeBed));
        analysisSets.defineSet(new IntersectionSet(villas, fourBed));

        analysisSets.defineSet(apartments);
        analysisSets.defineSet(new IntersectionSet(apartments, studio));
        analysisSets.defineSet(new IntersectionSet(apartments, oneBed));
        analysisSets.defineSet(new IntersectionSet(apartments, twoBed));
        analysisSets.defineSet(new IntersectionSet(apartments, threeBed));
        analysisSets.defineSet(new IntersectionSet(apartments, threeBed, oneBath));
        analysisSets.defineSet(new IntersectionSet(apartments, threeBed, twoBath));
        analysisSets.defineSet(new IntersectionSet(apartments, fourBed));
        analysisSets.defineSet(new IntersectionSet(apartments, fourBed, oneBath));
        analysisSets.defineSet(new IntersectionSet(apartments, fourBed, twoBath));

        analysisSets.defineSet(units);
        analysisSets.defineSet(new IntersectionSet(units, oneBed));
        analysisSets.defineSet(new IntersectionSet(units, twoBed));
        analysisSets.defineSet(new IntersectionSet(units, threeBed));
        analysisSets.defineSet(new IntersectionSet(units, threeBed, oneBath));
        analysisSets.defineSet(new IntersectionSet(units, threeBed, twoBath));
        analysisSets.defineSet(new IntersectionSet(units, fourBed));
        analysisSets.defineSet(new IntersectionSet(units, fourBed, oneBath));
        analysisSets.defineSet(new IntersectionSet(units, fourBed, twoBath));

        return analysisSets;
    }

    // ------------------------------------------------------------------------------------------------------

    public AnalysisSets initialiseAnalysisSets() {
        AnalysisSets analysisSets = createAnalysisSet();

        try {
            PersistenceService gateway = getPersistenceService();

            gateway.save(analysisSets);
        }
        catch (PersistenceServiceException e) {
            e.printStackTrace();
        }

        return analysisSets;
    }

    // ------------------------------------------------------------------------------------------------------
// ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    // ------------------------------------------------------------------------------------------------------

    public PropertyAdvertisement generatePropertyAdvertisement() {
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

    // ------------------------------------------------------------------------------------------------------

    public void generateRandomAdvertisements(int count) {
        try {
            PersistenceService gateway = getPersistenceService();

            for (int i = 0; i < count; i++) {
                gateway.save(generatePropertyAdvertisement());
            }
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }


    private static List<Street> sampleStreets;

    /** Loads a CSV file of street names into a static variable for use in the randomStreet method */
    private static void loadSampleStreets() {
        sampleStreets = new LinkedList<Street>();

        CsvOptions csvOptions = new CsvOptions();
        csvOptions.setQuoteOutput(false);
        CsvTextReader csvReader = null;
        try {
            csvReader = new CsvTextReader(ResourceLocator.openStream("streetNames.csv"), csvOptions);

            while (csvReader.read()) {
                if (csvReader.isNonBlank()) {
                    String[] values = csvReader.getAsStrings();
                    String streetName = values[0];
                    String streetTypeName = values[1];
                    StreetType streetType = StreetType.valueOf(streetTypeName);

                    sampleStreets.add(new Street(streetName, streetType, StreetSection.NA));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }
    // ------------------------------------------------------------------------------------------------------

    /** Loads a CSV file of of sample street names, caches it, and then returns one of the streets in the sample */
    public Street randomStreet() {
        Street street;
        if (sampleStreets == null) {
            loadSampleStreets();
        }
        return sampleStreets.get(RandomTools.randomInt(0, sampleStreets.size()-1));
    }

    private static boolean suburbsLoaded;

    public void loadSampleSuburbs() {
        AddressTestTools.initialiseCountryList();
        AddressTestTools.initialiseAustralianStates();
        AddressTestTools.initialiseAustralianSuburbs();
        suburbsLoaded = true;
    }

    public SuburbHandle randomSuburb() throws PersistenceServiceException {
        if (!suburbsLoaded) {
            loadSampleSuburbs();
        }
        PersistenceService gateway = getPersistenceService();

        return gateway.random(SuburbHandle.class);
    }

    // ------------------------------------------------------------------------------------------------------
    // ------------------------------------------------------------------------------------------------------

    /** Creates a property for a random address.
     * The only attributes of the property are that it has a type, and a certain number of bedrooms and bathrooms
     * The property has not been persisted.
     *
     * Set Suburb to null to use a random suburb
     *  */
    public Premise generateRandomProperty(SuburbHandle suburb) {

        Premise property = null;
        Address address = null;
        PersistenceService gateway = getPersistenceService();
        PersistenceSession session;

        // First, choose a type (exclude block of units and unknown types)
        PropertyTypes type = RandomTools.randomEnum(PropertyTypes.class, PropertyTypes.BlockOfUnits, PropertyTypes.Unknown);

        try {
            //session = gateway.openSession();

            // create a random address
            Street street = randomStreet();
            String streetNumber = Integer.toString(RandomTools.randomInt(1, 300));
            if (suburb == null) {
                suburb = randomSuburb();
            }


            // todo: broken - repair for new region implementation
//            if (PropertyType.isTypeOfUnit(type)) {
//                String unitNumber = Integer.toString(RandomTools.randomInt(1, 20));
//                address = new UnitAddress(unitNumber, streetNumber, street, suburb, suburb.getPostCode());
//            } else {
//                address = new StreetAddress(streetNumber, street, suburb, suburb.getPostCode());
//            }

            property = new Premise();
            Date dateApplied = RandomTools.randomDate(1990, 2005);
            property.associateAddress(address, dateApplied);
            PremiseAttributeSet attributes = new PremiseAttributeSet(dateApplied);
            attributes.setPropertyType(type);
            attributes.setBedrooms(RandomTools.randomInt(1,5));
            attributes.setBathrooms(RandomTools.randomInt(1,2));
            property.associateFeatures(attributes);
            //session.close();
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        }

        return property;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Generate a random advertisement for the specified property
     * @param type - type of advertisement, or null for random */
    public PropertyAdvertisement generatePropertyAdvertisement(Premise premise, PropertyAdvertisementTypes type, int firstYear, int lastYear) {
        if (type == null) {
            type = RandomTools.randomEnum(PropertyAdvertisementTypes.class);
        }
        PropertyAdvertisement advertisement = new PropertyAdvertisement(type);

        Date dateListed = RandomTools.randomDate(firstYear, lastYear);
        advertisement.setAddress(premise.getAddress());
        advertisement.setDateListed(dateListed);
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

        PremiseAttributeSet attributeSet = new PremiseAttributeSet(dateListed);
        attributeSet.setBedrooms(premise.getBedrooms());
        attributeSet.setBathrooms(premise.getBathrooms());
        attributeSet.setPropertyType(premise.getType());
        attributeSet.setBuildingArea(new Area(RandomTools.randomDouble(75, 250), UnitsOfArea.SquareMetre));
        advertisement.setAttributes(attributeSet);

        return advertisement;
    }

    // ------------------------------------------------------------------------------------------------------        
    // ------------------------------------------------------------------------------------------------------

    /** Create a bunch of properties in persistence */
    public void generateRandomProperties(int count) {
        try {
            PersistenceService gateway = getPersistenceService();

            for (int i = 0; i < count; i++) {
                gateway.save(generateRandomProperty(null));
            }
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
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
                p.print();
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
                p.print();
            }
            session.close();
        } catch(PersistenceServiceException e) {
            e.printStackTrace();
        }
    }

    public void generateRandomPropertiesWithAds(SuburbHandle region, PropertyAdvertisementTypes type, int count, int firstYear, int lastYear) {

        Premise premise;
        PropertyAdvertisement advertisement;
        PropertyAdvertisement secondAdvertisement;
        AskingPrice price;
        int properties = 0;
        int withAds = 0;
        int withTwoAds = 0;
        PersistenceSession ps = null;
        try {
            PersistenceService gateway = getPersistenceService();
            ps = gateway.openSession();

            properties = 0;
            while (properties < count) {
               // PersistenceTransaction transaction = ps.currentTransaction();

                premise = generateRandomProperty(region);

                //gateway.save(premise);
                if (RandomTools.randomInt(0, 9) == 0) {
                    // one in ten properties don't get an advertisement
                } else {
                    advertisement = generatePropertyAdvertisement(premise, type, firstYear, lastYear);
                    premise.associateFeatures(advertisement.getAttributes());
                    advertisement.associateWithPremise(premise);
                    gateway.save(advertisement);
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
                        gateway.save(secondAdvertisement);
                        //gateway.save(premise);
                        withTwoAds++;
                    }
                }
                gateway.save(premise);
                properties++;
              //  transaction.commit();
            }

            LOG.info("Created "+properties+" properties, "+withAds+" with ads, "+withTwoAds+" with two ads");
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch(PersistenceServiceException e) {
                    //
                }
            }
        }
    }

    public void generateRandomPropertiesWithAds(int count, int firstYear, int lastYear) {
        generateRandomPropertiesWithAds(null, null, count, firstYear, lastYear);
    }

    public void mapPropertiesToRegions() {
        CountryHandle australia = null;
        australia = new AddressDAO(getPersistenceService()).findCountry("AUS");
// todo: enable
        //PropertyToRegionSpooler propertyToRegionSpooler = new PropertyToRegionSpooler(persistenceService, australia);
        //propertyToRegionSpooler.start();
    }

    public void mapPropertiesToAggregateSets() {
        AnalysisSets analysisSets = initialiseAnalysisSets();
        CountryHandle australia = null;
        australia = new AddressDAO(getPersistenceService()).findCountry("AUS");
        // todo: enable
        //PropertyToAggregateSetSpooler propertyToAggregateSetSpooler = new PropertyToAggregateSetSpooler(persistenceService, analysisSets);
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

    /** Create advertisements of the specified type for all properties */
    public void generateRandomAdsForProperties(PropertyAdvertisementTypes type, int firstYear, int lastYear) {

        PropertyAdvertisement advertisement;
        PropertyAdvertisement secondAdvertisement;
        AskingPrice price;
        int properties = 0;
        int withAds = 0;
        int withTwoAds = 0;
        PersistenceSession ps = null;
        try {
            PersistenceService gateway = getPersistenceService();
            ps = gateway.openSession();

            List<Premise> propertyList = gateway.findAll(Premise.class);
            properties = 0;
            for (Premise premise : propertyList) {

                if (RandomTools.randomInt(0, 9) == 0) {
                    // one in ten properties don't get an advertisement
                } else {
                    advertisement = generatePropertyAdvertisement(premise, type, firstYear, lastYear);
                    premise.associateFeatures(advertisement.getAttributes());
                    advertisement.associateWithPremise(premise);
                    gateway.save(advertisement);
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
                        gateway.save(secondAdvertisement);
                        //gateway.save(premise);
                        withTwoAds++;
                    }
                }
                gateway.save(premise);
                properties++;
              //  transaction.commit();
            }

            LOG.info("Updated "+properties+" properties, "+withAds+" with ads, "+withTwoAds+" with two ads");
        } catch (PersistenceServiceException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch(PersistenceServiceException e) {
                    //
                }
            }
        }
    }


    public PersistenceService getPersistenceService() {
        return persistenceService;
    }
}
