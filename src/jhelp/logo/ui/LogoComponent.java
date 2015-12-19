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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

import jhelp.logo.language.Language;
import jhelp.logo.language.LogoGraphics;
import jhelp.logo.language.Turtle;
import jhelp.util.gui.JHelpImage;

/**
 * Logo component that show logo script result, can be use as ordinary swing component<br>
 * <br>
 * Last modification : 11 sept. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class LogoComponent
      extends JComponent
      implements LogoGraphics
{
   /** Temporary line */
   private static final Line2D.Double LINE  = new Line2D.Double(0, 0, 0, 0);
   /** Temporary line */
   private static final Line2D.Double LINE2 = new Line2D.Double(0, 0, 0, 0);

   /** Image where draw */
   private JHelpImage                 bufferedImage;
   /** Script interpreter */
   private final Language             language;
   /** Component screen height */
   private int                        screenHeight;
   /** Component screen width */
   private int                        screenWidth;
   /** Turtle to use */
   private final Turtle               turtle;

   /**
    * Constructs LogoComponent
    * 
    * @param screenWidth
    *           Component width
    * @param screenHeight
    *           Component height
    */
   public LogoComponent(final int screenWidth, final int screenHeight)
   {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;

      this.turtle = new Turtle(screenWidth, screenHeight);
      this.language = new Language(this.turtle, this);

      this.bufferedImage = new JHelpImage(screenWidth, screenHeight, 0xFFFFFFFF);
      this.bufferedImage.startDrawMode();

      this.setSize(screenWidth, screenHeight);
   }

   /**
    * Call when JVM destroy the object
    * 
    * @throws Throwable
    *            On issue
    * @see java.lang.Object#finalize()
    */
   @Override
   protected void finalize() throws Throwable
   {
      this.language.terminate();

      super.finalize();
   }

   /**
    * Paint the component
    * 
    * @param g
    *           Graphics context
    * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
    */
   @Override
   protected void paintComponent(final Graphics g)
   {
      if(this.bufferedImage != null)
      {
         g.drawImage(this.bufferedImage.getImage(), 0, 0, null);
      }

      if((this.turtle != null) && (this.turtle.isVisible() == true))
      {
         g.setColor(new Color(this.turtle.getColor(), true));

         LogoComponent.LINE.x1 = this.turtle.getX();
         LogoComponent.LINE.y1 = this.turtle.getY();
         final double angleTurtle = this.turtle.getAngle();

         double angle = Math.toRadians(angleTurtle + 150);
         LogoComponent.LINE.x2 = LogoComponent.LINE.x1 + (20 * Math.cos(angle));
         LogoComponent.LINE.y2 = LogoComponent.LINE.y1 + (20 * Math.sin(angle));
         ((Graphics2D) g).draw(LogoComponent.LINE);

         angle = Math.toRadians(angleTurtle - 150);
         LogoComponent.LINE.x2 = LogoComponent.LINE.x1 + (20 * Math.cos(angle));
         LogoComponent.LINE.y2 = LogoComponent.LINE.y1 + (20 * Math.sin(angle));
         ((Graphics2D) g).draw(LogoComponent.LINE);
      }
   }

   /**
    * Clear component with one color
    * 
    * @param color
    *           Color to use
    */
   @Override
   public void clear(final int color)
   {
      this.bufferedImage.clear(color);
      this.refresh();
   }

   /**
    * Draw a line
    * 
    * @param startX
    *           Start point X
    * @param startY
    *           Start point Y
    * @param endX
    *           End point X
    * @param endY
    *           End point Y
    * @param color
    *           Line color
    */
   @Override
   public void drawLine(final double startX, final double startY, final double endX, final double endY, final int color)
   {
      LogoComponent.LINE2.x1 = startX;
      LogoComponent.LINE2.y1 = startY;
      LogoComponent.LINE2.x2 = endX;
      LogoComponent.LINE2.y2 = endY;
      this.bufferedImage.drawLine((int) startX, (int) startY, (int) endX, (int) endY, color);
   }

   /**
    * Fill with a color
    * 
    * @param x
    *           Start X
    * @param y
    *           Start Y
    * @param color
    *           Color to use
    * @param near
    *           Precision for fill
    */
   @Override
   public void fill(final double x, final double y, final int color, int near)
   {
      final int xx = (int) Math.round(x);
      final int yy = (int) Math.round(y);

      if((xx < 0) || (yy < 0) || (xx >= this.screenWidth) || (yy >= this.screenHeight))
      {
         return;
      }

      if(near < 0)
      {
         near = 0;
      }

      this.bufferedImage.fillColor(xx, yy, color, near, false);
      this.refresh();
   }

   /**
    * Language interpreter linked
    * 
    * @return Language interpreter linked
    */
   public Language getLanguage()
   {
      return this.language;
   }

   /**
    * Refresh the component
    */
   @Override
   public void refresh()
   {
      this.bufferedImage.update();
      this.repaint();
   }

   /**
    * Call each time component's bounds change
    * 
    * @param x
    *           X of up left corner
    * @param y
    *           Y of up left corner
    * @param width
    *           Width
    * @param height
    *           Height
    * @see java.awt.Component#setBounds(int, int, int, int)
    */
   @Override
   public void setBounds(final int x, final int y, final int width, final int height)
   {
      final Dimension dimension = new Dimension(width, height);
      this.setPreferredSize(dimension);
      this.setMaximumSize(dimension);
      this.setMinimumSize(dimension);

      super.setBounds(x, y, width, height);

      if((this.screenWidth != width) && (this.screenHeight != height))
      {
         this.refresh();

         final JHelpImage bufferedImage = new JHelpImage(width, height, 0xFFFFFFFF);
         bufferedImage.startDrawMode();
         bufferedImage.drawImage((width - this.screenWidth) >> 1, (height - this.screenHeight) >> 1, this.bufferedImage);

         this.bufferedImage = bufferedImage;
         this.screenWidth = width;
         this.screenHeight = height;

         this.turtle.setScreenSize(width, height);

         this.refresh();
      }
   }

   /**
    * Force terminate interpretation
    */
   public void terminate()
   {
      this.language.terminate();
   }
}