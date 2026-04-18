package gui.panel;

import java.awt.*;

import javax.swing.*;

public class JFrameBuilder {

    public static void build(JComponent panel) {

        JFrame mainFrame = new JFrame("Versatile Gantt chart viewer");

        mainFrame.setMinimumSize( new Dimension(1000, 800));
        initLookAndFeel();
        mainFrame.add(panel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
//        ImageIcon img = new ImageIcon("mrv.png");
//        mainFrame.setIconImage(img.getImage());
        mainFrame.setVisible(true);

    }

    private static void initLookAndFeel() {

        /*
         * the installed lookAndFeels :
         *
         * Metal Nimbus CDE/Motif Windows Windows Classic
         */

        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {

                if ("Windows".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {

        }
    }

}
