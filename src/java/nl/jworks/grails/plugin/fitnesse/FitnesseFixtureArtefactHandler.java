package nl.jworks.grails.plugin.fitnesse;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

import java.lang.reflect.Modifier;

/**
 * @author Erik Pragt
 */
public class FitnesseFixtureArtefactHandler extends ArtefactHandlerAdapter {

    public static final String TYPE = "FitnesseFixture";
    public static final String CLASSNAME_SUFFIX = "Fixture";

    public FitnesseFixtureArtefactHandler() {
        super(TYPE, GrailsFitnesseFixtureClass.class, DefaultGrailsFitnesseFixtureClass.class, CLASSNAME_SUFFIX);
    }

    private boolean isAnnotatedFixtureClass(Class clazz) {
        return clazz != null && !Modifier.isAbstract(clazz.getModifiers()) && clazz.isAnnotationPresent(Fixture.class);
    }

    @Override
    public boolean isArtefactClass(Class clazz) {
        return isAnnotatedFixtureClass(clazz) || super.isArtefactClass(clazz);
    }
}
