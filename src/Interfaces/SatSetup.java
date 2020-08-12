package Interfaces;

import java.awt.*;
import java.awt.event.*;
import java.text.DecimalFormat;

import javax.swing.*;
import Variables.VarChecker;
import Main.Main;

/*
 * This class creates a new window, in which users can enter the number of satellites they want
 * to simulate, and their initial conditions. From here, there a numerous options to alter how
 * the satellites are viewed, and how the information about them are logged.
 */

public class SatSetup extends JFrame implements ActionListener, ItemListener {

   private static final long serialVersionUID = 1L;
   private int entered = 0;
   protected static int num = 0;
   private static boolean resetable = false, mansetup = false;
   protected static double[][] Vars = new double[100][7];
   protected static double[][] loadedvars = new double[100][9];
   protected static String eqtype;


   /*
    * Below creates 'row 1' of the window, which contains a text box to enter the number
    * of satellites, and two check boxes to choose the type of initial conditions.
    */
   JPanel row1 = new JPanel();
   ButtonGroup option = new ButtonGroup();
   JLabel numsatsLabel = new JLabel("Number of Satellites: ", JLabel.RIGHT);
   static JTextField numsatField = new JTextField(3); // 3 is the size of the box, not maximum that can be entered.
   JPanel boxes = new JPanel();
   static JCheckBox epi = new JCheckBox("Epicycle", false); // Both check boxes are set to unselected (false).
   static JCheckBox indav = new JCheckBox("Initial Dist & Vel", false);

   /*
    * Row two is set up, inside this row the text boxes are created for users to enter
    * the initial conditions.
    */
   JPanel row2 = new JPanel();
   JPanel indavrow = new JPanel();
   JPanel indavtitle = new JPanel();
   JPanel indavfield = new JPanel();
   JPanel indavunits = new JPanel();
   JLabel thetaLabel = new JLabel("theta:", JLabel.RIGHT);
   static JTextField thetaField = new JTextField(5);
   JLabel thetaUnits = new JLabel("deg", JLabel.LEFT);

   JLabel zLabel = new JLabel("z pos:", JLabel.RIGHT);
   static JTextField zField = new JTextField(5);
   JLabel zUnits = new JLabel("m", JLabel.LEFT);

   JLabel x1Label = new JLabel("x vel:", JLabel.RIGHT);
   static JTextField x1Field = new JTextField(5);
   JLabel x1Units = new JLabel("m/s", JLabel.LEFT);

   JLabel y1Label = new JLabel("y vel:", JLabel.RIGHT);
   static JTextField y1Field = new JTextField(5);
   JLabel y1Units = new JLabel("m/s", JLabel.LEFT);

   JLabel z1Label = new JLabel("z vel:", JLabel.RIGHT);
   static JTextField z1Field = new JTextField(5);
   JLabel z1Units = new JLabel("m/s", JLabel.LEFT);

   JPanel epirow = new JPanel();
   JPanel epititle = new JPanel();
   JPanel epifield = new JPanel();
   JPanel epiunits = new JPanel();

   JLabel ALabel = new JLabel("A:", JLabel.RIGHT);
   static JTextField AField = new JTextField(5);
   JLabel AUnits = new JLabel("m", JLabel.LEFT);

   JLabel bLabel = new JLabel("b:", JLabel.RIGHT);
   static JTextField bField = new JTextField(5);
   JLabel bUnits = new JLabel("m", JLabel.LEFT);

   JLabel CLabel = new JLabel("C:", JLabel.RIGHT);
   static JTextField CField = new JTextField(5);
   JLabel CUnits = new JLabel("m", JLabel.LEFT);

   JLabel deltaLabel = new JLabel("delta:", JLabel.RIGHT);
   static JTextField deltaField = new JTextField(5);
   JLabel deltaUnits = new JLabel("rads", JLabel.LEFT);

