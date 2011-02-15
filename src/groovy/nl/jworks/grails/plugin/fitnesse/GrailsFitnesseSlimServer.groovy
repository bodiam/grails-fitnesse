package nl.jworks.grails.plugin.fitnesse

import fitnesse.slim.SlimService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean

/**
 *
 * @author Erik Pragt
 */
class GrailsFitnesseSlimServer implements InitializingBean, DisposableBean {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final int startPort
    private final boolean verbose

    GrailsSlimFactory grailsSlimFactory

    private Map<Integer, SlimService> slimServices = [:]

    GrailsFitnesseSlimServer(int startPort, verbose) {
        this.startPort = startPort
        this.verbose = verbose
    }

    void startAcceptingConnections() {
        log.info "Accepting new connections starting at port ${startPort}"

        // BUG: For some reason, after closing the connection, the last opened slim server gets a SocketException. So I open 1 slim server extra
        // If you can find why this happens and know how to fix it, you'll get a good bottle of wine!
        11.times { offset ->
            int serverPort = startPort + offset

            try {
                def slimService = new SlimService(serverPort, grailsSlimFactory.getSlimServer(verbose))

                log.debug "Opening SlimService at port ${serverPort}."

                slimServices[serverPort] = slimService
            } catch (SocketException e) {
                log.error "Error: could not open a SlimSocket at port ${serverPort} : ${e.message}"
            }
        }
    }

    void destroy() {
        slimServices.each { port, SlimService slimService ->
            log.debug "Closing SlimService at port ${port}."
            slimService.close()
        }
        slimServices.clear()
    }

    void afterPropertiesSet() {
        startAcceptingConnections()
    }
}
