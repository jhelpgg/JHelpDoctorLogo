/**
 * <h1>License :</h1> <br>
 * The following code is deliver as is. I take care that code compile and work, but I am not responsible about any damage it may
 * cause.<br>
 * You can use, modify, the code as your need for any usage. But you can't do any action that avoid me or other person use,
 * modify this code. The code is free for usage and modification, you can't change that fact.<br>
 * <br>
 * 
 * @author JHelp
 */
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