   JLabel eLabel = new JLabel("e:", JLabel.RIGHT);
   static JTextField eField = new JTextField(5);
   JLabel eUnits = new JLabel("rads", JLabel.LEFT);

   JLabel wLabel = new JLabel("w:", JLabel.RIGHT);
   static JTextField wField = new JTextField(5);
   JLabel wUnits = new JLabel("m", JLabel.LEFT);

   JPanel row3 = new JPanel();
   JLabel timeLabel = new JLabel("Time to separate (seconds):", JLabel.RIGHT);
   static JTextField timeField = new JTextField(5);
   static JComboBox satpicker = new JComboBox();
   JButton reset = new JButton("Reset Conditions");
   static JButton addsat = new JButton("Add Satellite");

   public SatSetup(boolean varset) {

      // The actual window is created here.
      super("Satellite Options");
      setSize(320, 450);
      // DISPOSE_ON_CLOSE means only this window is closed, not the whole program.
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      // This GridLayout means that there are 3 columns and 1 row, separated by 10 pixels horizontally and vertically.
      GridLayout layout = new GridLayout(3, 1, 10, 0);
      Container pane = getContentPane();
      pane.setLayout(layout);

      /*
       * To determine if a button or check box is being pressed you need to add them to an
       * appropriate listener.
       */
      addsat.addActionListener(this);
      epi.addItemListener(this);
      indav.addItemListener(this);
      satpicker.addActionListener(this);
      reset.addActionListener(this);

      FlowLayout layout1 = new FlowLayout(FlowLayout.CENTER, 10, 22);
      option.add(epi);
      option.add(indav);
      row1.setLayout(layout1);
      row1.add(numsatsLabel);
      row1.add(numsatField);
      boxes.add(indav);
      boxes.add(epi);
      row1.add(boxes);
      //row1.setBorder(BorderFactory.createLineBorder(Color.black));
      pane.add(row1);

      GridLayout layout2 = new GridLayout(0, 2, 3, 3);
      row2.setLayout(layout2);

      FlowLayout indavoverall = new FlowLayout(FlowLayout.LEFT);
      indavrow.setLayout(indavoverall);
      GridLayout indavlayout = new GridLayout(0, 1);
      GridLayout indavlayout1 = new GridLayout(0, 1, 3, 3);
      indavtitle.setLayout(indavlayout1);
      indavfield.setLayout(indavlayout);
      indavunits.setLayout(indavlayout1);

      // Each element is added to the row, and all the text boxes are set so you can't edit them
      setIndavSelectable(false);

      indavtitle.add(thetaLabel);
      indavfield.add(thetaField);
      indavunits.add(thetaUnits);

      indavtitle.add(zLabel);
      indavfield.add(zField);
      indavunits.add(zUnits);

      indavtitle.add(x1Label);
      indavfield.add(x1Field);
      indavunits.add(x1Units);

      indavtitle.add(y1Label);
      indavfield.add(y1Field);
      indavunits.add(y1Units);

      indavtitle.add(z1Label);
      indavfield.add(z1Field);
      indavunits.add(z1Units);

      indavrow.add(indavtitle, BorderLayout.NORTH);
      indavrow.add(indavfield, BorderLayout.NORTH);
      indavrow.add(indavunits, BorderLayout.NORTH);

      row2.add(indavrow);

      FlowLayout epioverall = new FlowLayout(FlowLayout.LEFT);
      epirow.setLayout(epioverall);
      GridLayout epilayout = new GridLayout(0, 1);
      GridLayout epilayout1 = new GridLayout(0, 1, 3, 3);
      epititle.setLayout(epilayout1);
      epifield.setLayout(epilayout);
      epiunits.setLayout(epilayout1);

      setEpiSelectable(false);
      epititle.add(ALabel);
      epifield.add(AField);
      epiunits.add(AUnits);

      epititle.add(bLabel);
      epifield.add(bField);
      epiunits.add(bUnits);

      epititle.add(CLabel);
      epifield.add(CField);
      epiunits.add(CUnits);

      epititle.add(deltaLabel);
      epifield.add(deltaField);
      epiunits.add(deltaUnits);

      epititle.add(eLabel);
      epifield.add(eField);
      epiunits.add(eUnits);

      epititle.add(wLabel);
      epifield.add(wField);
      epiunits.add(wUnits);

      epirow.add(epititle, BorderLayout.NORTH);
      epirow.add(epifield, BorderLayout.NORTH);
      epirow.add(epiunits, BorderLayout.NORTH);
      row2.add(epirow);
      //row2.setBorder(BorderFactory.createLineBorder(Color.black));
      pane.add(row2);

      FlowLayout layout3 = new FlowLayout(FlowLayout.CENTER, 20, 22);
      row3.setLayout(layout3);
      row3.add(timeLabel);
      row3.add(timeField);
      satpicker.setEnabled(false);
      row3.add(satpicker);
      if (!Main.varset) {
         satpicker.addItem("Choose a Satellite");
         mansetup = true;
      }
      row3.add(reset);
      timeField.setEnabled(false);
      reset.setEnabled(false);
      //row3.setBorder(BorderFactory.createLineBorder(Color.black));
      addsat.setEnabled(false);
      row3.add(addsat);
      pane.add(row3);

      // If the variables have been set already, all the text boxes become uneditable.
      if (varset == true) {
         variableSet();
      }
      setContentPane(pane);
      setVisible(true);
   }


