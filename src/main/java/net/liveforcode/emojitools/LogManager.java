/*
 * Emoji Tools helps users and developers of Android, iOS, and OS X extract, modify, and repackage Emoji fonts.
 * Copyright (C) 2015 - 2016 Mitch Talmadge
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact Mitch Talmadge at mitcht@liveforcode.net
 */

package net.liveforcode.emojitools;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class LogManager implements ConsoleManager.ConsoleListener {

    private final static int LOG_SIZE = 1024 * 1024;
    private final static int LOG_COUNT = 10;
    private final ConsoleManager consoleManager;
    private Logger logger;

    public LogManager(File logDirectory) throws IOException {
        if (!logDirectory.exists() && !logDirectory.mkdir()) {
            System.out.println("Emoji Tools could not create the log directory. Does it have permission?");
        } else {
            this.logger = Logger.getLogger(new Versioning().getProgramName());

            Handler handler = new FileHandler(logDirectory.getAbsolutePath() + "/" + new Versioning().getProgramName().replaceAll("\\s+", "_") + ".%u.%g.log", LOG_SIZE, LOG_COUNT);
            handler.setFormatter(new Formatter() {
                @Override
                public String format(LogRecord record) {
                    return new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a").format(new Date()) + " - " + record.getLevel() + ": " + record.getMessage();
                }
            });
            logger.addHandler(handler);
        }

        this.consoleManager = new ConsoleManager();
        consoleManager.addConsoleListener(this);
    }

    /**
     * Logs an info message to file.
     *
     * @param message The message to log.
     */
    public void logInfo(String message) {
        logger.info(message);
    }

    /**
     * Logs a warning message to file.
     *
     * @param message The message to log.
     */
    public void logWarning(String message) {
        logger.warning(message);
    }

    /**
     * Logs a severe message to file.
     *
     * @param message The message to log.
     */
    public void logSevere(String message) {
        logger.severe(message);
    }

    /**
     * Called when System.out is used.
     *
     * @param message The message printed.
     */
    @Override
    public void sysOut(String message) {
        if (!message.trim().isEmpty() && message.trim().length() > 0)
            logger.info(message.endsWith("\n") ? message : message + "\n");
    }

    /**
     * Called when System.err is used.
     *
     * @param message The message printed.
     */
    @Override
    public void sysErr(String message) {
        //logger.severe(message);
    }
}
