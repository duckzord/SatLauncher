package Main;

import java.io.IOException;
import java.util.logging.*;

public class Logs {

   protected static Logger logger = Logger.getLogger("Collision Log");

   public Logs(String filename) {

      FileHandler fh; // Used to open a file and write to it.

      try {

         fh = new FileHandler("C:/" + filename, true); // Create a new file to store the logs in.
         logger.addHandler(fh); // Add the handler to the logger.
         logger.setLevel(Level.ALL); // Set either a specific level or all levels to written into the log.
         //logger.setLevel(Level.WARNING); // Set another level to look at.
         SimpleFormatter formatter = new SimpleFormatter(); // The format for the log.
         fh.setFormatter(formatter); // Use the formatter on the file handler.
      } catch (SecurityException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void addLog(Level level, String message) {

      logger.log(level, message); // Add a log to the file.

   }

}