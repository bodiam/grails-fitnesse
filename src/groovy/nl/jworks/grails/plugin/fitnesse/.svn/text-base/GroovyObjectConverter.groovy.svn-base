package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Converter
import fitnesse.slim.SlimError

import java.lang.reflect.Constructor

import grails.converters.JSON

/**
 * This class allows converting a JSON string into any Groovy object using the constructor which is added to every
 * class in Groovy which accepts property map as the parameter.
 *
 * @author Marcin Erdmann
 */
class GroovyObjectConverter implements Converter {

    private Class convertedClass

    public GroovyObjectConverter(Class convertedClass) {
        this.convertedClass = convertedClass        
    }

    public String toString(Object o) {
        return o.toString();
    }

    public Object fromString(String arg) {
        def json
            try {
                json = JSON.parse(arg)
            } catch (JSONException) {
                throw new SlimError("Converting to ${convertedClass.name} failed: '${arg}' is not a proper JSON string")
            }
        def paramMap = json.keySet().inject([:]) { paramMap, paramName ->
            def paramType = convertedClass.metaClass.getMetaProperty(paramName)?.type
            if (!paramType) {
                throw new SlimError("Converting to ${convertedClass.name} failed: '${paramName}' is not a property of the class")
            }
            paramMap << new MapEntry(paramName, GroovyConverterSupport.convert(json[paramName], paramType))
        }
        return convertedClass.metaClass.invokeConstructor(paramMap)
    }
}
