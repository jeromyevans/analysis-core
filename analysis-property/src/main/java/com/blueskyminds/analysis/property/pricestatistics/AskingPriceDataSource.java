package com.blueskyminds.analysis.property.pricestatistics;

import com.blueskyminds.analysis.core.datasource.AnalysisDataSource;
import com.blueskyminds.landmine.core.property.PropertyAdvertisementTypes;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Enumerated;

/**
 * An analysis datasource extended to identify the property advertisement type
 *
 * Date Started: 16/11/2007
 * <p/>
 * History:
 */
@Entity
@DiscriminatorValue("priceAnalysis")
public class AskingPriceDataSource extends AnalysisDataSource {

    private PropertyAdvertisementTypes propertyAdvertisementType;

    /**
     * Create a new datasource with the given description
     */
    public AskingPriceDataSource(String key, String description, PropertyAdvertisementTypes propertyAdvertisementType) {
        super(key, description);
        this.propertyAdvertisementType = propertyAdvertisementType;
    }

    /**
     * Default constructor for ORM
     */
    public AskingPriceDataSource() {
    }

    @Enumerated
    @Column(name="PropertyAdvertisementType")
    public PropertyAdvertisementTypes getPropertyAdvertisementType() {
        return propertyAdvertisementType;
    }

    public void setPropertyAdvertisementType(PropertyAdvertisementTypes propertyAdvertisementType) {
        this.propertyAdvertisementType = propertyAdvertisementType;
    }
}