   /*
    * The actionPerformed function is used for determining whether a button is being pressed,
    * and if it is, what action is then used.
    */
   public void actionPerformed(ActionEvent event) {

      Object cmd = event.getSource();
      double elements[];
      double conditions[];
      DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(6);

      if (cmd == satpicker && Main.varset) {
         if (!(epi.isSelected()) && !(indav.isSelected())) {
            epi.setEnabled(true);
            indav.setEnabled(true);
            numsatField.setText("" + num);
         }
         int index = satpicker.getSelectedIndex() - 1;
         if (index != -1) {
            if (indav.isSelected()) {
               setIndavSelectable(true);
               conditions = Main.satellite[index].getInitialConditions("Indav");
               thetaField.setText("" + df.format(conditions[0]));
               zField.setText("" + df.format(conditions[1]));
               x1Field.setText("" + df.format(conditions[2]));
               y1Field.setText("" + df.format(conditions[3]));
               z1Field.setText("" + df.format(conditions[4]));
               timeField.setText("" + df.format(conditions[5]));
               elements = Main.satellite[index].ConvertToEpi();
               setEpiSelectable(false);
               AField.setText("" + df.format(elements[0]));
               bField.setText("" + df.format(elements[1]));
               CField.setText("" + df.format(elements[2]));
               deltaField.setText("" + df.format(elements[3]));
               eField.setText("" + df.format(elements[4]));
               wField.setText("" + df.format(elements[5]));

               if (!Main.motion) {
                  reset.setEnabled(true);
                  setIndavSelectable(true);
                  timeField.setEnabled(true);
               }
            } else if (epi.isSelected() && index >= 0) {
               setEpiSelectable(true);
               AField.setText("" + df.format(Vars[index][0]));
               bField.setText("" + df.format(Vars[index][1]));
               CField.setText("" + df.format(Vars[index][2]));
               deltaField.setText("" + df.format(Vars[index][3]));
               eField.setText("" + df.format(Vars[index][4]));
               wField.setText("" + df.format(Vars[index][5]));
               timeField.setText("" + df.format(Vars[index][6]));
					/*
					 * Can't do this yet because lots more conversion!
					elements = Main.satellite[index].ConvertToIndav();
					setIndavSelectable(true);
					thetaField.setText("" + elements[0]);
					zField.setText("" + elements[2]);
					x1Field.setText("" + elements[1]);
					y1Field.setText("" + elements[3]);
					z1Field.setText("" + elements[5]);
					*/

               if (!Main.motion) {
                  reset.setEnabled(true);
                  timeField.setEnabled(true);
               }
            }
         }
      }
      if (cmd == reset) {
         resetConditions();
      }
      if (cmd == addsat) {
         if (VarChecker.checkVars(numsatField, "Number of Satellites") &&
             VarChecker.checkVars(timeField, "Time")) {
            {
               if (epi.isSelected()) {
                  if (checkConditions("Epi")) {
                     addSatellite();
                  }
               } else if (indav.isSelected()) {
                  if (checkConditions("Indav")) {
                     addSatellite();
                  }
               }
            }
         }
      }
   }

