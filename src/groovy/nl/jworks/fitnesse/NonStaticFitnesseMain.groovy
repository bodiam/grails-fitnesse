package nl.jworks.fitnesse

import fitnesse.Arguments
import fitnesse.FitNesse
import fitnesse.responders.run.formatters.TestTextFormatter
import fitnesse.FitNesseContext
import fitnesse.VelocityFactory
import fitnesse.Updater
import fitnesse.updates.UpdaterImplementation
import fitnesse.wiki.PageVersionPruner
import fitnesse.components.PluginsClassLoader
import util.CommandLine
import fitnesse.responders.WikiImportTestEventListener
import fitnesse.responders.ResponderFactory
import fitnesse.WikiPageFactory
import fitnesse.ComponentFactory
import fitnesse.html.HtmlPageFactory
import fitnesse.authentication.OneUserAuthenticator
import fitnesse.components.Logger
import fitnesse.authentication.PromiscuousAuthenticator
import fitnesse.authentication.MultiUserAuthenticator

public class NonStaticFitNesseMain {
    protected String extraOutput;

    public boolean dontExitAfterSingleCommand;

    public static void main(String[] args) throws Exception {
        new NonStaticFitNesseMain().start(args)
    }

    public void start(String[] args) {
        Arguments arguments = parseCommandLine(args);
        if (arguments != null) {
            launchFitNesse(arguments);
        } else {
            printUsage();
            exit(1);
        }
    }

    protected void exit(int status) {
        System.exit(status);
    }

    public void launchFitNesse(Arguments arguments) throws Exception {
        loadPlugins();
        FitNesseContext context = loadContext(arguments);
        VelocityFactory.makeVelocityFactory(context);
        Updater updater = null;
        if (!arguments.isOmittingUpdates())
            updater = new UpdaterImplementation(context);
        PageVersionPruner.daysTillVersionsExpire = arguments.getDaysTillVersionsExpire();
        FitNesse fitnesse = new FitNesse(context, updater);
        updateAndLaunch(arguments, context, fitnesse);
    }

    protected void loadPlugins() throws Exception {
        new PluginsClassLoader().addPluginsToClassLoader();
    }

    protected void updateAndLaunch(Arguments arguments, FitNesseContext context,
                                FitNesse fitnesse) throws Exception {
        if (!arguments.isOmittingUpdates())
            fitnesse.applyUpdates();
        if (!arguments.isInstallOnly()) {
            runFitNesse(arguments, context, fitnesse);
        }
    }

    protected void runFitNesse(Arguments arguments, FitNesseContext context, FitNesse fitnesse) throws Exception {
        boolean started = fitnesse.start();
        if (started) {
            printStartMessage(arguments, context);
            if (arguments.getCommand() != null) {
                executeSingleCommand(arguments, fitnesse, context);
            }
        }
    }

    protected void executeSingleCommand(Arguments arguments, FitNesse fitnesse, FitNesseContext context) throws Exception {
        context.doNotChunk = true;
        TestTextFormatter.finalErrorCount = 0;
        System.out.println("Executing command: " + arguments.getCommand());
        System.out.println("-----Command Output-----");
        fitnesse.executeSingleCommand(arguments.getCommand(), System.out);
        System.out.println("-----Command Complete-----");
        fitnesse.stop();
        if (shouldExitAfterSingleCommand())
            exit(TestTextFormatter.finalErrorCount);
    }

    protected boolean shouldExitAfterSingleCommand() {
        return !dontExitAfterSingleCommand;
    }

