package Interfaces;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.logging.Level;

import Main.Main;
import Variables.Probabilities;

import javax.swing.*;
import Variables.VarChecker;

public class CalcProbs extends JFrame implements ActionListener, ItemListener {

   // All the variables for the extended collision propability
   public static double newx, newy, newz, newx1, newy1, newz1, checkrange = 0.0016, pathinc, xlimit1, ylimit1, zlimit1,
       sat1x, sat1y, sat1z, sat2x, sat2y, sat2z, acc = 4, seprange = 2;

   public static boolean checksep = false;
   public static int pathtime = 2000;

   protected int varslength;

   protected static int[] counterarr;
   protected static double[] probarr;
   protected static double[][][] pathcoord;

   JPanel calcpanel = new JPanel();
   protected JButton calc = new JButton("Calculate!");

   JPanel range = new JPanel();
   JLabel rangelabel = new JLabel("Velocity range (+/-):");
   JTextField rangefield = new JTextField("" + checkrange, 5);
   JLabel rangeunits = new JLabel("m/s");

   JPanel sep = new JPanel();
   JCheckBox sepcheck = new JCheckBox();
   JLabel seplabel = new JLabel("Separation time range (+/-):");
   JTextField sepfield = new JTextField("" + seprange);
   JLabel sepunits = new JLabel("seconds");

   JPanel time = new JPanel();
   JLabel timelabel = new JLabel("Collision check run time:");
   JTextField timefield = new JTextField("" + pathtime);
   JLabel timeunits = new JLabel("seconds");

   public CalcProbs(int numsat) {
      super("Probability Calculations");
      setSize(500, 200);
      // DISPOSE_ON_CLOSE means only this window is closed, not the whole program.
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      // This GridLayout means that there are 3 columns and 1 row, separated by 10 pixels horizontally and vertically.
      Container pane = getContentPane();

      GridLayout calclayout = new GridLayout(4, 0);
      calcpanel.setLayout(calclayout);

      range.add(rangelabel);
      range.add(rangefield);
      range.add(rangeunits);

      sep.add(sepcheck);
      sep.add(seplabel);
      sep.add(sepfield);
      sep.add(sepunits);

      sepfield.setEnabled(false);

      time.add(timelabel);
      time.add(timefield);
      time.add(timeunits);

      calc.addActionListener(this);
      sepcheck.addItemListener(this);

      calcpanel.add(range);
      calcpanel.add(sep);
      calcpanel.add(time);
      calcpanel.add(calc);

      pane.add(calcpanel);

      varslength = numsat;

      setContentPane(pane);
      setVisible(true);
   }

   public void actionPerformed(ActionEvent act) {
      Object cmd = act.getSource();

      if (cmd == calc) {
         if (VarChecker.checkVars(timefield, "pathime")) {
            pathtime = (int) Double.parseDouble(timefield.getText());

            if (VarChecker.checkVars(rangefield, "Velocity Range")) {
               checkrange = Double.parseDouble(rangefield.getText());

               if (VarChecker.checkVars(sepfield, "Separation Time range")) {
                  seprange = Double.parseDouble(sepfield.getText());

                  checkProbabilities();
               }
            }
         }
      }
   }

   public void itemStateChanged(ItemEvent item) {
      Object cmd = item.getSource();

      if (cmd == sepcheck) {
         if (sepcheck.isSelected()) {
            sepfield.setEnabled(true);
            checksep = true;
         } else {
            sepfield.setEnabled(false);
            checksep = false;
         }
      }
   }


