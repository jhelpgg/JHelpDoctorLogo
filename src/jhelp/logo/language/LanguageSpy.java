package jhelp.logo.language;

/**
 * Spy/listener of language execution state
 * 
 * @author JHelp
 */
public interface LanguageSpy
{
   /**
    * Called when a line is read
    * 
    * @param line
    *           Read line
    * @param lineNumber
    *           Line number
    * @param executing
    *           Indicates if line corresponds to an execution {@code true} OR just on register for later
    */
   public void excuteLine(String line, int lineNumber, boolean executing);

   /**
    * Called when no more instruction to play
    */
   public void finished();
}