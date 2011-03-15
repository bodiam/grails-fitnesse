package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Converter
import fitnesse.slim.SlimError
import grails.converters.JSON
import java.lang.reflect.ParameterizedType
import org.codehaus.groovy.grails.web.json.JSONArray
import java.lang.reflect.Type
import org.codehaus.groovy.grails.web.json.JSONObject

class GroovyCollectionConverter implements Converter {

    private ParameterizedType convertedType

    GroovyCollectionConverter(ParameterizedType convertedType) {
        this.convertedType = convertedType
    }

    String toString(Object o) {
        return o.toString()
    }

    Object fromString(String arg) {
        JSONArray json
        try {
            json = JSON.parse(arg)
        } catch (JSONException) {
            throw new SlimError("Converting to ${convertedClass.name} failed: '${arg}' is not a proper JSON array string")
        }
        Type parameterType = convertedType.actualTypeArguments.first()
        json.collect { JSONObject jsonObject ->
            GroovyConverterSupport.convert(jsonObject, parameterType)
        }
    }
}
