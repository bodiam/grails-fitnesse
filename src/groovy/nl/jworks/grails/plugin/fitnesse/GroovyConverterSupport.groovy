package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Slim
import fitnesse.slim.Converter
import fitnesse.slim.ConverterSupport

class GroovyConverterSupport {
    
    public static Converter getConverter(Class clazz) {
        Converter converter = ConverterSupport.getConverter(clazz)
        if (!converter) {
            converter = new GroovyObjectConverter(clazz)
            Slim.addConverter(clazz, converter)
        }
        return converter
    }

    public static Object convert(arg, Class type) {
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
