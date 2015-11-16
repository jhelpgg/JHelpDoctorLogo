package jhelp.logo.language;

/**
 * Graphics for draw
 * 
 * @author JHelp
 */
public interface LogoGraphics
{
   /**
    * Clear the all graphics with the same color
    * 
    * @param color
    *           Color to clear with
    */
   public void clear(int color);

   /**
    * Draw a line
    * 
    * @param startX
    *           X of start point
    * @param startY
    *           Y of start point
    * @param endX
    *           X of end point
    * @param endY
    *           Y of end point
    * @param color
    *           Line color
    */
   public void drawLine(double startX, double startY, double endX, double endY, int color);

   /**
    * Fill with one color.<br>
    * The start point have a color, this color is used to know (using the precision) witch neighbor pixel to color also.
    * 
    * @param x
    *           Start point X
    * @param y
    *           Start point Y
    * @param color
    *           Color to fill with
    * @param near
    *           The "distance" accepted between start pixel color and neighbor color to fill them
    */
   public void fill(double x, double y, int color, int near);

   /**
    * Refresh the graphics.<br>
    * It updates the last changes on graphics to see them
    */
   public void refresh();
}