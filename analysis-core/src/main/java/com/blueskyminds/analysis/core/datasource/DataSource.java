package com.blueskyminds.analysis.core.datasource;

import com.blueskyminds.framework.memento.CaretakerDomainObject;
import com.blueskyminds.framework.memento.MementoOriginator;
import com.blueskyminds.framework.memento.XMLMemento;
import com.blueskyminds.framework.persistence.spooler.Spooler;
import com.blueskyminds.framework.tools.ReflectionException;
import com.blueskyminds.framework.tools.ReflectionTools;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.PrintStream;

/**
 * Identifies a source of data for analysis
 *
 * The EntitySpooler may use properties of the DataSource in the query
 *
 * Date Started: 24/08/2006
 *
 * History:
 *
 * Copyright (c) 2007 Blue Sky Minds Pty Ltd<br/>
 */
@Entity
@Table(name="analysis_DataSource")
public class DataSource extends CaretakerDomainObject implements MementoOriginator {

    private String description;
    private String spoolerClassName;

    // ------------------------------------------------------------------------------------------------------

    /** Create a new datasource with the given description */
    public DataSource(String description) {
        this.description = description;
    }

    /** Create a new datasource with the given description and memento */
    public DataSource(String description, Class spoolerClass, XMLMemento memento) {
        this.description = description;
        if (spoolerClass != null) {
            this.spoolerClassName = spoolerClass.getName();
        }
        setMemento(memento);
    }

    /** Default constructor for ORM */
    protected DataSource() {
    }

    // ------------------------------------------------------------------------------------------------------

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

    /** Get the className of the spooler to use on this datasource */
    @Basic
    @Column(name="Spooler")
    public String getSpoolerClassName() {
        return spoolerClassName;
    }

    protected void setSpoolerClassName(String spoolerClassName) {
        this.spoolerClassName = spoolerClassName;
    }

    // ------------------------------------------------------------------------------------------------------

    /** Create a spooler for this datasource */
    public Spooler createSpooler() {
        Spooler spooler = null;
        if (spoolerClassName != null) {
            try {
                spooler = ReflectionTools.createInstanceOf(spoolerClassName);
            } catch (ReflectionException e) {
                e.printStackTrace();
            }
        }

        return spooler;
    }

    // ------------------------------------------------------------------------------------------------------

    public void print(PrintStream out) {
        out.println(getIdentityName()+" ("+description+", spooler="+getSpoolerClassName()+", momento="+getMemento()+")");
    }
}
