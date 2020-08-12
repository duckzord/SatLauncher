
package Interfaces;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

import javax.swing.*;
import com.jogamp.newt.event.*;
import com.jogamp.newt.opengl.GLWindow;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.glsl.ShaderCode;
import com.jogamp.opengl.util.glsl.ShaderProgram;
import Main.Main;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL.GL_FLOAT;
import static com.jogamp.opengl.GL.GL_TRIANGLES;
import static com.jogamp.opengl.GL.GL_UNSIGNED_SHORT;
import static com.jogamp.opengl.GL2ES2.GL_DEBUG_SEVERITY_HIGH;
import static com.jogamp.opengl.GL2ES2.GL_DEBUG_SEVERITY_MEDIUM;
import static com.jogamp.opengl.GL2ES3.*;
import static com.jogamp.opengl.GL2ES3.GL_UNIFORM_BUFFER;
import static com.jogamp.opengl.GL4.GL_MAP_COHERENT_BIT;
import static com.jogamp.opengl.GL4.GL_MAP_PERSISTENT_BIT;

public class MainWindow extends JFrame implements ActionListener {

   private static final long serialVersionUID = 1L;
   protected static double[][] loadedvars = new double[100][9];
   public static SatDisplay satinfo;
   JFileChooser fc = new JFileChooser();
   FileReader filereader = new FileReader();

   JMenuBar menubar = new JMenuBar();
   JMenu newsim = new JMenu("New Simulation");
   JMenu simcalc = new JMenu("Calculations");
   JMenu options = new JMenu("Options");
   JMenuItem mansetup = new JMenuItem("Manual Setup");
   JMenuItem loadsetup = new JMenuItem("Load Setup");
   JMenuItem savesetup = new JMenuItem("Save Current Setup");
   JMenuItem probcalc = new JMenuItem("Calculate Probabilities");
   JMenuItem siminfo = new JMenuItem("Simulation Information");
   JMenuItem simsetup = new JMenuItem("Simulation Setup");

   JPanel row = new JPanel();
   JPanel timepanel = new JPanel();
   JLabel timelabel = new JLabel("Time:");
   public JLabel time = new JLabel("" + Main.t);
   JLabel timeunits = new JLabel("seconds");

   JPanel timebuttons = new JPanel();
   public JButton startsim = new JButton("Start");
   public JButton pausesim = new JButton("Pause");
   JLabel collision = new JLabel();

   GLProfile glProfile = GLProfile.get(GLProfile.GL2);
   GLCapabilities caps = new GLCapabilities(glProfile);
   GLJPanel canvas = new GLJPanel(caps);
   // Constructs animator with initial thing to draw (canvas) and the frames per second
   FPSAnimator animator = new FPSAnimator(canvas, 50);


