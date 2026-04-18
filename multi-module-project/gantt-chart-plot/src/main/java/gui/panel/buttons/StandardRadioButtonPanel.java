package gui.panel.buttons;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class StandardRadioButtonPanel extends JPanel implements Observer {

    /** */
    private static final long serialVersionUID = 1L;

    private final ButtonGroup buttonGroup = new ButtonGroup();
    private final IRadioButtonController controller;
    private int gridCounter = 0;

    public StandardRadioButtonPanel(
            IRadioButtonController controller,
            String header,
            List<String> elements,
            String... optionalButtonName) {

        this.controller = controller;

        this.setThisLayout(header);

        elements.forEach(this::createRadioButton);

        buttonGroup.getElements().nextElement().setSelected(true);

        if (optionalButtonName.length > 0) {
            this.createRefreshButton(optionalButtonName[0]);
        }
    }

    private void createRefreshButton(String optionalButtonName) {

        JButton btnRefreshColors = new JButton(optionalButtonName);
        GridBagConstraints gbc_btnRefreshColors = new GridBagConstraints();
        gbc_btnRefreshColors.fill = GridBagConstraints.HORIZONTAL;
        gbc_btnRefreshColors.gridx = 0;
        gbc_btnRefreshColors.gridy = gridCounter++;
        this.add(btnRefreshColors, gbc_btnRefreshColors);

        btnRefreshColors.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        controller.hitOptionalButton();
                    }
                });
    }

    private void createRadioButton(String option) {

        JRadioButton rdbtnStandardRadioButton = new JRadioButton(option);
        GridBagConstraints gbc_rdbtnStandardRadioButton = new GridBagConstraints();
        gbc_rdbtnStandardRadioButton.insets = new Insets(0, 0, 5, 0);
        gbc_rdbtnStandardRadioButton.fill = GridBagConstraints.HORIZONTAL;
        gbc_rdbtnStandardRadioButton.gridx = 0;
        gbc_rdbtnStandardRadioButton.gridy = gridCounter++;
        this.add(rdbtnStandardRadioButton, gbc_rdbtnStandardRadioButton);

        rdbtnStandardRadioButton.addActionListener(
                new ActionListener() {

                    public void actionPerformed(ActionEvent event) {
                        JRadioButton cb = (JRadioButton) event.getSource();
                        if (cb.isSelected()) {

                            controller.updateSelectedItem(option);
                        }
                    }
                });

        buttonGroup.add(rdbtnStandardRadioButton);
    }

    private void setThisLayout(String header) {
        this.setBorder(
                new TitledBorder(
                        BorderFactory.createLineBorder(Color.gray, 1),
                        header,
                        TitledBorder.LEADING,
                        TitledBorder.TOP,
                        null,
                        null));

        GridBagLayout gridBagLayout = new GridBagLayout();
        gridBagLayout.columnWidths = new int[] {0, 0};
        gridBagLayout.rowHeights = new int[] {0, 0, 0, 0, 0, 0};
        gridBagLayout.columnWeights = new double[] {1.0, Double.MIN_VALUE};
        gridBagLayout.rowWeights = new double[] {0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};

        this.setLayout(gridBagLayout);
    }

    @Override
    public void update() {

        controller.updateSelectedItem(getSelectedButtonText(this.buttonGroup));
    }

    private String getSelectedButtonText(ButtonGroup buttonGroup) {
        for (Enumeration<AbstractButton> buttons = buttonGroup.getElements();
                buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();

            if (button.isSelected()) {
                return button.getText();
            }
        }

        return null;
    }
}
