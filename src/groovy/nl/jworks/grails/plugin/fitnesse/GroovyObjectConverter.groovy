package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Converter
import fitnesse.slim.SlimError

import grails.converters.JSON
import java.lang.reflect.Type
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Field
import org.codehaus.groovy.grails.web.json.JSONObject
import org.codehaus.groovy.grails.plugins.support.aware.GrailsApplicationAware
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.commons.DomainClassArtefactHandler

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
        JSONObject json
        try {
            json = JSON.parse(arg)
        } catch (JSONException) {
            throw new SlimError("Converting to ${convertedClass.name} failed: '${arg}' is not a proper JSON object string")
        }
        def paramMap = json.keySet().inject([:]) { paramMap, paramName ->
            Type paramType = determineFieldType(convertedClass, paramName)
            if (!paramType) {
                throw new SlimError("Converting to ${convertedClass.name} failed: '${paramName}' is not a property of the class")
            }
            paramMap << new MapEntry(paramName, GroovyConverterSupport.convert(json[paramName], paramType))
        }
        return convertedClass.metaClass.invokeConstructor(paramMap)
    }

    private Type determineFieldType(Class clazz, String fieldName) {
        if (DomainClassArtefactHandler.isDomainClass(clazz) && clazz.metaClass.getMetaProperty('hasMany')) {
            if (clazz.hasMany[fieldName]) {
                return ([
                        getActualTypeArguments: {-> [clazz.hasMany[fieldName]] as Type[] },
                        getOwnerType: {-> Set },
                        getRawType: {-> }
                ]) as ParameterizedType
            }
        }
        Field field = clazz.metaClass.getMetaProperty(fieldName)?.field?.field
        if (field?.type in Collection) {
            return field.genericType
        } else {
            return field?.type
        }
    }
}
