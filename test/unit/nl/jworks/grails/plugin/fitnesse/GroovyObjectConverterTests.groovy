package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.SlimError
import fitnesse.slim.Slim
import fitnesse.slim.converters.IntegerArrayConverter
import fitnesse.slim.converters.StringConverter
import fitnesse.slim.converters.IntConverter

import grails.test.*
import fitnesse.slim.converters.ConverterRegistry

class ConverterTestClass {
    int number
    String string
}

class ComplexConverterTestClass {
    int[] numbers
    ConverterTestClass inner
}

class GroovyObjectConverterTests extends GrailsUnitTestCase {
    
    def converter = new GroovyObjectConverter(ConverterTestClass.class)

    public GroovyObjectConverterTests() {
        ConverterRegistry.addConverter(int.class, new IntConverter())
        ConverterRegistry.addConverter(String.class, new StringConverter())
        ConverterRegistry.addConverter(int[].class, new IntegerArrayConverter())
    }

    void testFailOnBadJSON() {
        shouldFail(SlimError.class) {
            converter.fromString('{bad: JSON')
        }
    }

    void testFailOnNonexistingProperty() {
        shouldFail(SlimError.class) {
            converter.fromString('{nonexistingProperty: value')
        }
    }

    void testFromString() {
        ConverterTestClass result = converter.fromString('{number: 5, string: test}')
        assertEquals result.number, 5
        assertEquals result.string, 'test'
    }

    void testComplexFromString() {
        def complex = new GroovyObjectConverter(ComplexConverterTestClass.class)
        ComplexConverterTestClass result = complex.fromString('{numbers: [1,2,3], inner: {number: 5, string: test}}')
        assert result.numbers == [1,2,3]
        assert result.inner.number == 5
        assert result.inner.string == 'test'
    }
}
