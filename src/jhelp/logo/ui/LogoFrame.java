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
package jhelp.logo.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import jhelp.gui.FileChooser;
import jhelp.gui.JHelpAutoStyledTextArea;
import jhelp.gui.JHelpFrame;
import jhelp.gui.action.GenericAction;
import jhelp.logo.language.Language;
import jhelp.logo.language.Language.KeyWord;
import jhelp.util.debug.Debug;
import jhelp.util.filter.FileFilter;
import jhelp.util.io.UtilIO;

/**
 * Logo frame, for test in live logo scripts<br>
 * <br>
 * Last modification : 11 sept. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class LogoFrame
      extends JHelpFrame
{
   /**
    * Action for load a logo script <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   class LoadAction
         extends GenericAction
   {
      /**
		 *
		 */
      private static final long serialVersionUID = -5604948063947800836L;

      /**
       * Constructs LoadAction
       */
      LoadAction()
      {
         super("Load", "Load a program");
      }

      /**
       * Called when user clicked on load button <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param actionEvent
       *           Event description
       * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      protected void doActionPerformed(final ActionEvent actionEvent)
      {
         final File file = LogoFrame.this.fileChooser.showOpenFile();

         if(file != null)
         {
            UtilIO.createFile(file);

            BufferedReader bufferedReader = null;
            final StringBuilder stringBuilder = new StringBuilder();

            try
            {
               bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));

               String line = bufferedReader.readLine();
               while(line != null)
               {
                  stringBuilder.append(line);

                  line = bufferedReader.readLine();
                  if(line != null)
                  {
                     stringBuilder.append('\n');
                  }
               }

               LogoFrame.this.autoStyledTextArea.setText(stringBuilder.toString());

               LogoFrame.this.logoComponent.getLanguage().setDirectoryWork(file.getParentFile());
            }
            catch(final Exception exception)
            {
               Debug.printException(exception);
            }
            finally
            {
               if(bufferedReader != null)
               {
                  try
                  {
                     bufferedReader.close();
                  }
                  catch(final Exception exception2)
                  {
                     // Nothing to do
                  }
               }
            }
         }
      }
   }

   /**
    * Action for save actual logo script <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   class SaveAction
         extends GenericAction
   {
      /**
		 *
		 */
      private static final long serialVersionUID = 8596404903579132834L;

      /**
       * Constructs SaveAction
       */
      SaveAction()
      {
         super("Save", "Save the program");
      }

      /**
       * Called when user clicked on save button <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param actionEvent
       *           Event description
       * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      protected void doActionPerformed(final ActionEvent actionEvent)
      {
         final File file = LogoFrame.this.fileChooser.showSaveFile();

         if(file != null)
         {
            UtilIO.createFile(file);

            final String program = LogoFrame.this.autoStyledTextArea.getText();

            BufferedWriter bufferedWriter = null;

            try
            {
               bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));

               bufferedWriter.write(program);

               LogoFrame.this.logoComponent.getLanguage().setDirectoryWork(file.getParentFile());
            }
            catch(final Exception exception)
            {
               Debug.printException(exception);
            }
            finally
            {
               if(bufferedWriter != null)
               {
                  try
                  {
                     bufferedWriter.flush();
                  }
                  catch(final Exception exception2)
                  {
                     // Nothing to do
                  }

                  try
                  {
                     bufferedWriter.close();
                  }
                  catch(final Exception exception2)
                  {
                     // Nothing to do
                  }
               }
            }
         }
      }
   }

   /**
    * Action for send a logo script to logo component and show the result in screen <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   class SendAction
         extends GenericAction
   {
      /**
		 *
		 */
      private static final long serialVersionUID = 1694538268507486661L;

      /**
       * Constructs SendAction
       */
      SendAction()
      {
         super("Send", "Send to execute");
      }

      /**
       * Called when user click on send button to start the execution <br>
       * <br>
       * <b>Parent documentation:</b><br>
       * {@inheritDoc}
       * 
       * @param actionEvent
       *           Event description
       * @see jhelp.gui.action.GenericAction#doActionPerformed(java.awt.event.ActionEvent)
       */
      @Override
      protected void doActionPerformed(final ActionEvent actionEvent)
      {
         final Language language = LogoFrame.this.logoComponent.getLanguage();
         language.raz();

         language.appendBlocks(LogoFrame.this.autoStyledTextArea.getText());
      }
   }

   /**
	 *
	 */
   private static final long serialVersionUID = 8648642815736769745L;

   /** Send button */
   private JButton           buttonSend;
   /** Text area of script */
   JHelpAutoStyledTextArea   autoStyledTextArea;
   /** File chooser : window that les user choose a file for save or load */
   FileChooser               fileChooser;
   /** Component that show the result of a script */
   LogoComponent             logoComponent;

   /**
    * Constructs LogoFrame
    */
   public LogoFrame()
   {
      super("Logo editor");
   }

   /**
    * Create menu file
    * 
    * @return Created menu
    */
   private JMenu createFileMenu()
   {
      final JMenu menu = new JMenu("File");

      menu.add(new SaveAction());
      menu.add(new LoadAction());

      return menu;
   }

   /**
    * Create the menu bar
    * 
    * @return Created menu bar
    */
   private JMenuBar createJMenuBar()
   {
      final JMenuBar menuBar = new JMenuBar();

      menuBar.add(this.createFileMenu());

      return menuBar;
   }

   /**
    * Add listeners
    * 
    * @see jhelp.gui.JHelpFrame#addListeners()
    */
   @Override
   protected void addListeners()
   {
      // Nothing to do
   }

   /**
    * Indicates if can exit now
    * 
    * @return {@code true}
    * @see jhelp.gui.JHelpFrame#canCloseNow()
    */
   @Override
   protected boolean canCloseNow()
   {
      this.logoComponent.terminate();

      return true;
   }

   /**
    * Create components
    * 
    * @see jhelp.gui.JHelpFrame#createComponents()
    */
   @Override
   protected void createComponents()
   {
      this.fileChooser = new FileChooser(this);
      this.fileChooser.setTitle("Program logo");

      final FileFilter fileFilter = this.fileChooser.getFileFilter();
      fileFilter.addExtension("logo");
      this.fileChooser.setFileFilter(fileFilter);

      this.logoComponent = new LogoComponent(500, 500);

      this.autoStyledTextArea = new JHelpAutoStyledTextArea();
      this.autoStyledTextArea.setPreferredSize(new Dimension(567, 567));

      this.autoStyledTextArea.createStyle("DEFAULT", "Courier New", 12, false, false, false, Color.BLACK, Color.WHITE);
      this.autoStyledTextArea.createStyle("KeyWord", "Courier New", 14, true, false, false, Color.BLACK, Color.WHITE);
      this.autoStyledTextArea.createStyle("Symbol", "Courier New", 12, true, false, false, Color.BLUE, Color.WHITE);

      for(final KeyWord keyWord : KeyWord.values())
      {
         this.autoStyledTextArea.associate("KeyWord", keyWord.getName());
      }

      this.autoStyledTextArea.associate("Symbol", "PI", "E", "cos", "sin", "tan", "ln", "exp", "random");

      this.autoStyledTextArea.setSymbolStyle("Symbol");
      this.autoStyledTextArea.setDefaultStyle("DEFAULT");

      this.buttonSend = new JButton(new SendAction());
   }

   /**
    * Layout components
    * 
    * @see jhelp.gui.JHelpFrame#layoutComponents()
    */
   @Override
   protected void layoutComponents()
   {
      this.setJMenuBar(this.createJMenuBar());

      this.setLayout(new BorderLayout());

      // this.add(this.logoComponent, BorderLayout.CENTER);

      final JPanel panel = new JPanel(new BorderLayout());
      panel.add(new JScrollPane(this.autoStyledTextArea), BorderLayout.CENTER);
      panel.add(this.buttonSend, BorderLayout.SOUTH);

      // this.add(panel, BorderLayout.EAST);
      this.add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.logoComponent, panel), BorderLayout.CENTER);
   }

   /**
    * Language reference and interpret scripts
    * 
    * @return Language reference and interpret scripts
    */
   public Language getLanguage()
   {
      return this.logoComponent.getLanguage();
   }
}