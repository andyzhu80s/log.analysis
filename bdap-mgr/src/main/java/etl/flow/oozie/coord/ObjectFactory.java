//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.10.21 at 03:56:46 PM PDT 
//


package etl.flow.oozie.coord;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the etl.flow.oozie.coord package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _CoordinatorApp_QNAME = new QName("uri:oozie:coordinator:0.5", "coordinator-app");
    private final static QName _Datasets_QNAME = new QName("uri:oozie:coordinator:0.5", "datasets");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: etl.flow.oozie.coord
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link CONFIGURATION }
     * 
     */
    public CONFIGURATION createCONFIGURATION() {
        return new CONFIGURATION();
    }

    /**
     * Create an instance of {@link PARAMETERS }
     * 
     */
    public PARAMETERS createPARAMETERS() {
        return new PARAMETERS();
    }

    /**
     * Create an instance of {@link DATASETS }
     * 
     */
    public DATASETS createDATASETS() {
        return new DATASETS();
    }

    /**
     * Create an instance of {@link COORDINATORAPP }
     * 
     */
    public COORDINATORAPP createCOORDINATORAPP() {
        return new COORDINATORAPP();
    }

    /**
     * Create an instance of {@link INPUTLOGIC }
     * 
     */
    public INPUTLOGIC createINPUTLOGIC() {
        return new INPUTLOGIC();
    }

    /**
     * Create an instance of {@link INPUTEVENTS }
     * 
     */
    public INPUTEVENTS createINPUTEVENTS() {
        return new INPUTEVENTS();
    }

    /**
     * Create an instance of {@link CONTROLS }
     * 
     */
    public CONTROLS createCONTROLS() {
        return new CONTROLS();
    }

    /**
     * Create an instance of {@link OUTPUTEVENTS }
     * 
     */
    public OUTPUTEVENTS createOUTPUTEVENTS() {
        return new OUTPUTEVENTS();
    }

    /**
     * Create an instance of {@link DATAIN }
     * 
     */
    public DATAIN createDATAIN() {
        return new DATAIN();
    }

    /**
     * Create an instance of {@link LOGICALDATAIN }
     * 
     */
    public LOGICALDATAIN createLOGICALDATAIN() {
        return new LOGICALDATAIN();
    }

    /**
     * Create an instance of {@link COMBINE }
     * 
     */
    public COMBINE createCOMBINE() {
        return new COMBINE();
    }

    /**
     * Create an instance of {@link LOGICALOR }
     * 
     */
    public LOGICALOR createLOGICALOR() {
        return new LOGICALOR();
    }

    /**
     * Create an instance of {@link DATAOUT }
     * 
     */
    public DATAOUT createDATAOUT() {
        return new DATAOUT();
    }

    /**
     * Create an instance of {@link WORKFLOW }
     * 
     */
    public WORKFLOW createWORKFLOW() {
        return new WORKFLOW();
    }

    /**
     * Create an instance of {@link ASYNCDATASET }
     * 
     */
    public ASYNCDATASET createASYNCDATASET() {
        return new ASYNCDATASET();
    }

    /**
     * Create an instance of {@link ACTION }
     * 
     */
    public ACTION createACTION() {
        return new ACTION();
    }

    /**
     * Create an instance of {@link FLAG }
     * 
     */
    public FLAG createFLAG() {
        return new FLAG();
    }

    /**
     * Create an instance of {@link SYNCDATASET }
     * 
     */
    public SYNCDATASET createSYNCDATASET() {
        return new SYNCDATASET();
    }

    /**
     * Create an instance of {@link LOGICALAND }
     * 
     */
    public LOGICALAND createLOGICALAND() {
        return new LOGICALAND();
    }

    /**
     * Create an instance of {@link CONFIGURATION.Property }
     * 
     */
    public CONFIGURATION.Property createCONFIGURATIONProperty() {
        return new CONFIGURATION.Property();
    }

    /**
     * Create an instance of {@link PARAMETERS.Property }
     * 
     */
    public PARAMETERS.Property createPARAMETERSProperty() {
        return new PARAMETERS.Property();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link COORDINATORAPP }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "uri:oozie:coordinator:0.5", name = "coordinator-app")
    public JAXBElement<COORDINATORAPP> createCoordinatorApp(COORDINATORAPP value) {
        return new JAXBElement<COORDINATORAPP>(_CoordinatorApp_QNAME, COORDINATORAPP.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DATASETS }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "uri:oozie:coordinator:0.5", name = "datasets")
    public JAXBElement<DATASETS> createDatasets(DATASETS value) {
        return new JAXBElement<DATASETS>(_Datasets_QNAME, DATASETS.class, null, value);
    }

}
