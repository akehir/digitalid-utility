package net.digitalid.utility.testing;

import net.digitalid.utility.logging.Level;
import net.digitalid.utility.logging.logger.FileLogger;
import net.digitalid.utility.logging.logger.Logger;

import org.junit.BeforeClass;

/**
 * This class sets the output file of the logger.
 */
public abstract class LoggerSetup {
    
    /**
     * Sets the output file of the logger.
     */
    @BeforeClass
    public static void setUpLogging() {
        Logger.logger.set(FileLogger.with("target/test-logs/test.log"));
        Level.threshold.set(Level.VERBOSE);
    }
    
}
