package com.blueskyminds.analysis.core.datasource;

import com.blueskyminds.framework.AbstractEntity;

import javax.persistence.*;
import java.io.PrintStream;

/**
 * Identifies a source of data for analysis
 *
 * Date Started: 24/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity(name = "AnalysisDataSource")
@Table(name="analysis_DataSource")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Class")
@DiscriminatorValue("base")
public class AnalysisDataSource extends AbstractEntity {

    private String key;
    private String description;
//    private String spoolerClassName;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new datasource with the given description */
    public AnalysisDataSource(String key, String description) {
        this.key = key;
        this.description = description;
    }

    /** Create a new datasource with the given description and memento */
//    public AnalysisDataSource(String description, Class spoolerClass, XMLMemento memento) {
//        this.description = description;
//        if (spoolerClass != null) {
//            this.spoolerClassName = spoolerClass.getName();
//        }
//        setMemento(memento);
//    }

    /** Default constructor for ORM */
    protected AnalysisDataSource() {
    }

    @Basic
    @Column(name="KeyValue")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    /** Get the description of this DataSource */
    @Basic
    @Column(name="Description")
    public String getDescription() {
        return description;
    }

    protected void setDescription(String description) {
        this.description = description;
    }

    // ------------------------------------------------------------------------------------------------------

//    /** Get the className of the spooler to use on this datasource */
//    @Basic
//    @Column(name="Spooler")
//    public String getSpoolerClassName() {
//        return spoolerClassName;
//    }
//
//    protected void setSpoolerClassName(String spoolerClassName) {
//        this.spoolerClassName = spoolerClassName;
//    }
//
//    // ------------------------------------------------------------------------------------------------------
//
//    /** Create a spooler for this datasource */
//    public Spooler createSpooler() {
//        Spooler spooler = null;
//        if (spoolerClassName != null) {
//            try {
//                spooler = ReflectionTools.createInstanceOf(spoolerClassName);
//            } catch (ReflectionException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return spooler;
//    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        //out.println(getIdentityName()+" ("+key+" "+description+", spooler="+getSpoolerClassName()+", momento="+getMemento()+")");
        out.println(getIdentityName()+" ("+key+" "+description+")");
    }
}
