package nl.jworks.grails.plugin.fitnesse;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;

/**
 * @author Erik Pragt
 */
public class DefaultGrailsFitnesseFixtureClass extends AbstractInjectableGrailsClass implements GrailsFitnesseFixtureClass {

    private final static String QUERY_FIXTURE = "queryFixture";
    private final static String MAPPING = "mapping";

    private boolean isQueryFixture;
    private Object mapping;

    public DefaultGrailsFitnesseFixtureClass(Class clazz) {
        super(clazz, FitnesseFixtureArtefactHandler.CLASSNAME_SUFFIX);

        Object tmpIsQueryFixture = getStaticPropertyValue(QUERY_FIXTURE, Boolean.class);
        isQueryFixture = Boolean.TRUE.equals(tmpIsQueryFixture);

        mapping = getStaticPropertyValue(MAPPING, Object.class);
    }

    public boolean getIsQueryFixture() {
        return isQueryFixture;
    }

    public Object getMapping() {
        return mapping;
    }
}
