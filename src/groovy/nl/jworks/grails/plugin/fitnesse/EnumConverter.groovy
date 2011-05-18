package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.Converter

class EnumConverter implements Converter {
    Class type

    EnumConverter(Class type) {
        this.type = type
    }

    String toString(Object o) {
        return o.toString()
    }

    Object fromString(String s) {
        return Enum.valueOf(type, s)
    }
}
