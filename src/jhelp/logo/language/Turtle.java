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
 * A turtle for draw<br>
 * <br>
 * Last modification : 11 sept. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class Turtle
{
   /** Actual turtle angle */
   double  angle;
   /** Turtle color */
   int     color;
   /** Indicates if turtle draw */
   boolean draw;
   /** Screen height */
   int     screenHeight;
   /** Screen width */
   int     screenWidth;
   /** Actual turtle speed */
   int     speed;
   /** Indicates if turtle is visible */
   boolean visible;
   /** Turtle X position */
   double  x;
   /** Turtle Y position */
   double  y;

   /**
    * Constructs Turtle
    * 
    * @param screenWidth
    *           Screen width
    * @param screenHeight
    *           Screen height
    */
   public Turtle(final int screenWidth, final int screenHeight)
   {
      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;

      this.reset();
   }

   /**
    * Return angle
    * 
    * @return angle
    */
   public double getAngle()
   {
      return this.angle;
   }

   /**
    * Return color
    * 
    * @return color
    */
   public int getColor()
   {
      return this.color;
   }

   /**
    * Return speed
    * 
    * @return speed
    */
   public int getSpeed()
   {
      return this.speed;
   }

   /**
    * Return x
    * 
    * @return x
    */
   public double getX()
   {
      return this.x;
   }

   /**
    * Return y
    * 
    * @return y
    */
   public double getY()
   {
      return this.y;
   }

   /**
    * Indicates if turtle draw when move
    * 
    * @return {@code true} if turtle draw when move
    */
   public boolean isDraw()
   {
      return this.draw;
   }

   /**
    * Return visible
    * 
    * @return visible
    */
   public boolean isVisible()
   {
      return this.visible;
   }

   /**
    * Reset the turtle to default value
    */
   public void reset()
   {
      this.x = this.screenWidth >> 1;
      this.y = this.screenHeight >> 1;

      this.color = 0xFF000000;
      this.draw = true;
      this.visible = true;

      this.angle = 270;
      this.speed = 345;
   }

   /**
    * Change the screen size
    * 
    * @param screenWidth
    *           New width
    * @param screenHeight
    *           New height
    */
   public void setScreenSize(final int screenWidth, final int screenHeight)
   {
      this.x += (screenWidth - this.screenWidth) >> 1;
      this.y += (screenHeight - this.screenHeight) >> 1;

      this.screenWidth = screenWidth;
      this.screenHeight = screenHeight;
   }
}