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
package jhelp.logo;

import jhelp.logo.ui.LogoFrame;
import jhelp.util.gui.UtilGUI;

/**
 * Launch doctor logo editor<br>
 * <br>
 * Last modification : 11 sept. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class DoctorLogo
{
   /**
    * Launch doctor logo editor
    * 
    * @param args
    *           Unused
    */
   public static void main(final String[] args)
   {
      UtilGUI.initializeGUI();

      final LogoFrame logoFrame = new LogoFrame();
      UtilGUI.takeAllScreen(logoFrame);
      logoFrame.setVisible(true);
   }
}