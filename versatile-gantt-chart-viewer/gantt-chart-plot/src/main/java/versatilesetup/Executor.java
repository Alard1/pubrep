package versatilesetup;

import app.GenericSettings;

import gui.panel.JFrameBuilder;
import input.model.CharacteristicBean;
import input.model.EventBlockBean;
import input.model.InputLoader;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.List;

import javax.swing.*;

public class Executor {

    public static void execute(Path itemPath, Path charPath) {

        Pair<List<EventBlockBean>, List<CharacteristicBean>> inputData =
                InputLoader.loadInputData(itemPath, charPath);

        GenericSettings<SingleEventDataContainer> settings =
                StandardSettingsBuilder.build(inputData);

        JSplitPane panel = StandardPanelBuilder.createStandardPanel(settings);

        JFrameBuilder.build(panel);
    }
}
