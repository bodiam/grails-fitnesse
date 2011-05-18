package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Converter

/**
 * Groovy considers untyped arguments to be of type Object. This converter tried to convert the values from object
 * to the right value.
 *
 * @author Erik Pragt
 */
class ObjectConverter implements Converter {
    public String toString(Object o) {
        return o.toString();
    }

    public Object fromString(String arg) {
        return arg;
    }
}