   public void checkProbabilities() {
      /*
       * Each element of this array counts the number of collisions with every satellite. For
       * example, counterarr[0] will count the collisions with satellite 0, counterarr[1] will
       * count the collisions with satellite 1 etc.
       */
      int arraysize = (int) acc * (int) acc * (int) acc;
      int count = 0;
      boolean arrayset = false;
      double initx, inity, initz, initx1, inity1, initz1,
          startx, starty, startz, startx1, starty1, startz1,
          prob, prob1, newsep = 0, newsep1 = 0, endtime = 0, endtime1 = 0;

      int septime = 0, septime1 = 0;
      pathcoord = new double[varslength][arraysize + 1][4];
      counterarr = new int[varslength];
      probarr = new double[varslength];
      pathinc = (checkrange * 2) / (acc - 1);
      Probabilities probs = new Probabilities(pathinc);
      DecimalFormat df = new DecimalFormat();
      df.setMaximumFractionDigits(16);

      for (int i = 0; i < varslength; i++) {

         Main.satellite[i].Translations(0);
         initx = Main.satellite[i].getx1();
         inity = Main.satellite[i].gety1();
         initz = Main.satellite[i].getz1();

         startx = initx - checkrange;
         starty = inity - checkrange;
         startz = initz - checkrange;
         if (checksep) {
            newsep = Main.satellite[i].getSeptime() - seprange;
            endtime = Main.satellite[i].getSeptime() + seprange;
         } else {
            septime = (int) Main.satellite[i].getSeptime();
         }

         if (!arrayset) {

            newx = startx;
            /*
             * Working the first satellite
             */
            for (int nx = 0; nx < acc; nx++) {

               newy = starty;

               for (int ny = 0; ny < acc; ny++) {

                  newz = startz;

                  for (int nz = 0; nz < acc; nz++) {
                     /*
                      * Working out second satellite to compare against.
                      */
                     for (int k = 0; k < varslength; k++) {

                        Main.satellite[k].Translations(0);
                        initx1 = Main.satellite[k].getx1();
                        inity1 = Main.satellite[k].gety1();
                        initz1 = Main.satellite[k].getz1();

                        startx1 = initx1 - checkrange;
                        starty1 = inity1 - checkrange;
                        startz1 = initz1 - checkrange;
                        if (checksep) {
                           newsep1 = Main.satellite[k].getSeptime() - seprange;
                           endtime1 = Main.satellite[k].getSeptime() + seprange;
                        } else {
                           septime1 = (int) Main.satellite[k].getSeptime();
                        }

                        if (k > i) {

                           if (!arrayset) {

                              newx1 = startx1;

                              for (int nx1 = 0; nx1 < acc; nx1++) {

                                 newy1 = starty1;

                                 for (int ny1 = 0; ny1 < acc; ny1++) {

                                    newz1 = startz1;

                                    for (int nz1 = 0; nz1 < acc; nz1++) {

                                       pathcoord[k][count][0] = newx1;
                                       pathcoord[k][count][1] = newy1;
                                       pathcoord[k][count][2] = newz1;

                                       if (checksep) {
                                          for (int t = (int) newsep; t <= endtime; t++) {
                                             if (t > 0) {
                                                for (int s = (int) newsep1; s <= endtime1; s++) {
                                                   if (s > 0) {
                                                      if (checkProbPaths(newx, newy, newz, newx1, newy1, newz1, i, k, t, s)) {
                                                         prob = probs.calculatePathProb(newx, newy, newz, initx, inity, initz);
                                                         prob1 = probs.calculatePathProb(newx1, newy1, newz1, initx1, inity1, initz1);

                                                         probarr[i] = probarr[i] + (prob * prob1);
                                                         probarr[k] = probarr[k] + (prob * prob1);
                                                      }
                                                   }
                                                }
                                             }
                                          }
                                       } else {
                                          if (checkProbPaths(newx, newy, newz, newx1, newy1, newz1, i, k, septime, septime1)) {
                                             prob = probs.calculatePathProb(newx, newy, newz, initx, inity, initz);
                                             prob1 = probs.calculatePathProb(newx1, newy1, newz1, initx1, inity1, initz1);

                                             probarr[i] = probarr[i] + (prob * prob1);
                                             probarr[k] = probarr[k] + (prob * prob1);
                                          }
                                       }
                                       newz1 += pathinc;
                                    }
                                    newy1 += pathinc;
                                 }
                                 newx1 += pathinc;
                              }
                           } else {
                              for (int arr = 0; arr < arraysize; arr++) {
                                 newx1 = pathcoord[k][arr][0];
                                 newy1 = pathcoord[k][arr][1];
                                 newz1 = pathcoord[k][arr][2];

                                 if (checksep) {
                                    for (int t = (int) newsep; t < endtime; t++) {
                                       if (t > 0) {
                                          for (int s = (int) newsep1; s < endtime1; s++) {
                                             if (s > 0) {
                                                if (checkProbPaths(newx, newy, newz, newx1, newy1, newz1, i, k, t, s)) {
                                                   prob = probs.calculatePathProb(newx, newy, newz, initx, inity, initz);
                                                   prob1 = probs.calculatePathProb(newx1, newy1, newz1, initx1, inity1, initz1);

                                                   probarr[i] = probarr[i] + (prob * prob1);
                                                   probarr[k] = probarr[k] + (prob * prob1);
                                                }
                                             }
                                          }
                                       }
                                    }
                                 } else {
                                    if (checkProbPaths(newx, newy, newz, newx1, newy1, newz1, i, k, septime, septime1)) {
                                       prob = probs.calculatePathProb(newx, newy, newz, initx, inity, initz);
                                       prob1 = probs.calculatePathProb(newx1, newy1, newz1, initx1, inity1, initz1);

                                       probarr[i] = probarr[i] + (prob * prob1);
                                       probarr[k] = probarr[k] + (prob * prob1);
                                    }
                                 }
                              }
                           }
                        }
                     }
                     arrayset = true;

                     newz += pathinc;
                  }
                  newy += pathinc;
               }
               newx += pathinc;
            }
            // Once one satellite has been analysed, the number of collisions recorded are printed to the screen.
            System.out.println("Probability of collision with satellite " + i + ": " + df.format((probarr[i] * 100)) + " %");
            //System.out.println("Number of collision with satellite " +i+ ": " +counterarr[i]);
         } else {
            for (int arr = 0; arr < arraysize; arr++) {

               newx = pathcoord[i][arr][0];
               newy = pathcoord[i][arr][1];
               newz = pathcoord[i][arr][2];

               for (int k = 0; k < varslength; k++) {
                  if (k > i) {
                     Main.satellite[k].Translations(0);
                     initx1 = Main.satellite[k].getx1();
                     inity1 = Main.satellite[k].gety1();
                     initz1 = Main.satellite[k].getz1();
                     if (checksep) {
                        newsep1 = Main.satellite[k].getSeptime() - seprange;
                        endtime1 = Main.satellite[k].getSeptime() + seprange;
                     } else {
                        septime1 = (int) Main.satellite[k].getSeptime();
                     }

                     for (int arr2 = 0; arr2 < arraysize; arr2++) {
                        newx1 = pathcoord[k][arr2][0];
                        newy1 = pathcoord[k][arr2][1];
                        newz1 = pathcoord[k][arr2][2];

                        if (checksep) {
                           for (int t = (int) newsep; t < endtime; t++) {
                              if (t > 0) {
                                 for (int s = (int) newsep1; s < endtime1; s++) {
                                    if (s > 0) {
                                       if (checkProbPaths(newx, newy, newz, newx1, newy1, newz1, i, k, t, s)) {
                                          prob = probs.calculatePathProb(newx, newy, newz, initx, inity, initz);
                                          prob1 = probs.calculatePathProb(newx1, newy1, newz1, initx1, inity1, initz1);

                                          probarr[i] = probarr[i] + (prob * prob1);
                                          probarr[k] = probarr[k] + (prob * prob1);
                                       }
                                    }
                                 }
                              }
                           }
                        } else {
                           if (checkProbPaths(newx, newy, newz, newx1, newy1, newz1, i, k, septime, septime1)) {
                              prob = probs.calculatePathProb(newx, newy, newz, initx, inity, initz);
                              prob1 = probs.calculatePathProb(newx1, newy1, newz1, initx1, inity1, initz1);

                              probarr[i] = probarr[i] + (prob * prob1);
                              probarr[k] = probarr[k] + (prob * prob1);
                           }
                        }
                     }
                  }
               }
            }
            System.out.println("Probability of collision with satellite " + i + ": " + df.format((probarr[i] * 100)) + " %");
            //System.out.println("Number of collision with satellite " +i+ ": " +counterarr[i]);

         }
      }
      // "probcheck" confirms the calculations have been done, and then can't be done again.
      Main.probcheck = true;
      // Once the calculations have been done, draw the lines that represent all the paths.
      Main.problines = true;

   }
   /*
    * This function calculates all the possible paths of every satellite given a
    * range and estimated initial velocities (as above when drawing the paths).
    * Next, the same thing is done for another satellite, and the two are compared
    * for every possible combination of paths for a certain amount of time (pathtime).
    * If there is a collision, then that path is skipped and the next one is looked at.
    * As there is quite a lot of processing, it can take a long time to calculate the
    * probability for a lot of satellites.
    */


