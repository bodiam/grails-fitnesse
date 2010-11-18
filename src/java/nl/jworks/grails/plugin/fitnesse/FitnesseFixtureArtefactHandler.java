package nl.jworks.grails.plugin.fitnesse;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

/**
 * @author Erik Pragt
 */
public class FitnesseFixtureArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "FitnesseFixture";
    public static final String CLASSNAME_SUFFIX = "Fixture";

    public FitnesseFixtureArtefactHandler() {
        super(TYPE, GrailsFitnesseFixtureClass.class, DefaultGrailsFitnesseFixtureClass.class, CLASSNAME_SUFFIX);
    }
}