   public MainWindow(String name, Main program) {

      Container pane = getContentPane();
      // Constructs animator with initial thing to draw (canvas) and the frames per second
      caps.setDoubleBuffered(true);// request double buffer display mode
      canvas.addGLEventListener(program); // Listener for the GL drawing commands
      canvas.addKeyListener(program); // Listener for keyboard inputs
      JFrame.setDefaultLookAndFeelDecorated(false);
      JFrame frame = new JFrame(name);

      frame.setSize(500, 500);
      frame.setLocationRelativeTo(null);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // EXIT_ON_CLOSE closes all open windows.

      BorderLayout layout = new BorderLayout();
      pane.setLayout(layout);

      // Set up a menu to contain a button to access the GUI
      mansetup.addActionListener(this);
      simsetup.addActionListener(this);
      loadsetup.addActionListener(this);
      savesetup.addActionListener(this);
      probcalc.addActionListener(this);
      siminfo.addActionListener(this);

      menubar.setBackground(Color.lightGray);

      mansetup.setFont(new Font("sanserif", Font.PLAIN, 10));
      loadsetup.setFont(new Font("sanserif", Font.PLAIN, 10));
      savesetup.setFont(new Font("sanserif", Font.PLAIN, 10));
      newsim.add(mansetup);
      newsim.add(loadsetup);
      newsim.add(savesetup);

      probcalc.setFont(new Font("sanserif", Font.PLAIN, 10));
      simcalc.add(probcalc);

      siminfo.setFont(new Font("sanserif", Font.PLAIN, 10));
      simsetup.setFont(new Font("sanserif", Font.PLAIN, 10));
      options.add(siminfo);
      options.add(simsetup);

      newsim.setFont(new Font("sanserif", Font.PLAIN, 10));
      simcalc.setFont(new Font("sanserif", Font.PLAIN, 10));
      options.setFont(new Font("sanserif", Font.PLAIN, 10));
      menubar.add(newsim);
      menubar.add(simcalc);
      menubar.add(options);

      time.setForeground(Color.green);
      timelabel.setForeground(Color.green);
      timeunits.setForeground(Color.green);

      BorderLayout botlayout = new BorderLayout();
      row.setLayout(botlayout);
      row.setBackground(Color.black);

      timepanel.setBackground(Color.black);
      timepanel.add(timelabel);
      timepanel.add(time);
      timepanel.add(timeunits);

      row.add(timepanel, BorderLayout.EAST);

      timebuttons.setBackground(Color.black);

      row.add(collision, BorderLayout.CENTER);

      startsim.setEnabled(false);
      pausesim.setEnabled(false);

      startsim.addActionListener(this);
      pausesim.addActionListener(this);

      timebuttons.add(startsim);
      timebuttons.add(pausesim);

      row.add(timebuttons, BorderLayout.WEST);

      pane.add(menubar, BorderLayout.NORTH);
      pane.add(canvas, BorderLayout.CENTER);
      pane.add(row, BorderLayout.SOUTH);

      frame.setContentPane(pane);
      frame.setVisible(true);
      canvas.requestFocusInWindow();
      animator.start();
   }

   /*
    * This function checks which button has been pressed, and then what action should be performed.
    */
   public void actionPerformed(ActionEvent event) {

      Object button = event.getSource();

      if (button == mansetup) {
         if (!Main.varset) {
            new SatSetup(false); // Creates a new interface window.
            canvas.requestFocusInWindow(); // This called so that the keyboard functions still work
            // in the main window
         } else {
            new SatSetup(true);
            canvas.requestFocusInWindow();
         }
      }
      /*
       * Open a file chooser box, and let the user select a file. If
       * a file is chosen, get the file, and then send it to
       * getVariables(), where it is determined whether the file is
       * readable or not. If so, then the variables are extracted,
       * returned here, where they are then sent to the main file to
       * plot the satellites.
       */
      if (button == loadsetup) {
         int returnVal = fc.showOpenDialog(MainWindow.this);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            loadedvars = filereader.getVariables(file);
            System.out.println(filereader.satnames[0]);
            SatSetup.addLoadedSats(loadedvars);

         }
      }

      if (button == savesetup) {
         int returnVal = fc.showSaveDialog(MainWindow.this);
         if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            Main.collectSimVars();
            new FileSave(file, SatSetup.Vars, Main.simvars, SatSetup.num);
            System.out.println(file);
         }
      }

      if (button == probcalc) {
         new CalcProbs(SatSetup.num);
      }
      if (button == simsetup) {
         new SimSetup();
         canvas.requestFocusInWindow();
      }
      if (button == siminfo) {
         satinfo = new SatDisplay();
         canvas.requestFocusInWindow();
      }
      if (button == startsim) {
         timestart();
         canvas.requestFocusInWindow();
      }
      if (button == pausesim) {
         timepause();
         canvas.requestFocusInWindow();
      }
   }

   public void timestart() {
      startsim.setEnabled(false);
      pausesim.setEnabled(true);
      collision.setForeground(Color.black);
      Main.motion = true;
   }

   public void timepause() {
      startsim.setEnabled(true);
      pausesim.setEnabled(false);
      Main.motion = false;
   }

   public void satCollided(boolean collided, int sat1, int sat2) {
      if (collided) {
         timepause();
         collision.setText("Satellites " + sat1 + " and " + sat2 + " have collided!");
         collision.setForeground(Color.red);
      }
   }

}
