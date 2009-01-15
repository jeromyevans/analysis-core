package com.blueskyminds.analysis.core.series;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;

/**
 * Stores a value with a reference to its source
 *
 * Date Started: 4/01/2009
 * <p/>
 * Copyright (c) 2008 Blue Sky Minds Pty Ltd
 */
public class Data implements Comparable {

    private BigDecimal value;
    private Object source;

    public Data(BigDecimal value, Object source) {
        this.value = value;
        this.source = source;
    }

    public Data(BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Object getSource() {
        return source;
    }

    /**
     * Factory method to create an array of data without source entries
     * @param array
     * @return
     */
    public static Data[] fromArray(BigDecimal[] array) {
        Data[] values = new Data[array.length];
        for (int i = 0; i < array.length; i++) {
            values[i] = new Data(array[i], null);
        }
        return values;
    }

    /**
     * Factory method to create an array of data without source entries
     * @param array
     * @return
     */
    public static List<Data> asList(BigDecimal[] array) {
        List<Data> values = new ArrayList<Data>(array.length);
        for (BigDecimal value : array) {
            values.add(new Data(value, null));
        }
        return values;
    }

    public int compareTo(Object o) {
        if (value != null) {
            if (o != null) {
                if (o instanceof Data) {
                    return value.compareTo(((Data)o).getValue());
                }
            }
        }
        return 0;
    }
}