   public boolean checkProbPaths(double newx, double newy, double newz, double newx1, double newy1, double newz1, int i, int k,
                                 int septimei, int septimek) {

      for (int coltime = 0; coltime < pathtime; coltime++) {

         if (coltime > septimei) {
            Main.satellite[i].Translations(coltime - septimei, newx, newy, newz);
            sat1x = Main.satellite[i].getx();
            sat1y = Main.satellite[i].gety();
            sat1z = Main.satellite[i].getz();

            if (coltime > septimek) {
               Main.satellite[k].Translations(coltime - septimek, newx1, newy1, newz1);
               sat2x = Main.satellite[k].getx();
               sat2y = Main.satellite[k].gety();
               sat2z = Main.satellite[k].getz();


               /*
                * Here the checks for collisions are done. If the satellites collide on a path,
                * the numbers of the satellites are recorded in the log, along with the the
                * velocities that caused the paths. Also the relevant array elements are
                * incremented, and finally the "pathtime" loop is broken out of, ready for the
                * next paths to be analysed.
                */
               if (Math.abs(sat1x - sat2x) < Main.collrange) {
                  if (Math.abs(sat1y - sat2y) < Main.collrange) {
                     if (Math.abs(sat1z - sat2z) < Main.collrange) {
    					/*Main.clog.addLog(Level.FINE, "Satellites " +i+ " and "
    						+k+ " have collided at time: "+coltime);
    					Main.clog.addLog(Level.FINE, "Sat " +i+ ": New X: " +newx+ " New Y: " 
    						+newy+ " New Z: " +newz+ "Separation Time: " + septimei);
    					Main.clog.addLog(Level.FINE, "Sat " +k+ ": New X: " +newx1+ " New Y: " 
    						+newy1+ " New Z: " +newz1 + "Separation Time: " + septimek);*/
                        return true;
                     }
                  }
               }
            }
         }


      }
      return false;
   }


}