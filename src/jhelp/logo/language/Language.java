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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import jhelp.util.Utilities;
import jhelp.util.debug.Debug;
import jhelp.util.debug.DebugLevel;
import jhelp.util.list.StackInt;
import jhelp.util.math.UtilMath;
import jhelp.util.math.formal.Function;
import jhelp.util.text.UtilText;

/**
 * Language reference<br>
 * It also contains a script to execute<br>
 * It also able to interpret is carry script <br>
 * <br>
 * Last modification : 12 sept. 2010<br>
 * Version 0.0.0<br>
 * 
 * @author JHelp
 */
public class Language
{
   /**
    * Mode of language interpreter <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   static enum Mode
   {
      /** In calling program mode */
      CALL_PROGRAM,
      /** In normal mode */
      NORMAL,
      /** In registering program mode */
      REGISTER_PROGRAM
   }

   /**
    * Thread that play the script <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   class ThreadExecute
         extends Thread
   {
      /**
       * Play the script
       * 
       * @see java.lang.Thread#run()
       */
      @Override
      public void run()
      {
         Language.this.runExecute();
      }
   }

   /**
    * Available key words string <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   public static final class Instructions
   {
      /**
       * ANGLE<br>
       * Current turtle angle. Can be use only inside &lt;expression&gt;
       */
      public static final String ANGLE         = "ANGLE";
      /**
       * CLEAR AARRGGBB<br>
       * Clear the screen with the given color in hexadecimal
       */
      public static final String CLEAR         = "CLEAR";
      /**
       * COLOR AARRGGBB<br>
       * Change the turtle color in hexadecimal. Next drawing will use this color
       */
      public static final String COLOR         = "COLOR";
      /**
       * COMPUTE_ANGLE &lt;expression_y&gt;,&lt;expression_x&gt;<br>
       * Compute angle of vector (&lt;expression_x&gt;, &lt;expression_y&gt;). Can be use only as &lt;expression&gt; in LET, ex:
       * LET a=COMPUTE_ANGLE y,x
       */
      public static final String COMPUTE_ANGLE = "COMPUTE_ANGLE";
      /**
       * FILL AARRGGBB<br>
       * Fill the area where lies the turtle
       */
      public static final String FILL          = "FILL";
      /**
       * FORWARD &lt;expression&gt;<br>
       * Make turtle go forward on its current direction with the specified amount. If the turtle pen is down, a segment is draw
       * from actual position to new position on using current color
       */
      public static final String FORWARD       = "FORWARD";
      /**
       * HIDE<br>
       * Hide the turtle. This not change the pen down state, the turtle is just reduce to one pixel
       */
      public static final String HIDE          = "HIDE";
      /**
       * HOME<br>
       * Put the turtle in middle on screen, and in direction to up. It draw a segment with current color if pen in down
       */
      public static final String HOME          = "HOME";
      /**
       * IMPORT &lt;path&gt;<br>
       * Import an other logo file to get programs inside it.<br>
       * Path can have several format : file://&lt;absolute local path&gt;, http://&lt;URL path&gt;, ../&lt;relative path&gt;,
       * ./&lt;relative path&gt;
       */
      public static final String IMPORT        = "IMPORT";
      /**
       * LET &lt;variable&gt;=&lt;expression&&gt;<br>
       * Define or change the value of a variable
       */
      public static final String LET           = "LET";
      /**
       * PEN_DOWN<br>
       * Put the pen down, so turtle will draw in its next moves
       */
      public static final String PEN_DOWN      = "PEN_DOWN";
      /**
       * PEN_UP<br>
       * Put the pen up, so turtle will not draw in its next moves
       */
      public static final String PEN_UP        = "PEN_UP";
      /**
       * PROGRAM &lt;name&gt;([&lt;variable&gt;[,&lt;variable&gt;[,&lt;variable&gt;[...]]]])<br>
       * {<br>
       * ...<br>
       * }<br>
       * Define a program, that is to say a set of instruction can be call at any time
       */
      public static final String PROGRAM       = "PROGRAM";
      /**
       * REPEAT &lt;expression&gt;<br>
       * {<br>
       * ...<br>
       * }<br>
       * Repeat the given time in variable a set of instruction
       */
      public static final String REPEAT        = "REPEAT";
      /**
       * ROTATE &lt;expression&gt;<br>
       * Rotate the turtle with the given angle
       */
      public static final String ROTATE        = "ROTATE";
      /**
       * SPEED &lt;expression&gt;<br>
       * Change the speed of turtle to move. The maximum speed is 0 (if specify &lt;0, 0 will be take) and more the value is
       * big, more the turtle go slow
       */
      public static final String SPEED         = "SPEED";
      /**
       * VISIBLE<br>
       * Show the turtle. This not change the pen down state, the turtle is just visible on screen
       */
      public static final String VISIBLE       = "VISIBLE";
      /**
       * X<br>
       * Current turtle X position. Can be use only inside &lt;expression&gt;
       */
      public static final String X             = "X";
      /**
       * Y<br>
       * Current turtle Y position. Can be use only inside &lt;expression&gt;
       */
      public static final String Y             = "Y";
   }

   /**
    * A key word <br>
    * <br>
    * Last modification : 13 janv. 2011<br>
    * Version 0.0.0<br>
    * 
    * @author JHelp
    */
   public static enum KeyWord
   {
      /**
       * ANGLE<br>
       * Current turtle angle. Can be use only inside &lt;expression&gt;
       */
      ANGLE(Instructions.ANGLE, "ANGLE", "Current turtle angle. Can be use only inside <expression>"),
      /**
       * CLEAR AARRGGBB<br>
       * Clear the screen with the given color in hexadecimal
       */
      CLEAR(Instructions.CLEAR, "CLEAR AARRGGBB", "Clear the screen with the given color in hexadecimal"),
      /**
       * COLOR AARRGGBB<br>
       * Change the turtle color in hexadecimal. Next drawing will use this color
       */
      COLOR(Instructions.COLOR, "COLOR AARRGGBB", "Change the turtle color in hexadecimal. Next drawing will use this color"),
      /**
       * COMPUTE_ANGLE &lt;expression_y&gt;,&lt;expression_x&gt;<br>
       * Compute angle of vector (&lt;expression_x&gt;, &lt;expression_y&gt;). Can be use only as &lt;expression&gt; in LET, ex:
       * LET a=COMPUTE y,x
       */
      COMPUTE_ANGLE(Instructions.COMPUTE_ANGLE, "COMPUTE_ANGLE <expression_y>,<expression_x>",
            "Compute angle of vector (<expression_x>, <expression_y>). Can be use only as <expression> in LET, ex:  LET a=COMPUTE y,x"),
      /**
       * FILL AARRGGBB<br>
       * Fill the area where lies the turtle
       */
      FILL(Instructions.FILL, "FILL AARRGGBB,<expression>", "Fill the area where lies the turtle"),
      /**
       * FORWARD &lt;expression&gt;<br>
       * Make turtle go forward on its current direction with the specified amount. If the turtle pen is down, a segment is draw
       * from actual position to new position on using current color
       */
      FORWARD(
            Instructions.FORWARD,
            "FORWARD <expression>",
            "Make turtle go forward on its current direction with the specified amount. If the turtle pen is down, a segment is draw from actual position to new position on using current color"),
      /**
       * HIDE<br>
       * Hide the turtle. This not change the pen down state, the turtle is just reduce to one pixel
       */
      HIDE(Instructions.HIDE, "HIDE", "Hide the turtle. This not change the pen down state, the turtle is just reduce to one pixel"),
      /**
       * HOME<br>
       * Put the turtle in middle on screen, and in direction to up. It draw a segment with current color if pen in down
       */
      HOME(Instructions.HOME, "HOME", "Put the turtle in middle on screen, and in direction to up. It draw a segment with current color if pen in down"),
      /**
       * IMPORT &lt;path&gt;<br>
       * Import an other logo file to get programs inside it.<br>
       * Path can have several format : file://&lt;absolute local path&gt;, http://&lt;URL path&gt;, ../&lt;relative path&gt;,
       * ./&lt;relative path&gt;
       */
      IMPORT(
            Instructions.IMPORT,
            "IMPORT <path>",
            "Import an other logo file to get programs inside it.\nPath can have serveral format : file://<absolute local path>, http://<URL path>, ../<relative path>, ./<relative path>"),
      /**
       * LET &lt;variable&gt;=&lt;expression&&gt;<br>
       * Define or change the value of a variable
       */
      LET(Instructions.LET, "LET <variable>=<expression>", "Define or change the value of a variable"),
      /**
       * PEN_DOWN<br>
       * Put the pen down, so turtle will draw in its next moves
       */
      PEN_DOWN(Instructions.PEN_DOWN, "PEN_DOWN", "Put the pen down, so turtle will draw in its next moves"),
      /**
       * PEN_UP<br>
       * Put the pen up, so turtle will not draw in its next moves
       */
      PEN_UP(Instructions.PEN_UP, "PEN_UP", "Put the pen up, so turtle will not draw in its next moves"),
      /**
       * PROGRAM &lt;name&gt;([&lt;variable&gt;[,&lt;variable&gt;[,&lt;variable&gt ;[...]]]])<br>
       * {<br>
       * ...<br>
       * }<br>
       * Define a program, that is to say a set of instruction can be call at any time
       */
      PROGRAM(Instructions.PROGRAM, "PROGRAM <name>([<variable>[,<variable>[,<variable>[...]]]])\n{\n ...\n}",
            "Define a program, that is to say a set of instruction can be call at any time"),
      /**
       * REPEAT &lt;expression&gt;<br>
       * {<br>
       * ...<br>
       * <br>
       * Repeat then given time in variable a set of instruction
       */
      REPEAT(Instructions.REPEAT, "REPEAT <expression>\n{\n ...\n}", "Repeat then given time in variable a set of instruction"),
      /**
       * ROTATE &lt;expression&gt;<br>
       * Rotate the turtle with the given angle
       */
      ROTATE(Instructions.ROTATE, "ROTATE <expression>", "Rotate the turtle with the given angle"),
      /**
       * SPEED &lt;expression&gt;<br>
       * Change the speed of turtle to move. The maximum speed is 0 (if specify &lt;0, 0 will be take) and more the value is
       * big, more the turtle go slow
       */
      SPEED(Instructions.SPEED, "SPEED <expression>",
            "Change the speed of turtle to move. The maximum speed is 0 (if specify <0, 0 will be take) and more the value is big, more the turtle go slow"),
      /**
       * VISIBLE<br>
       * Show the turtle. This not change the pen down state, the turtle is just visible on screen
       */
      VISIBLE(Instructions.VISIBLE, "VISIBLE", "Show the turtle. This not change the pen down state, the turtle is just visible on screen"),
      /**
       * X<br>
       * Current turtle X position. Can be use only inside &lt;expression&gt;
       */
      X(Instructions.X, "X", "Current turtle X position. Can be use only inside <expression>"),
      /**
       * Y<br>
       * Current turtle Y position. Can be use only inside &lt;expression&gt;
       */
      Y(Instructions.Y, "Y", "Current turtle Y position. Can be use only inside <expression>"), ;

      /** Full description */
      private final String description;
      /** Key word name */
      private final String name;
      /** Short description */
      private final String shortDecsiption;

      /**
       * Constructs KeyWord
       * 
       * @param name
       *           Name
       * @param shortDecsiption
       *           Short description
       * @param description
       *           Full description
       */
      KeyWord(final String name, final String shortDecsiption, final String description)
      {
         this.name = name;
         this.shortDecsiption = shortDecsiption;
         this.description = description;
      }

      /**
       * Return description
       * 
       * @return description
       */
      public String getDescription()
      {
         return this.description;
      }

      /**
       * Return name
       * 
       * @return name
       */
      public String getName()
      {
         return this.name;
      }

      /**
       * Return shortDecsiption
       * 
       * @return shortDecsiption
       */
      public String getShortDecsiption()
      {
         return this.shortDecsiption;
      }
   }

   /** Work directory */
   private File                                 directoryWork;
   /** Indicates if finish of interpretation */
   private boolean                              finish;
   /** Spy/listener of execution events */
   private LanguageSpy                          languageSpy;
   /** Script's lines */
   private final Vector<String>                 lines;
   /** Component link to draw on it */
   private final LogoGraphics                   logoGraphics;
   /** Actual parameters value */
   private final Stack<HashMap<String, Double>> parameters;
   /** Registered program list */
   private final HashMap<String, Integer>       programs;
   /** Thread for script execution */
   private ThreadExecute                        threadExecute;
   /** The turtle */
   private final Turtle                         turtle;

   /** Indicates if is waiting mode */
   private boolean                              waiting;

   /**
    * Constructs Language
    * 
    * @param turtle
    *           The turtle to use
    * @param logoGraphics
    *           Graphics where draw turtle, lines, fill area
    */
   public Language(final Turtle turtle, final LogoGraphics logoGraphics)
   {
      this.turtle = turtle;
      this.logoGraphics = logoGraphics;

      this.parameters = new Stack<HashMap<String, Double>>();
      this.parameters.push(new HashMap<String, Double>());

      this.lines = new Vector<String>();

      this.programs = new HashMap<String, Integer>();
      this.waiting = false;
      this.finish = false;
   }

   /**
    * Compute the value of a function with actual parameters value
    * 
    * @param context
    *           Actual parameters value
    * @param function
    *           Function to compute
    * @return Function value
    */
   private double computeValue(final HashMap<String, Double> context, final String function)
   {
      Function fonction = Function.parse(UtilText.removeWhiteCharacters(function));

      fonction = fonction.replace(Language.Instructions.ANGLE, -this.turtle.angle);
      fonction = fonction.replace(Language.Instructions.X, this.turtle.x);
      fonction = fonction.replace(Language.Instructions.Y, this.turtle.y);
      fonction = fonction.replace("random", Math.random());

      for(final String key : context.keySet())
      {
         fonction = fonction.replace(key, context.get(key));
      }

      return fonction.simplifyMaximum().obtainRealValueNumber();
   }

   /**
    * Cut a block of instruction in lines
    * 
    * @param block
    *           Block to cut
    */
   private void cutInLine(final String block)
   {
      final StringTokenizer stringTokenizer = new StringTokenizer(block, ";\n\r", false);
      String part;

      while(stringTokenizer.hasMoreElements())
      {
         part = stringTokenizer.nextToken().trim();

         if((part.length() > 0) && (!part.startsWith("//")))
         {
            this.lines.add(part);
         }
      }
   }

   /**
    * Exceute the current script
    */
   @SuppressWarnings({"unchecked", "ConstantConditions"})
   void runExecute()
   {
      String line;
      final StackInt stack = new StackInt();
      int index = 0;
      Mode mode = Mode.NORMAL;
      boolean turtleInstruction = false;
      HashMap<String, Double> context = this.parameters.pop();
      int indexSpace;
      int tempInt1, tempInt2;
      String tempString1, tempString2;
      double tempDouble = 0;
      String instruction;
      int countProg = 0;

      if(this.lines.isEmpty())
      {
         if(this.languageSpy != null)
         {
            this.languageSpy.finished();
         }
      }

      while((index < this.lines.size()) && (!this.finish))
      {
         line = this.lines.elementAt(index);

         if(this.languageSpy != null)
         {
            this.languageSpy.excuteLine(line, index, mode != Mode.REGISTER_PROGRAM);
         }

         index++;
         turtleInstruction = false;

         // Interpret the line

         indexSpace = line.indexOf(' ');
         if(indexSpace >= 0)
         {
            instruction = line.substring(0, indexSpace);
         }
         else
         {
            instruction = line;
         }

         if(mode != Mode.REGISTER_PROGRAM)
         {
            // LET
            if(Instructions.LET.equals(instruction))
            {
               tempInt1 = line.indexOf('=', indexSpace);

               if((tempInt1 <= indexSpace) || (tempInt1 >= (line.length() - 1)))
               {
                  Debug.println(DebugLevel.ERROR, "The instruction LET is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1, tempInt1).trim();
                  tempString2 = line.substring(tempInt1 + 1).trim();

                  if(tempString2.startsWith(Instructions.COMPUTE_ANGLE))
                  {
                     tempInt1 = tempString2.indexOf(' ');
                     if(tempInt1 < 0)
                     {
                        Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                              " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                     }
                     else
                     {
                        tempInt2 = tempString2.indexOf(',', tempInt1);
                        if(tempInt2 < 0)
                        {
                           Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                                 " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                        }
                        else
                        {
                           final double y = this.computeValue(context, tempString2.substring(tempInt1 + 1, tempInt2));
                           final double x = this.computeValue(context, tempString2.substring(tempInt2 + 1));

                           tempDouble = -Math.toDegrees(Math.atan2(y, x));
                           if(Double.isNaN(tempDouble))
                           {
                              Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                                    " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                           }
                        }
                     }
                  }
                  else
                  {
                     tempDouble = this.computeValue(context, tempString2);
                     if(Double.isNaN(tempDouble))
                     {
                        Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                              " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                     }
                  }

                  context.put(tempString1, tempDouble);
               }
            }
            // FORWARD
            else if(Instructions.FORWARD.equals(instruction))
            {
               if(indexSpace < 0)
               {
                  Debug.println(DebugLevel.ERROR, "The instruction FORWARD is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1);

                  tempDouble = this.computeValue(context, tempString1);

                  if(Double.isNaN(tempDouble))
                  {
                     Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                           " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                  }
                  else
                  {
                     final double startX = this.turtle.x;
                     final double startY = this.turtle.y;
                     final double angleTurtle = this.turtle.angle;

                     final double angle = Math.toRadians(angleTurtle);
                     final double endX = startX + (tempDouble * Math.cos(angle));
                     final double endY = startY + (tempDouble * Math.sin(angle));

                     if(this.turtle.draw)
                     {
                        this.logoGraphics.drawLine(startX, startY, endX, endY, this.turtle.color);
                     }

                     this.turtle.x = endX;
                     this.turtle.y = endY;

                     turtleInstruction = true;
                  }
               }
            }
            // ROTATE
            else if(Instructions.ROTATE.equals(instruction))
            {
               if(indexSpace < 0)
               {
                  Debug.println(DebugLevel.ERROR, "The instruction ROTATE is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1);

                  tempDouble = this.computeValue(context, tempString1);

                  if(Double.isNaN(tempDouble))
                  {
                     Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                           " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                  }
                  else
                  {
                     this.turtle.angle -= tempDouble;

                     while(UtilMath.sign(this.turtle.angle) < 0)
                     {
                        this.turtle.angle += 360;
                     }

                     while(UtilMath.sign(360 - this.turtle.angle) <= 0)
                     {
                        this.turtle.angle -= 360;
                     }

                     turtleInstruction = true;
                  }
               }
            }
            // COLOR
            else if(Instructions.COLOR.equals(instruction))
            {
               if(indexSpace < 0)
               {
                  Debug.println(DebugLevel.ERROR, "The instruction COLOR is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1).trim();

                  try
                  {
                     this.turtle.color = UtilText.parseHexaString(tempString1);

                     turtleInstruction = true;
                  }
                  catch(final Exception exception)
                  {
                     final Exception exception2 = new Exception(UtilText.concatenate("Malformed color description at line ", index - 1, " | Line=", line),
                           exception);

                     Debug.printException(exception2);
                  }
               }
            }
            // PEN_DOWN
            else if(Instructions.PEN_DOWN.equals(instruction))
            {
               this.turtle.draw = true;
               turtleInstruction = true;
            }
            // PEN_UP
            else if(Instructions.PEN_UP.equals(instruction))
            {
               this.turtle.draw = false;
               turtleInstruction = true;
            }
            // VISIBLE
            else if(Instructions.VISIBLE.equals(instruction))
            {
               this.turtle.visible = true;

               turtleInstruction = true;
            }
            // HIDE
            else if(Instructions.HIDE.equals(instruction))
            {
               this.turtle.visible = false;

               turtleInstruction = true;
            }
            // HOME
            else if(Instructions.HOME.equals(instruction))
            {
               final double startX = this.turtle.x;
               final double startY = this.turtle.y;

               final double endX = this.turtle.screenWidth >> 1;
               final double endY = this.turtle.screenHeight >> 1;

               if(this.turtle.draw)
               {
                  this.logoGraphics.drawLine(startX, startY, endX, endY, this.turtle.color);
               }

               this.turtle.x = endX;
               this.turtle.y = endY;
               this.turtle.angle = 270;

               turtleInstruction = true;
            }
            // CLEAR
            else if(Instructions.CLEAR.equals(instruction))
            {
               if(indexSpace < 0)
               {
                  Debug.println(DebugLevel.ERROR, "The instruction CLEAR is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1).trim();

                  try
                  {
                     this.logoGraphics.clear(UtilText.parseHexaString(tempString1));
                  }
                  catch(final Exception exception)
                  {
                     Debug.printException(exception, "Malformed color description at line ", index - 1, " | Line=", line);
                  }
               }
            }
            // SPEED
            else if(Instructions.SPEED.equals(instruction))
            {
               if(indexSpace < 0)
               {
                  Debug.println(DebugLevel.ERROR, "The instruction SPEED is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1);

                  tempDouble = this.computeValue(context, tempString1);

                  if(Double.isNaN(tempDouble))
                  {
                     Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                           " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                  }
                  else
                  {
                     this.turtle.speed = Math.max((int) Math.round(tempDouble), 0);
                  }
               }
            }
            // REPEAT
            else if(Instructions.REPEAT.equals(instruction))
            {
               if(indexSpace < 0)
               {
                  Debug.println(DebugLevel.ERROR, "The instruction REPEAT is malformed at line ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1);

                  tempDouble = this.computeValue(context, tempString1);

                  if(Double.isNaN(tempDouble))
                  {
                     Debug.println(DebugLevel.WARNING, "Error in evaluate value of line ", index - 1,
                           " a parameter is not define or a parameter have illegal value (divide by zero, ln of <=0 number, ...) : ", line);
                  }
                  else
                  {
                     final int time = (int) Math.round(tempDouble);
                     line = this.lines.elementAt(index++);

                     if(!line.equals("{"))
                     {
                        Debug.println(DebugLevel.ERROR, "No open brace { affter REPEAT in line " + (index - 2));
                     }
                     else if(time <= 0)
                     {
                        int count = 0;
                        line = this.lines.elementAt(index++);

                        while(index < this.lines.size())
                        {
                           line = this.lines.elementAt(index++);

                           if(line.equals("}"))
                           {
                              if(count <= 0)
                              {
                                 break;
                              }

                              count--;
                           }
                           else if(line.equals("{"))
                           {
                              count++;
                           }
                        }
                     }
                     else
                     {
                        stack.push(index);
                        stack.push(-time);

                        this.parameters.push(context);
                        context = (HashMap<String, Double>) context.clone();
                     }
                  }
               }
            }
            // }
            else if(instruction.equals("}"))
            {
               if((stack.isEmpty()) || (this.parameters.isEmpty()))
               {
                  Debug.println(DebugLevel.ERROR, "Unexpected } at ", index - 1);
               }
               else
               {
                  int val = stack.pop();

                  if(val < 0)
                  {
                     // Repeat case

                     final int ind = stack.pop();

                     val++;

                     if(val < 0)
                     {
                        index = ind;

                        stack.push(index);
                        stack.push(val);
                     }
                     else
                     {
                        context = this.parameters.pop();
                     }
                  }
                  else
                  {
                     // Program case

                     index = val;

                     if(this.parameters.size() <= 1)
                     {
                        mode = Mode.NORMAL;
                     }

                     context = this.parameters.pop();
                  }
               }
            }
            // PROGRAM
            else if(Instructions.PROGRAM.equals(instruction))
            {
               if((!stack.isEmpty()) || (mode == Mode.CALL_PROGRAM))
               {
                  Debug.println(DebugLevel.ERROR, "Can't declare program inside PROGRAM or REPEAT at ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = null;

                  if(indexSpace > 0)
                  {
                     tempInt1 = line.indexOf('(', indexSpace + 1);

                     if(tempInt1 >= indexSpace)
                     {
                        tempInt2 = line.length() - 1;

                        if(line.charAt(tempInt2) == ')')
                        {
                           tempString1 = line.substring(indexSpace + 1, tempInt1).trim();
                        }
                     }
                  }

                  if(tempString1 == null)
                  {
                     Debug.println(DebugLevel.ERROR, "Malformed program declaration at ", index - 1, " : ", line);
                  }
                  else
                  {
                     line = this.lines.elementAt(index++);

                     if(!line.equals("{"))
                     {
                        Debug.println(DebugLevel.ERROR, "No open brace { affter PROGRAM in line " + (index - 2));
                     }
                     else
                     {
                        this.programs.put(tempString1, index - 2);

                        mode = Mode.REGISTER_PROGRAM;

                        countProg = 0;
                     }
                  }
               }
            }
            // IMPORT
            else if(Instructions.IMPORT.equals(instruction))
            {
               tempString1 = line.substring(indexSpace + 1).trim();

               index--;
               this.lines.remove(index);

               int insert = index;
               InputStream inputStream = null;

               if(tempString1.startsWith("http://"))
               {
                  try
                  {
                     final URL url = new URL(tempString1);
                     inputStream = url.openStream();
                  }
                  catch(final Exception exception)
                  {
                     Debug.printException(exception, "Can't load : ", tempString1);
                  }
               }
               else if(tempString1.startsWith("file://"))
               {
                  try
                  {
                     final File file = new File(tempString1.substring(7));
                     inputStream = new FileInputStream(file);
                  }
                  catch(final Exception exception)
                  {
                     Debug.printException(exception, "Can't load : ", tempString1);
                  }
               }
               else
               {
                  if(this.directoryWork == null)
                  {
                     Debug.println(DebugLevel.WARNING, "No work directory !");
                  }
                  else
                  {
                     try
                     {
                        final StringTokenizer stringTokenizer = new StringTokenizer(tempString1, "/", false);
                        File file = this.directoryWork;
                        String pathElement;

                        while(stringTokenizer.hasMoreTokens())
                        {
                           pathElement = stringTokenizer.nextToken();

                           if(pathElement.equals(".."))
                           {
                              file = file.getParentFile();
                           }
                           else if(!pathElement.equals("."))
                           {
                              file = new File(file, pathElement);
                           }
                        }

                        if(file.exists())
                        {
                           inputStream = new FileInputStream(file);
                        }
                        else
                        {
                           Debug.println(DebugLevel.WARNING, "File ", file.getAbsolutePath(), " doesn't exists at line ", index);
                        }
                     }
                     catch(final Exception exception)
                     {
                        Debug.printException(exception, "Can't load : ", tempString1);
                     }
                  }
               }

               if(inputStream != null)
               {
                  BufferedReader bufferedReader = null;
                  try
                  {
                     bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                     String lig = bufferedReader.readLine();

                     while(lig != null)
                     {
                        lig = lig.trim();

                        if((lig.length() > 0) && (!lig.startsWith("//")))
                        {
                           this.lines.insertElementAt(lig, insert++);
                        }

                        lig = bufferedReader.readLine();
                     }
                  }
                  catch(final Exception exception)
                  {
                     Debug.printException(exception, "Can't load : ", tempString1);
                  }
                  finally
                  {
                     if(bufferedReader != null)
                     {
                        try
                        {
                           bufferedReader.close();
                        }
                        catch(final Exception ignored)
                        {
                        }
                     }
                  }
               }
            }
            // FILL
            else if(Instructions.FILL.equals(instruction))
            {
               tempInt1 = line.indexOf(',', indexSpace + 1);
               if(tempInt1 < 0)
               {
                  Debug.println(DebugLevel.ERROR, "Malformed line :", line);
               }
               else
               {
                  tempString1 = line.substring(indexSpace + 1, tempInt1).trim();
                  tempString2 = line.substring(tempInt1 + 1).trim();

                  try
                  {
                     final int color = UtilText.parseHexaString(tempString1);
                     final double value = this.computeValue(context, tempString2);

                     if(Double.isNaN(value))
                     {
                        Debug.println(DebugLevel.ERROR, "Couldn't evaluate line :", line);
                     }
                     else
                     {
                        this.logoGraphics.fill(this.turtle.x, this.turtle.y, color, (int) Math.round(value));
                     }
                  }
                  catch(final Exception exception)
                  {
                     Debug.printException(exception, "Malformed line :", line);
                  }
               }
            }
            // Other case
            else
            {
               tempInt1 = line.indexOf('(');
               tempInt2 = line.length() - 1;

               if((tempInt1 < 1) || (line.charAt(tempInt2) != ')'))
               {
                  Debug.println(DebugLevel.ERROR, "Malformed line at ", index - 1, " : ", line);
               }
               else
               {
                  tempString1 = line.substring(0, tempInt1).trim();

                  final Integer integer = this.programs.get(tempString1);
                  if(integer == null)
                  {
                     Debug.println(DebugLevel.ERROR, "Line not refer an instruction or a defined program at ", index - 1, " : ", line);
                  }
                  else
                  {
                     final int ind = integer.intValue();

                     StringTokenizer stringTokenizer = new StringTokenizer(line.substring(tempInt1 + 1, tempInt2), ",", false);
                     final double[] values = new double[stringTokenizer.countTokens()];
                     int i = 0;

                     while(stringTokenizer.hasMoreTokens())
                     {
                        values[i++] = this.computeValue(context, stringTokenizer.nextToken());
                     }

                     this.parameters.push(context);
                     context = new HashMap<String, Double>();

                     tempInt1 = index - 1;
                     tempString2 = line;

                     line = this.lines.elementAt(ind);
                     i = line.indexOf('(');
                     stringTokenizer = new StringTokenizer(line.substring(i + 1, line.length() - 1), ",", false);

                     if(stringTokenizer.countTokens() != values.length)
                     {
                        Debug.println(DebugLevel.ERROR, "Wrong number of parameters on ", tempInt1, " : ", tempString2);
                        Debug.println(DebugLevel.ERROR, "The function declaration on ", ind, " is ", line);
                     }
                     else
                     {
                        i = 0;

                        while(stringTokenizer.hasMoreTokens())
                        {
                           context.put(stringTokenizer.nextToken().trim(), values[i++]);
                        }

                        stack.push(index);

                        index = ind + 2;

                        mode = Mode.CALL_PROGRAM;
                     }
                  }
               }
            }
         }
         else
         {
            if(line.equals("{"))
            {
               countProg++;
            }
            else if(line.equals("}"))
            {
               if(countProg <= 0)
               {
                  mode = Mode.NORMAL;
               }
               else
               {
                  countProg--;
               }
            }
         }

         if((mode != Mode.REGISTER_PROGRAM) && (turtleInstruction))
         {
            this.logoGraphics.refresh();

            if(this.turtle.speed > 0)
            {
               synchronized(this.lines)
               {
                  this.waiting = true;
                  try
                  {
                     this.lines.wait(this.turtle.speed);
                  }
                  catch(final Exception ignored)
                  {
                  }
                  this.waiting = false;
               }
            }
         }

         if((index >= this.lines.size()) && (!this.finish))
         {
            this.logoGraphics.refresh();

            if(this.languageSpy != null)
            {
               this.languageSpy.finished();
            }

            synchronized(this.lines)
            {
               this.waiting = true;
               try
               {
                  this.lines.wait();
               }
               catch(final Exception ignored)
               {
               }
               this.waiting = false;
            }
         }
      }

      this.threadExecute = null;
   }

   /**
    * Call by JVM when object is destroy
    * 
    * @throws Throwable
    *            On issue
    * @see java.lang.Object#finalize()
    */
   @Override
   protected void finalize() throws Throwable
   {
      this.terminate();

      super.finalize();
   }

   /**
    * Add block of instruction to current script
    * 
    * @param blocks
    *           Blocks to add
    */
   public void appendBlocks(final String... blocks)
   {
      if(this.finish)
      {
         return;
      }

      for(final String block : blocks)
      {
         this.cutInLine(block);
      }

      if(this.threadExecute == null)
      {
         this.threadExecute = new ThreadExecute();
         this.threadExecute.start();
      }
      else
      {
         synchronized(this.lines)
         {
            if(this.waiting)
            {
               this.lines.notify();
            }
         }
      }
   }

   /**
    * Return directoryWork
    * 
    * @return directoryWork
    */
   public File getDirectoryWork()
   {
      return this.directoryWork;
   }

   /**
    * Current spy/listener of execution events
    * 
    * @return Current spy/listener of execution events
    */
   public LanguageSpy getLanguageSpy()
   {
      return this.languageSpy;
   }

   /**
    * Clear the screen and clear the script
    */
   public void raz()
   {
      this.terminate();

      this.logoGraphics.clear(0xFFFFFFFF);
      this.turtle.reset();
      this.logoGraphics.refresh();

      this.parameters.clear();
      this.parameters.push(new HashMap<String, Double>());

      this.programs.clear();

      while(this.threadExecute != null)
      {
         Utilities.sleep(123);
      }

      this.finish = false;
   }

   /**
    * Modify directoryWork
    * 
    * @param directoryWork
    *           New directoryWork value
    */
   public void setDirectoryWork(final File directoryWork)
   {
      this.directoryWork = directoryWork;
   }

   /**
    * Change/define spy/listener of execution events<br>
    * Can use {@code null} to remove current
    * 
    * @param languageSpy
    *           New spy/listener of execution events OR {@code null} to remove current
    */
   public void setLanguageSpy(final LanguageSpy languageSpy)
   {
      this.languageSpy = languageSpy;
   }

   /**
    * Force to terminate all
    */
   public void terminate()
   {
      this.finish = true;

      synchronized(this.lines)
      {
         if(this.waiting)
         {
            this.lines.notify();
         }
      }

      this.lines.clear();
   }
}