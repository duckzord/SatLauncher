package Interfaces;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import Main.Main;
import Variables.VarChecker;

public class SimSetup extends JFrame implements ItemListener, ActionListener {

   private static final long serialVersionUID = 1L;
   /*
    * Create all the text fields, labels, check boxes etc, that are needed
    * to make the options window work correctly
    */
   JPanel row1 = new JPanel();
   JCheckBox satnames = new JCheckBox("Satellite names (on/off)", false);
   JCheckBox plotlines = new JCheckBox("Show satellite paths", false);
   JCheckBox problines = new JCheckBox("Extended collision probability paths", false);
   JCheckBox axislines = new JCheckBox("Turn on axes", false);

   JPanel row2 = new JPanel();

   JPanel labels = new JPanel();
   JLabel timeinclab = new JLabel("Time to increment by:   ");
   JLabel timejumplab = new JLabel("Jump to a time:    ");

   JPanel fields = new JPanel();
   JTextField timeincfield = new JTextField("" + Main.timeinc, 3);
   JTextField timejumpfield = new JTextField("" + Main.t, 3);

   JPanel units = new JPanel();
   JLabel timeincunit = new JLabel("seconds");
   JLabel timejumpunit = new JLabel("seconds");

   /*
    * ==========================================================
    */
   JPanel row3 = new JPanel();

   JPanel logs = new JPanel();
   JLabel logslabel = new JLabel("Turn logs on:");

   JPanel logdata = new JPanel();

   JPanel logboxes = new JPanel();
   JCheckBox closelimit = new JCheckBox("High Warning Distance: ", false);
   JCheckBox midlimit = new JCheckBox("Medium Warning Distance: ", false);
   JCheckBox allcoord = new JCheckBox("Log all coordinates", false);

   JPanel logfields = new JPanel();
   JTextField closefield = new JTextField("" + Main.warnrange);
   JTextField midfield = new JTextField("" + Main.midrange);

   JPanel logunits = new JPanel();
   JLabel closeunits = new JLabel("metres");
   JLabel midunits = new JLabel("metres");
   /*
    * ===========================================================
    */

   JPanel launcher = new JPanel();
   JPanel launchtitle = new JPanel();
   JPanel launchfield = new JPanel();
   JPanel launchunits = new JPanel();

   JLabel radTitle = new JLabel("Launcher Radius:");
   JTextField radField = new JTextField("" + Main.launch_rad, 3);
   JLabel radUnits = new JLabel("m");

   JLabel heiTitle = new JLabel("Launcher Height:");
   JTextField heiField = new JTextField("" + Main.launch_height, 3);
   JLabel heiUnits = new JLabel("m");

   JLabel altTitle = new JLabel("Launcher Altitude:");
   JTextField altField = new JTextField("" + Main.launch_alt, 3);
   JLabel altUnits = new JLabel("km");

   /*
    * =============================================================
    */
   JPanel row4 = new JPanel();
   JButton apply = new JButton("Apply");


