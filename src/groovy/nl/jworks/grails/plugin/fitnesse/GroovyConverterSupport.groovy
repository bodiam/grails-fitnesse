package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Converter
import fitnesse.slim.ConverterSupport
import fitnesse.slim.Slim

import java.lang.reflect.Type
import java.lang.reflect.ParameterizedType

class GroovyConverterSupport {
    
    public static Converter getConverter(Type type) {
        //in doesn't work here for primitives that's why instanceof is used
        boolean typeIsASimpleClass = type instanceof Class
        boolean typeIsEnumClass = type.isEnum()

        if(typeIsEnumClass) {
            Slim.addConverter(type, new EnumConverter(type))
        }

        Converter converter = typeIsASimpleClass ? ConverterSupport.getConverter(type) : null
        if (!converter) {
            converter = type in ParameterizedType ? new GroovyCollectionConverter(type) : new GroovyObjectConverter(type)
            typeIsASimpleClass && Slim.addConverter(type, converter)
        }
        return converter
    }

    public static Object convert(arg, Type type) {
        if (type == List.class && arg instanceof List) {
            return arg
        } else {
            Converter converter = getConverter(type)
            return converter.fromString(arg.toString())
        }
    }

    public static Object[] convertArgs(Object[] args, Object[] argumentTypes) {
        def converted = []
        args.eachWithIndex { arg, index ->
            converted << convert(arg, argumentTypes[index])
        }
        return converted as Object[]
    } 
}