    protected FitNesseContext loadContext(Arguments arguments) throws Exception {
        FitNesseContext context = new FitNesseContext();
        context.port = arguments.getPort();
        context.rootPath = arguments.getRootPath();
        ComponentFactory componentFactory = new ComponentFactory(context.rootPath);
        context.rootDirectoryName = arguments.getRootDirectory();
        context.setRootPagePath();
        String defaultNewPageContent = componentFactory.getProperty(ComponentFactory.DEFAULT_NEWPAGE_CONTENT);
        if (defaultNewPageContent != null)
            context.defaultNewPageContent = defaultNewPageContent;
        WikiPageFactory wikiPageFactory = new WikiPageFactory();
        context.responderFactory = new ResponderFactory(context.rootPagePath);
        context.logger = makeLogger(arguments);
        context.authenticator = makeAuthenticator(arguments.getUserpass(), componentFactory);
        context.htmlPageFactory = componentFactory.getHtmlPageFactory(new HtmlPageFactory());

        extraOutput = componentFactory.loadPlugins(context.responderFactory, wikiPageFactory);
        extraOutput += componentFactory.loadWikiPage(wikiPageFactory);
        extraOutput += componentFactory.loadResponders(context.responderFactory);
        extraOutput += componentFactory.loadSymbolTypes();
        extraOutput += componentFactory.loadContentFilter();

        context.root = wikiPageFactory.makeRootPage(context.rootPath, context.rootDirectoryName, componentFactory);

        WikiImportTestEventListener.register();

        return context;
    }

    public Arguments parseCommandLine(String[] args) {
        CommandLine commandLine = new CommandLine("[-p port][-d dir][-r root][-l logDir][-e days][-o][-i][-a userpass][-c command]");
        Arguments arguments = null;
        if (commandLine.parse(args)) {
            arguments = new Arguments();
            if (commandLine.hasOption("p"))
                arguments.setPort(commandLine.getOptionArgument("p", "port"));
            if (commandLine.hasOption("d"))
                arguments.setRootPath(commandLine.getOptionArgument("d", "dir"));
            if (commandLine.hasOption("r"))
                arguments.setRootDirectory(commandLine.getOptionArgument("r", "root"));
            if (commandLine.hasOption("l"))
                arguments.setLogDirectory(commandLine.getOptionArgument("l", "logDir"));
            if (commandLine.hasOption("e"))
                arguments.setDaysTillVersionsExpire(commandLine.getOptionArgument("e", "days"));
            if (commandLine.hasOption("a"))
                arguments.setUserpass(commandLine.getOptionArgument("a", "userpass"));
            if (commandLine.hasOption("c"))
                arguments.setCommand(commandLine.getOptionArgument("c", "command"));
            arguments.setOmitUpdates(commandLine.hasOption("o"));
            arguments.setInstallOnly(commandLine.hasOption("i"));
        }
        return arguments;
    }

    private Logger makeLogger(Arguments arguments) {
        String logDirectory = arguments.getLogDirectory();
        return logDirectory != null ? new Logger(logDirectory) : null;
    }

    public fitnesse.authentication.Authenticator makeAuthenticator(String authenticationParameter, ComponentFactory componentFactory) throws Exception {
        fitnesse.authentication.Authenticator authenticator = new PromiscuousAuthenticator();
        if (authenticationParameter != null) {
            if (new File(authenticationParameter).exists())
                authenticator = new MultiUserAuthenticator(authenticationParameter);
            else {
                String[] values = authenticationParameter.split(":");
                authenticator = new OneUserAuthenticator(values[0], values[1]);
            }
        }

        return componentFactory.getAuthenticator(authenticator);
    }

    private void printUsage() {
        System.err.println("Usage: java -jar fitnesse.jar [-pdrleoa]");
        System.err.println("\t-p <port number> {" + Arguments.DEFAULT_PORT + "}");
        System.err.println("\t-d <working directory> {" + Arguments.DEFAULT_PATH + "}");
        System.err.println("\t-r <page root directory> {" + Arguments.DEFAULT_ROOT + "}");
        System.err.println("\t-l <log directory> {no logging}");
        System.err.println("\t-e <days> {" + Arguments.DEFAULT_VERSION_DAYS + "} Number of days before page versions expire");
        System.err.println("\t-o omit updates");
        System.err.println("\t-a {user:pwd | user-file-name} enable authentication.");
        System.err.println("\t-i Install only, then quit.");
        System.err.println("\t-c <command> execute single command.");
    }

    private void printStartMessage(Arguments args, FitNesseContext context) {
        System.out.println("FitNesse (" + FitNesse.VERSION + ") Started...");
        System.out.print(context.toString());
        System.out.println("\tpage version expiration set to " + args.getDaysTillVersionsExpire() + " days.");
        if (extraOutput != null)
            System.out.print(extraOutput);
    }
}