package com.epicmonstrosity.samuraistyle.lint;

import com.epicmonstrosity.samuraistyle.cli.CheckstyleOptions;
import com.puppycrawl.tools.checkstyle.*;
import com.puppycrawl.tools.checkstyle.api.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.puppycrawl.tools.checkstyle.ThreadModeSettings.SINGLE_THREAD_MODE_INSTANCE;

public class CheckstyleService {

    public int scan(final List<File> files, final CheckstyleOptions options) throws CheckstyleException, IOException {
        final Configuration config = getCheckstyleConfig(options);
        final ClassLoader moduleClassLoader = Checker.class.getClassLoader();
        final RootModule rootModule = getRootModule(config.getName(), moduleClassLoader);
        try {
            rootModule.setModuleClassLoader(moduleClassLoader);
            rootModule.configure(config);

            // printing to console is mutually exclusive, could add support for multiple listeners
            if (!options.isPrintToConsole())
                rootModule.addListener(createListener(options.getOutputFormatType(), options.getOutputFilename()));
            else
                rootModule.addListener(createListener(options.getOutputFormatType(), null));

            return rootModule.process(files);
        } finally {
            // Required to be called.
            rootModule.destroy();
        }
    }

    private Configuration getCheckstyleConfig(final CheckstyleOptions options) throws CheckstyleException {
        return ConfigurationLoader.loadConfiguration(
                options.getConfig(), new PropertiesExpander(System.getProperties()),
                ConfigurationLoader.IgnoredModulesOptions.OMIT, SINGLE_THREAD_MODE_INSTANCE);
    }

    private RootModule getRootModule(final String name, final ClassLoader moduleClassLoader) throws CheckstyleException {
        return (RootModule) new PackageObjectFactory(Checker.class.getPackage().getName(), moduleClassLoader).createModule(name);
    }

    private AuditListener createListener(final String format, final String output) throws IOException {
        final OutputStream outputStream = getOutputStream(output);
        final AutomaticBean.OutputStreamOptions outputStreamOptions = getOutputStreamOptions(output);
        return switch (format) {
            case "XML" -> new XMLLogger(outputStream, outputStreamOptions);
            case "SARIF" -> new SarifLogger(outputStream, outputStreamOptions);
            default -> new DefaultLogger(outputStream, outputStreamOptions);
        };
    }

    private OutputStream getOutputStream(final String output) throws IOException {
        return output == null ? System.out : Files.newOutputStream(Paths.get(output));
    }

    private AutomaticBean.OutputStreamOptions getOutputStreamOptions(String output) {
        return output == null ? AutomaticBean.OutputStreamOptions.NONE : AutomaticBean.OutputStreamOptions.CLOSE;
    }
}
