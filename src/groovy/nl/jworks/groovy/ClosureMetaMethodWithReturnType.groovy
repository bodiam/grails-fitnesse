package nl.jworks.groovy

import java.lang.reflect.Modifier
import org.codehaus.groovy.reflection.CachedClass
import org.codehaus.groovy.reflection.ReflectionCache
import org.codehaus.groovy.runtime.InvokerHelper

class ClosureMetaMethodWithReturnType extends MetaMethod {

    Class returnType
    String name
    Class originalDeclaringClass
    Closure closure

    public ClosureMetaMethodWithReturnType(String name, Class returnType, Class declaringClass, Closure closure) {
        super(closure.getParameterTypes())
        this.name = name
        this.returnType = returnType
        this.originalDeclaringClass = declaringClass
        this.closure = closure
    }

    public int getModifiers() {
        return Modifier.PUBLIC
    }

    public CachedClass getDeclaringClass() {
        return ReflectionCache.getCachedClass(originalDeclaringClass)
    }

    public Object invoke(Object object, Object[] arguments) {
        Closure cloned = (Closure) closure.clone()
        cloned.setDelegate(object)
        arguments = coerceArgumentsToClasses(arguments)
        return InvokerHelper.invokeMethod(cloned, "call", arguments)
    }
}
