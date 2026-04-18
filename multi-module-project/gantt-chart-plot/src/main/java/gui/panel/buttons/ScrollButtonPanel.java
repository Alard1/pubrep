package gui.panel.buttons;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class ScrollButtonPanel extends JPanel implements Observer, Observable {

    private static final long serialVersionUID = 1L;
    private final List<Observer> observers = new ArrayList<>();
    private final IScrollController controller;
    private int gridCounter = 0;
    private Map<String, JComboBox<String>> comboBoxesByName = new HashMap<>();
    private Map<JComboBox<String>, String> namesByComboBox = new HashMap<>();

    public ScrollButtonPanel(
            IScrollController controller,
            String header,
            List<String> dimensionNames,
            boolean withResetButton) {

        this.controller = controller;

        this.setThisLayout(header);

        dimensionNames.forEach(this::createSingleScrollFunction);

        if (withResetButton) {
            createResetButton();
        }
    }

    private void createResetButton() {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = gridCounter++;

        JButton btnNewButton_8 = new JButton("Reset");

        btnNewButton_8.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        List<Pair<String, List<String>>> state = controller.clickResetButton();

                        for (Pair<String, List<String>> s : state) {
                            updateComboBoxWithNewModel(s);
                        }

                        updateObservers();
                    }
                });

        this.add(btnNewButton_8, gbc);
    }

    public void setSelectedItem(String string) {
        if (namesByComboBox.size() > 1) {
            System.out.println("ambiguous input! only one value while we can choose more");
        } else {

            JComboBox<String> combo = namesByComboBox.keySet().stream().toList().get(0);

            combo.setSelectedItem(string);
        }
    }

    private void createSingleScrollFunction(String id) {

        JComboBox<String> newCombo = new JComboBox<String>();

        comboBoxesByName.put(id, newCombo);
        namesByComboBox.put(newCombo, id);

        GridBagConstraints gbc_plantComboBox = new GridBagConstraints();
        gbc_plantComboBox.fill = GridBagConstraints.BOTH;
        gbc_plantComboBox.insets = new Insets(0, 0, 5, 0);
        gbc_plantComboBox.gridwidth = 2;
        gbc_plantComboBox.gridx = 0;
        gbc_plantComboBox.gridy = gridCounter++;
        this.add(newCombo, gbc_plantComboBox);

        newCombo.addItemListener(
                new ItemListener() {

                    @Override
                    public void itemStateChanged(ItemEvent e) {

                        if (e.getStateChange() == ItemEvent.SELECTED) {

                            stateChanged(newCombo);
                        }
                    }
                });

        JPanel panel_1 = new JPanel();
        GridBagConstraints gbc_panel_1 = new GridBagConstraints();
        gbc_panel_1.gridwidth = 2;
        gbc_panel_1.insets = new Insets(0, 0, 5, 0);
        gbc_panel_1.fill = GridBagConstraints.BOTH;
        gbc_panel_1.gridx = 0;
        gbc_panel_1.gridy = gridCounter++;
        this.add(panel_1, gbc_panel_1);
        panel_1.setLayout(new GridLayout(0, 2, 0, 0));

        JButton btnPreviousPlant = new JButton("Previous " + id.toLowerCase());
        panel_1.add(btnPreviousPlant);

        JButton btnNextPlant = new JButton("Next " + id.toLowerCase());
        panel_1.add(btnNextPlant);
        btnNextPlant.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Integer next = getComboNextElement(newCombo, false);
                        newCombo.setSelectedIndex(next);
                    }
                });
        btnPreviousPlant.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Integer next = getComboNextElement(newCombo, true);
                        newCombo.setSelectedIndex(next);
                    }
                });
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

        GridBagLayout gbl_scrollPanel = new GridBagLayout();
        gbl_scrollPanel.columnWidths = new int[] {0, 0};
        gbl_scrollPanel.rowHeights = new int[] {0, 0};
        gbl_scrollPanel.columnWeights = new double[] {Double.MIN_VALUE};
        gbl_scrollPanel.rowWeights = new double[] {Double.MIN_VALUE};

        this.setLayout(gbl_scrollPanel);
    }

    private <E> int getComboNextElement(JComboBox<E> comboBox, Boolean next) {

        int length = comboBox.getItemCount();
        int outputValue;
        if (next) {
            outputValue = comboBox.getSelectedIndex() - 1;
            if (outputValue == -1) {
                outputValue = length - 1;
            }
            return outputValue;
        } else {
            outputValue = comboBox.getSelectedIndex() + 1;
            if (outputValue >= length) {
                outputValue = 0;
            }
            return outputValue;
        }
    }

    private void updateStringComboBoxScope(
            JComboBox<String> comboBox, List<String> possibleValues) {

        String element;

        String currentSpecificFilter = (String) comboBox.getSelectedItem();

        for (int i = comboBox.getModel().getSize() - 1; i >= 0; i--) {
            element = comboBox.getModel().getElementAt(i);
            if (element.equals(currentSpecificFilter)) {
                continue;
            }
            ((DefaultComboBoxModel<String>) comboBox.getModel()).removeElementAt(i);
        }

        for (String mm : possibleValues) {

            if (mm.equals(currentSpecificFilter)) {
                continue;
            }
            ((DefaultComboBoxModel<String>) comboBox.getModel()).addElement(mm);
        }
    }

    protected void stateChanged(JComboBox<String> combo) {

        List<Pair<String, String>> newState = new ArrayList<>();

        for (Entry<String, JComboBox<String>> item : comboBoxesByName.entrySet()) {

            newState.add(
                    ImmutablePair.of(item.getKey(), (String) item.getValue().getSelectedItem()));
        }

        String nameOfDimensionThatWasJustChanged = namesByComboBox.get(combo);

        List<Pair<String, List<String>>> allNewPossibleItemsByDimensionName =
                controller.tellAboutStateChange(newState, nameOfDimensionThatWasJustChanged);

        for (Pair<String, List<String>> item : allNewPossibleItemsByDimensionName) {
            String nameOfTheComboBox = item.getLeft();

            if (nameOfTheComboBox.equals(nameOfDimensionThatWasJustChanged)) {
                continue;
            }

            JComboBox<String> theComboBox = comboBoxesByName.get(nameOfTheComboBox);
            List<String> possibleItemsForThisComboBox = item.getRight();

            this.updateStringComboBoxScope(theComboBox, possibleItemsForThisComboBox);
        }

        this.updateObservers();
    }

    private void initialize() {

        List<Pair<String, List<String>>> state = controller.initialize();

        state.forEach(this::updateComboBoxWithNewModel);
    }

    public void updateComboBoxWithNewModel(Pair<String, List<String>> box) {

        String comboBoxName = box.getKey();
        String[] possibleValues = box.getRight().toArray(new String[0]);

        comboBoxesByName
                .get(comboBoxName)
                .setModel(new DefaultComboBoxModel<String>(possibleValues));
    }

    @Override
    public void update() {

        this.initialize();

        this.updateObservers();
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void updateObservers() {
        for (Observer o : this.observers) {
            // for now this only works if there is one scroll dimension
            o.update((String) comboBoxesByName.values().stream().toList().get(0).getSelectedItem());
        }
    }

    @Override
    public void addObserver(Observer observer) {
        this.observers.add(observer);
    }
}