   /*
    * The itemStateChanged function determines whether a check box's status has changed.
    * If so, then the relevant action is performed.
    */
   public void itemStateChanged(ItemEvent ievent) {

      Object item = ievent.getItem();

      if (item == epi) {
         setEpiSelectable(true);
         setIndavSelectable(false);
         if (!Main.varset) {
            addsat.setEnabled(true);
         }
         timeField.setEnabled(true);
      }
      if (item == indav) {
         setEpiSelectable(false);
         setIndavSelectable(true);
         if (!Main.varset) {
            addsat.setEnabled(true);
         }
         timeField.setEnabled(true);
      }
   }

   /*
    * When the start button is pressed, the motion variable in Main is set to go,
    * then the start button and the text boxes are disabled, and the pause button
    * is enabled.
    */

   /*When the pause button is pressed, the motion variable in Main is set to pause,
    * then the pause button is disabled and the start button is enabled.
    *
    */

   public boolean checkConditions(String condtype) {
      if (condtype == "Indav") {
         if (VarChecker.checkVars(thetaField, "theta") &&
             VarChecker.checkVars(x1Field, "x vel") &&
             VarChecker.checkVars(zField, "z pos") &&
             VarChecker.checkVars(y1Field, "y vel") &&
             VarChecker.checkVars(z1Field, "z vel")) {
            return true;
         } else {
            return false;
         }
      } else if (condtype == "Epi") {
         if (VarChecker.checkVars(AField, "A") &&
             VarChecker.checkVars(bField, "b") &&
             VarChecker.checkVars(CField, "C") &&
             VarChecker.checkVars(deltaField, "delta") &&
             VarChecker.checkVars(eField, "e") &&
             VarChecker.checkVars(wField, "w")) {
            return true;
         } else {
            return false;
         }

      } else {
         return false;
      }
   }

   public void resetConditions() {

      resetable = true;
      Main.t = 0;
      entered = satpicker.getSelectedIndex() - 1;
      if (epi.isSelected()) {
         if (checkConditions("Epi")) {
            addSatellite();
         }
      } else if (indav.isSelected()) {
         if (checkConditions("Indav")) {
            addSatellite();
         }
      } else {
         System.out.println("Please select the conditions you want to reset");
      }

   }

   /*
    * This disables all editable text boxes and enables the satellite chooser.
    */
   public static void variableSet() {
      addsat.setEnabled(false);
      epi.setEnabled(false);
      indav.setEnabled(false);
      numsatField.setEnabled(false);
      timeField.setEnabled(false);
      satpicker.setEnabled(true);
      setIndavSelectable(false);
      setEpiSelectable(false);
   }

   public static void addLoadedSats(double[][] loadedvars) {
      num = (int) loadedvars[0][0];
      double[] globvars = new double[9];
      for (int i = 0; i < num; i++) {

         for (int j = 0; j < 6; j++) {
            Vars[i][j] = loadedvars[i + 1][j];
         }
         if (!resetable) {
            if (i == 0 && !mansetup) {
               satpicker.addItem("Choose a Satellite");
            }
            satpicker.addItem("Satellite " + i);
         }
      }
      for (int j = 0; j < 9; j++) {
         globvars[j] = loadedvars[0][j];
      }
      eqtype = "Indav";
      Main.createVariables(globvars);
      Main.createSats(Vars, num, eqtype);
      variableSet();
      Main.varset = true;
      System.out.println(num + " satellites have been added");
   }

