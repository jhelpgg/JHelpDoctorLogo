/**
 * Project : JHelpDoctorLogo<br>
 * Package : jhelp.logo<br>
 * Class : DoctorLogo<br>
 * Date : 11 sept. 2010<br>
 * By JHelp
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
      logoFrame.setVisible(true);
   }
}