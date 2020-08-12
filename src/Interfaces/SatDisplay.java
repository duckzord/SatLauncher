package Interfaces;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//import java.awt.event.ItemEvent;
//import java.awt.event.ItemListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Main.Main;

public class SatDisplay extends JFrame implements ActionListener, ListSelectionListener {

   private static final long serialVersionUID = 1L;
   double[][] satvars = SatSetup.Vars;
   int numsats = SatSetup.num;
   public String[] columnnames = {"Sat Name", "Theta (degrees)", "Z Position (metres)",
       "Initial x Velocity", "Initial y Velocity", "Initial z Velocity", "Separation Time"};
   public Object[][] satdata = new Object[numsats][8];
   JPanel panel1 = new JPanel();
   JPanel panel2 = new JPanel();

   JPanel p2row1 = new JPanel();
   JLabel detail = new JLabel("Detailed Information: ");

   JPanel p2row2 = new JPanel();
   JLabel currxpos = new JLabel("Current x Position:");
   public static JLabel xposvar = new JLabel();
   JLabel currypos = new JLabel("Current y Position:");
   public static JLabel yposvar = new JLabel();
   JLabel currzpos = new JLabel("Current z Position:");
   public static JLabel zposvar = new JLabel();
   JLabel currxvel = new JLabel("Current x Velocity:");
   public static JLabel xvelvar = new JLabel();
   JLabel curryvel = new JLabel("Current y Velocity:");
   public static JLabel yvelvar = new JLabel();
   JLabel currzvel = new JLabel("Current z Velocity:");
   public static JLabel zvelvar = new JLabel();

   public SatDisplay() {
      super("Satellite Information");
      setSize(900, 500);
      setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      GridLayout layout = new GridLayout(2, 1, 0, 0);
      Container pane = getContentPane();
      pane.setLayout(layout);

      GridLayout layout1 = new GridLayout(1, 1, 0, 0);
      panel1.setLayout(layout1);
      GridLayout layout2 = new GridLayout(2, 1, 0, 0);
      panel2.setLayout(layout2);
      FlowLayout layout3 = new FlowLayout(FlowLayout.LEFT, 0, 30);
      p2row1.setLayout(layout3);
      GridLayout layout4 = new GridLayout(1, 7, 0, 0);
      p2row2.setLayout(layout4);

      if (numsats != 0) {
         for (int i = 0; i < numsats; i++) {
            satdata[i][0] = "Satellite " + i;
            for (int j = 1; j < 8; j++) {
               satdata[i][j] = "" + satvars[i][j - 1];
            }
         }
      }
      JTable datatable = new JTable(satdata, columnnames);
      datatable.getSelectionModel().addListSelectionListener(this);
      datatable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      JScrollPane scrollpane = new JScrollPane(datatable);
      panel1.add(scrollpane);
      pane.add(panel1);

      p2row1.add(detail);

      GridLayout infolayout = new GridLayout(1, 3);
      p2row2.setLayout(infolayout);
      p2row2.add(currxpos);
      p2row2.add(xposvar);
      p2row2.add(currypos);
      p2row2.add(yposvar);
      p2row2.add(currzpos);
      p2row2.add(zposvar);
		/*
		 * Don't add these yet, because nothing they don't do anything yet.
		p2row2.add(currxvel);
		p2row2.add(xvelvar);
		p2row2.add(curryvel);
		p2row2.add(yvelvar);
		p2row2.add(currzvel);
		p2row2.add(zvelvar);*/
      panel2.add(p2row1);
      panel2.add(p2row2);
      pane.add(panel2);
      setContentPane(pane);
      setVisible(true);

   }

   public void actionPerformed(ActionEvent action) {
   }

   public void valueChanged(ListSelectionEvent lse) {

      String preindex = lse.getSource().toString();
      System.out.println(preindex);
      int index1 = preindex.indexOf('{');
      int index2 = preindex.indexOf('}');

      int index = Integer.parseInt(preindex.substring(index1 + 1, index2));
      String xs = "" + Main.satellite[index].getx();
      String ys = "" + Main.satellite[index].gety();
      String zs = "" + Main.satellite[index].getz();

      if (xs.length() > 5) {
         xs = xs.substring(0, 5);
         xposvar.setText(xs);
      } else {
         xposvar.setText(xs);
      }
      if (ys.length() > 5) {
         ys = ys.substring(0, 5);
         yposvar.setText(ys);
      } else {
         yposvar.setText(ys);
      }
      if (zs.length() > 5) {
         zs = zs.substring(0, 5);
         zposvar.setText(zs);
      } else {
         zposvar.setText(zs);
      }

   }
}