   public SimSetup() {
      super("Simulation Setup");
      setSize(300, 500);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      Container pane = getContentPane();

      GridLayout layout = new GridLayout(4, 0, 0, 0);
      pane.setLayout(layout);

      /*
       * Set up the two check boxes that deal with turning on and off
       * the satellite names and satellite paths.
       */
      FlowLayout layout1 = new FlowLayout(FlowLayout.LEFT);
      row1.setLayout(layout1);
      row1.add(satnames);
      row1.add(plotlines);
      row1.add(problines);
      row1.add(axislines);

      // Decide whether the check boxes should be checked already.
      if (Main.names) {
         satnames.setSelected(true);
      }
      if (Main.plotline) {
         plotlines.setSelected(true);
      }
      if (Main.problines) {
         problines.setSelected(true);
      }
      if (Main.axislines) {
         axislines.setSelected(true);
      }

      satnames.addItemListener(this);
      plotlines.addItemListener(this);
      problines.addItemListener(this);
      axislines.addItemListener(this);

      pane.add(row1);

      /*
       * Set up the check boxes and text fields needed to turn on/off
       * the logs, and customise how they are used.
       */
      GridLayout layout3 = new GridLayout(3, 0, 0, -80);
      logs.setLayout(layout3);
      logboxes.add(logslabel, BorderLayout.NORTH);

      /*
       * Add item listeners to the check boxes
       */
      closelimit.addItemListener(this);
      midlimit.addItemListener(this);

      GridLayout boxeslayout = new GridLayout(0, 1);
      logboxes.setLayout(boxeslayout);
      logboxes.add(closelimit);
      logboxes.add(midlimit);
      logboxes.add(allcoord);
      allcoord.setEnabled(false);
      logdata.add(logboxes, BorderLayout.NORTH);

      GridLayout logfieldslayout = new GridLayout(0, 1);
      logfields.setLayout(logfieldslayout);
      closefield.setEnabled(false);
      logfields.add(closefield);
      midfield.setEnabled(false);
      logfields.add(midfield);
      logdata.add(logfields, BorderLayout.NORTH);

      GridLayout logunitslayout = new GridLayout(0, 1);
      logunits.setLayout(logunitslayout);
      closeunits.setEnabled(false);
      logunits.add(closeunits);
      midunits.setEnabled(false);
      logunits.add(midunits);
      logdata.add(logunits, BorderLayout.NORTH);

      logs.add(logdata);

      row2.add(logs);
      pane.add(row2);

      /*
       * Text fields needed to set up here to allow the user to choose
       * how much the time is incremented by.
       */
      GridLayout labellayout = new GridLayout(0, 1);
      labels.setLayout(labellayout);
      labels.add(timeinclab);
      labels.add(timejumplab);
      row3.add(labels, BorderLayout.PAGE_START);

      GridLayout fieldlayout = new GridLayout(0, 1);
      fields.setLayout(fieldlayout);
      fields.add(timeincfield);
      fields.add(timejumpfield);
      row3.add(fields, BorderLayout.WEST);

      GridLayout unitslayout = new GridLayout(0, 1);
      units.setLayout(unitslayout);
      units.add(timeincunit);
      units.add(timejumpunit);
      row3.add(units, BorderLayout.WEST);

      GridLayout launchlayout = new GridLayout(0, 1);
      GridLayout launchlayout1 = new GridLayout(0, 1, 3, 3);
      launchtitle.setLayout(launchlayout1);
      launchfield.setLayout(launchlayout);
      launchunits.setLayout(launchlayout1);

      launchtitle.add(radTitle);
      launchtitle.add(heiTitle);
      launchtitle.add(altTitle);
      launcher.add(launchtitle, BorderLayout.WEST);

      launchfield.add(radField);
      launchfield.add(heiField);
      launchfield.add(altField);
      launcher.add(launchfield, BorderLayout.WEST);

      launchunits.add(radUnits);
      launchunits.add(heiUnits);
      launchunits.add(altUnits);
      launcher.add(launchunits, BorderLayout.WEST);

      if (Main.varset) {
         radField.setEnabled(false);
         heiField.setEnabled(false);
         altField.setEnabled(false);
      }

      pane.add(launcher);

      row4.add(apply);
      apply.addActionListener(this);
      pane.add(row4);

      setContentPane(pane);
      setVisible(true);
   }

   public void itemStateChanged(ItemEvent ievent) {

      Object item = ievent.getItem();

      /*
       * When the satellite names check box changes state, either turn
       * on or off the satellite names inside the simulation.
       */
      if (item == satnames) {
         if (satnames.isSelected()) {
            Main.names = true;
         } else {
            Main.names = false;
         }
      }

      /*
       * When the satellite paths check box changes state, either turn
       * on or off the satellite paths in the simulation.
       */
      if (item == plotlines) {
         if (plotlines.isSelected()) {
            Main.plotline = true;
         } else {
            Main.plotline = false;
         }
      }
      /*
       * Turn on or off the extended collision paths in the main simulator.
       */
      if (item == problines) {
         if (problines.isSelected()) {
            Main.problines = true;
         } else {
            Main.problines = false;
         }
      }
      /*
       * Turn on the axes in the simulator. This makes the motion of the satellites
       * easier to follow
       */
      if (item == axislines) {
         if (axislines.isSelected()) {
            Main.axislines = true;
         } else {
            Main.axislines = false;
         }
      }
      /*
       * Set whether the simulator should turn on the warning logs. Also allows the
       * user to then set the range the warnings are given.
       */
      if (item == closelimit) {
         if (closelimit.isSelected()) {
            closeLogs(true);
            Main.warnlogset = true;
         } else {
            closeLogs(false);
            Main.warnlogset = false;
         }
      }
      /*
       * Set whether medium warning level logs should be turned on. Also allows the
       * user to set the range these logs are triggered.
       */
      if (item == midlimit) {
         if (midlimit.isSelected()) {
            midLogs(true);
            Main.midlogset = true;
         } else {
            midLogs(false);
            Main.midlogset = false;
         }
      }
   }

