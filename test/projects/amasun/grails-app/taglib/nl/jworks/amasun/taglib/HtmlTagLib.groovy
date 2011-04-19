package nl.jworks.amasun.taglib

import org.codehaus.groovy.grails.web.servlet.mvc.GrailsWebRequest

class HtmlTagLib {
    def img = { attrs, body ->
        def contextPath = GrailsWebRequest.lookup(request).contextPath
        def source = attrs.src

        attrs.remove('src')
        String attributes = attrs.collect { k, v -> "$k='$v'" }.join(" ")

        out << "<img src='${contextPath}/${source}' ${attributes} />"

    }

    def script = { attrs, body ->
        def contextPath = GrailsWebRequest.lookup(request).contextPath
        def source = attrs.src
        attrs.remove('src')

        String attributes = attrs.collect { k, v -> "$k='$v'" }.join(" ")

        out << "<script src='${contextPath}/${source}' ${attributes}></script>"

    }
}
