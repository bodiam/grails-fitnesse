package nl.jworks.amasun.controller.console

import grails.util.Environment

class ConsoleController {

    def index = {
        if (Environment.current == Environment.DEVELOPMENT) {
            String[] databaseManagerOptions = ['--url', 'jdbc:hsqldb:mem:devDB', '--noexit']

            org.hsqldb.util.DatabaseManagerSwing.main(databaseManagerOptions)

            render "Database Manager started!"
        }
    }
}