   public void actionPerformed(ActionEvent act) {

      Object cmd = act.getSource();

      /*
       * When apply is clicked, all the fields in the options need to be
       * checked if they have changed. If they have, checks need to be done
       * and if they pass the value requested can be changed.
       */
      if (cmd == apply) {

         /*
          * This is how much the time is incremented by. If it is not equal
          * to the existing value then it is first checked if it valid using
          * "checkVars". If the check passes the value in "main" is changed,
          * and a confirmation message is given.
          */
         if (Double.parseDouble(timeincfield.getText()) != Main.timeinc) {
            if (VarChecker.checkVars(timeincfield, "Time to increment by")) {
               Main.timeinc = Double.parseDouble(timeincfield.getText());
               System.out.println("\"Time to increment by\" value changed!");
            }
         }
         /*
          * This looks at whether the user wants to change the range at which
          * warnings are written into the log file.
          */
         if (closelimit.isSelected() &&
             !(closefield.getText().trim().matches(("" + (Main.warnrange)).trim()))) {
            if (VarChecker.checkVars(closefield, "\"Warning Range\"")) {
               Main.warnrange = Double.parseDouble(closefield.getText());
               System.out.println("\"Warning range\" has been changed");
            } else {
               closefield.setText("" + Main.warnrange);
            }
         }
         /*
          * This looks at whether the user wants to change the range at which
          * medium warnings are written into the log file.
          */
         if (midlimit.isSelected() &&
             !(midfield.getText().trim().matches(("" + (Main.midrange)).trim()))) {
            if (VarChecker.checkVars(midfield, "\"Medium Range\"")) {
               Main.midrange = Double.parseDouble(midfield.getText());
               System.out.println("\"Medium range\" has been changed");
            } else {
               midfield.setText("" + Main.midrange);
            }
         }
         if (!Main.varset) {
            if (!radField.getText().trim().matches(("" + Main.launch_rad).trim())) {
               if (VarChecker.checkVars(radField, "\"Launcher Radius\"")) {
                  Main.launch_rad = (float) Double.parseDouble(radField.getText());
                  System.out.println("\"Launcher radius\" has been changed");
               } else {
                  radField.setText("" + Main.launch_rad);
               }
            }
         } else {
            System.out.println("Variables set, can't change launcher geometry");
            radField.setText("" + Main.launch_rad);
         }

         if (!Main.varset) {
            if (!heiField.getText().trim().matches(("" + Main.launch_height).trim())) {
               if (VarChecker.checkVars(heiField, "\"Launcher height\"")) {
                  Main.launch_height = (float) Double.parseDouble(heiField.getText());
                  System.out.println("\"Launcher height\" has been changed");
               } else {
                  heiField.setText("" + Main.launch_height);
               }
            }
         } else {
            System.out.println("Variables set, can't change launcher geometry");
            heiField.setText("" + Main.launch_height);
         }

         if (!Main.varset) {
            if (!altField.getText().trim().matches(("" + Main.launch_alt).trim())) {
               if (VarChecker.checkVars(altField, "\"Launcher altitude\"")) {
                  Main.launch_alt = (float) Double.parseDouble(altField.getText());
                  System.out.println("\"Launcher altitude\" has been changed");
               } else {
                  altField.setText("" + Main.launch_alt);
               }
            }
         } else {
            System.out.println("Variables set, can't change launcher geometry");
            altField.setText("" + Main.launch_alt);
         }

      }

   }

   public void closeLogs(boolean b) {
      closefield.setEnabled(b);
      closeunits.setEnabled(b);
   }

   public void midLogs(boolean b) {
      midfield.setEnabled(b);
      midunits.setEnabled(b);
   }
}