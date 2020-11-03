import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class maze {
    int row = 50;
    int col = 50;
    board bd;
    maze(){

        JFrame controlFrame = new JFrame("MAZE");
        controlFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        controlFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        bd = new board(row, col);
        //Grid Bag Layouting for controller
        JPanel pane = new JPanel();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 10;
        c.ipadx = 100;
        c.gridwidth = 5;
        c.insets = new Insets(15,0,0,15);
        //Speed
        JLabel speed = new JLabel("Speed");
        c.gridx = 0;
        c.gridy = 0;
        pane.add(speed,c);
        JSlider speedsldr = new JSlider(10,100, 50);
        speedsldr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {

                bd.setSpeed(speedsldr.getValue());
                bd.repaint();
            }
        });
        speedsldr.setInverted(true);
        speedsldr.setMajorTickSpacing(10);
        speedsldr.setMinorTickSpacing(1);
        speedsldr.setPaintTicks(true);
        speedsldr.setPaintLabels(true);
        c.gridx = 0;
        c.gridy = 1;
        pane.add(speedsldr,c);

        //For row controller
        JLabel rowlabel = new JLabel("Row");
        c.gridx = 0;
        c.gridy = 2;
        pane.add(rowlabel,c);
        JSlider rowsldr = new JSlider(10,50, 50);
        rowsldr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                row = rowsldr.getValue();
                bd.setRows(row);
                bd.repaint();
            }
        });
        row = rowsldr.getValue();
        rowsldr.setMajorTickSpacing(10);
        rowsldr.setMinorTickSpacing(1);
        rowsldr.setPaintTicks(true);
        rowsldr.setPaintLabels(true);
        c.gridx = 0;
        c.gridy = 3;
        pane.add(rowsldr,c);

        //For column controller

        JLabel collabel = new JLabel("Column");
        c.gridx = 0;
        c.gridy = 4;
        pane.add(collabel,c);
        JSlider colsldr = new JSlider(10,50, 50);
        colsldr.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                col = colsldr.getValue();
                bd.setCol(col);
                bd.repaint();
            }
        });
        col = colsldr.getValue();
        colsldr.setMajorTickSpacing(10);
        colsldr.setMinorTickSpacing(1);
        colsldr.setPaintTicks(true);
        colsldr.setPaintLabels(true);
        c.gridx = 0;
        c.gridy = 5;
        pane.add(colsldr,c);

        //For solve Button
        c.insets = new Insets(25,0,0,15);
        JButton solve = new JButton("Solve");
        JCheckBox solveBox = new JCheckBox("Show Solver");
        solveBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!solveBox.isSelected()){
                    bd.speed = 0;
                    solveBox.setSelected(false);
                }else{
                    solveBox.setSelected(true);
                    bd.speed = speedsldr.getValue();
                }
            }
        });
        solveBox.setSelected(true);
        c.gridx = 0;
        c.gridy = 6;
        c.fill = GridBagConstraints.HORIZONTAL;
        pane.add(solve, c);
        c.gridx = 5;

        pane.add(solveBox,c);
        solve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(bd.stopper) {
                    bd.solver();
                }
                bd.stopper = true;
                bd.solve = true;
            }
        });

        //For Generate Button
        JButton start = new JButton("Generate");
        JCheckBox startBox = new JCheckBox("Show Generation");
        startBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!startBox.isSelected()){
                    bd.speed = 0;
                    startBox.setSelected(false);
                }else{
                    startBox.setSelected(true);
                    bd.speed = speedsldr.getValue();
                }
            }
        });
        startBox.setSelected(true);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 7;

        pane.add(start, c);
        c.gridx = 5;
        pane.add(startBox, c);

        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                colsldr.setEnabled(false);
                rowsldr.setEnabled(false);
                start.setEnabled(false);
                bd.clicked();
                bd.paint = true;
                bd.repaint();
            }
        });

        //For Stop Button
        JButton end = new JButton("Stop");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 8;
        pane.add(end, c);
        end.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               bd.stopper = false;
            }
        });
        JPanel footer = new JPanel();
        footer.setBackground(Color.BLUE);

        footer.add(bd.info);
        footer.add(bd.done);
        controlFrame.getContentPane().add(bd);
        controlFrame.getContentPane().add(pane, BorderLayout.EAST);
        controlFrame.getContentPane().add(footer, BorderLayout.SOUTH);
        controlFrame.setVisible(true);

    }

    //Main Block of the program
    public static void main(String[] args) {
        new maze();
    }
}