   /*
    * When addSatellite is called, the number entered in the number of satellites is taken
    * and cast from text to integer. This number is used to make sure only the number of
    * satellites chosen is entered. Each text field where a value is entered is then cast
    * from text to double, and entered into the Vars array. Once the values are entered,
    * the text fields are cleared and the it waits for the next satellite's variables. If
    * there are no more satellites that need to be entered then the Vars array is sent to
    * Main in order to draw the satellites.
    */

   public void addSatellite() {

      num = Integer.parseInt(numsatField.getText());
      if (entered < num) {
         if (epi.isSelected()) {
            Vars[entered][0] = Double.parseDouble(AField.getText());
            Vars[entered][1] = Double.parseDouble(bField.getText());
            Vars[entered][2] = Double.parseDouble(CField.getText());
            Vars[entered][3] = Double.parseDouble(deltaField.getText());
            Vars[entered][4] = Double.parseDouble(eField.getText());
            Vars[entered][5] = Double.parseDouble(wField.getText());
            Vars[entered][6] = Double.parseDouble(timeField.getText());
            eqtype = "Epi";
            if (!resetable) {
               System.out.println("Satellite " + entered + " entered!");
               satpicker.addItem("Satellite " + entered);
               entered++;
               clearEpi();
               indav.setEnabled(false);
               numsatField.setEnabled(false);
            } else {
               System.out.println("Satellite " + entered + " resetted!");
               entered = num;
            }
         }
         if (indav.isSelected()) {
            Vars[entered][0] = Double.parseDouble(thetaField.getText());
            Vars[entered][1] = Double.parseDouble(zField.getText());
            Vars[entered][2] = Double.parseDouble(x1Field.getText());
            Vars[entered][3] = Double.parseDouble(y1Field.getText());
            Vars[entered][4] = Double.parseDouble(z1Field.getText());
            Vars[entered][5] = Double.parseDouble(timeField.getText());
            eqtype = "Indav";
            if (!resetable) {
               System.out.println("Satellite " + entered + " entered!");
               satpicker.addItem("Satellite " + entered);
               entered++;
               clearIndav();
               epi.setEnabled(false);
               numsatField.setEnabled(false);
            } else {
               System.out.println("Satellite " + entered + " resetted!");
               entered = num;
            }
         }
      }

      if (entered == num) {
         Main.createSats(Vars, num, eqtype);
         if (!resetable) {
            variableSet();
         }
         Main.varset = true;
      }
   }

   // Choose whether the Epicycle element text boxes are selectable or non-selectable.
   public static void setEpiSelectable(boolean selectable) {
      AField.setEnabled(selectable);
      bField.setEnabled(selectable);
      CField.setEnabled(selectable);
      deltaField.setEnabled(selectable);
      eField.setEnabled(selectable);
      wField.setEnabled(selectable);
   }

   // Choose whether the initial position and velocity text boxes are selectable or non-selectable.
   public static void setIndavSelectable(boolean selectable) {
      thetaField.setEnabled(selectable);
      zField.setEnabled(selectable);
      x1Field.setEnabled(selectable);
      y1Field.setEnabled(selectable);
      z1Field.setEnabled(selectable);
   }

   // Clear all the Epicycle text boxes are cleared.
   public void clearEpi() {
      AField.setText(null);
      bField.setText(null);
      CField.setText(null);
      deltaField.setText(null);
      eField.setText(null);
      wField.setText(null);
      timeField.setText(null);
   }

   // All the initial position and velocity text boxes are cleared.
   public void clearIndav() {
      thetaField.setText(null);
      zField.setText(null);
      x1Field.setText(null);
      y1Field.setText(null);
      z1Field.setText(null);
      timeField.setText(null);
   }


}