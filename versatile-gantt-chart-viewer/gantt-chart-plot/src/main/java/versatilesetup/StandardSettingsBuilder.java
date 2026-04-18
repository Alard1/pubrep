package versatilesetup;

import app.GenericSettings;

import buildingblocks.DataContainer;
import buildingblocks.options.ColorOption;
import buildingblocks.options.GroupOption;
import buildingblocks.options.ScrollOption;

import gui.ColoringFieldName;
import input.model.CharacteristicBean;
import input.model.EventBlockBean;
import org.apache.commons.lang3.tuple.Pair;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class StandardSettingsBuilder {

    public static GenericSettings<SingleEventDataContainer> build(
            Pair<List<EventBlockBean>, List<CharacteristicBean>> inputData) {

        List<SingleEventDataContainer> data = createDatasetFromInputObjects(inputData);

        Set<String> uniqueCharacteristics =
                inputData.getRight().stream()
                        .map(CharacteristicBean::getCharacteristic)
                        .collect(Collectors.toCollection(TreeSet::new));

        GenericSettings<SingleEventDataContainer> settings = new GenericSettings<>();

        DataContainer<SingleEventDataContainer> container =
                new DataContainer<>(data, StandardSettingsBuilder::cloneList);

        settings.setContainer(container);

        settings.setToolTipFunction(SingleEventDataContainer::getHtml);

        List<ScrollOption<SingleEventDataContainer>> scrollOptions =
                createScrollOptions(uniqueCharacteristics);

        List<GroupOption<SingleEventDataContainer>> groupOptions =
                createGroupOptions(uniqueCharacteristics);

        List<ColorOption<SingleEventDataContainer>> colorOptions =
                createColorOptions(uniqueCharacteristics);

        settings.setColorOptions(colorOptions);
        settings.setGroupOptions(groupOptions);
        settings.setScrollOptions(scrollOptions);

        return settings;
    }

    private static List<ScrollOption<SingleEventDataContainer>> createScrollOptions(
            Set<String> uniqueCharacteristics) {

        List<ScrollOption<SingleEventDataContainer>> colorOptionList = new ArrayList<>();

        for (String characteristic : uniqueCharacteristics) {

            ColoringFieldName coloringFieldName = new ColoringFieldName(characteristic);

            colorOptionList.add(
                    new ScrollOption<>(
                            characteristic, coloringFieldName::getValueForFieldName, true));
        }

        return colorOptionList;
    }

    private static List<GroupOption<SingleEventDataContainer>> createGroupOptions(
            Set<String> uniqueCharacteristics) {

        List<GroupOption<SingleEventDataContainer>> colorOptionList = new ArrayList<>();

        for (String characteristic : uniqueCharacteristics) {

            ColoringFieldName coloringFieldName = new ColoringFieldName(characteristic);

            colorOptionList.add(
                    new GroupOption<>(characteristic, coloringFieldName::getValueForFieldName));
        }

        colorOptionList.add(
                new GroupOption<>("Start date - end date", StandardSettingsBuilder::parseDates));

        colorOptionList.add(new GroupOption<>("id", SingleEventDataContainer::getId));

        colorOptionList.add(new GroupOption<>("same", o -> ""));

        return colorOptionList;
    }

    private static String parseDates(SingleEventDataContainer object) {

        LocalDate start = object.getStartDate();
        LocalDate end = object.getEndDate();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String startString = formatter.format(start);
        String endString = formatter.format(end);

        return startString +  " - " +endString;
    }

    private static List<ColorOption<SingleEventDataContainer>> createColorOptions(
            Set<String> uniqueCharacteristics) {

        List<ColorOption<SingleEventDataContainer>> colorOptionList = new ArrayList<>();

        for (String characteristic : uniqueCharacteristics) {

            ColoringFieldName coloringFieldName = new ColoringFieldName(characteristic);

            colorOptionList.add(
                    new ColorOption<>(
                            characteristic, coloringFieldName::getValueForFieldName, true));
        }

        colorOptionList.add(new ColorOption<>("id", SingleEventDataContainer::getId, false));

        colorOptionList.add(new ColorOption<>("same", o -> "", false));

        return colorOptionList;
    }

    private static List<SingleEventDataContainer> cloneList(List<SingleEventDataContainer> data) {

        return data.stream()
                .map(SingleEventDataContainer::cloneSingle)
                .collect(Collectors.toList());
    }

    private static List<SingleEventDataContainer> createDatasetFromInputObjects(
            Pair<List<EventBlockBean>, List<CharacteristicBean>> inputData) {

        List<EventBlockBean> eventBlockBeans = inputData.getLeft();
        List<CharacteristicBean> charBeans = inputData.getRight();

        Map<String, List<CharacteristicBean>> charsSplitById =
                charBeans.stream()
                        .collect(
                                Collectors.groupingBy(
                                        CharacteristicBean::getId,
                                        LinkedHashMap::new,
                                        Collectors.toList()));

        List<SingleEventDataContainer> dataContainers = new ArrayList<>();

        for (EventBlockBean bean : eventBlockBeans) {
            dataContainers.add(
                    new SingleEventDataContainer(
                            bean, charsSplitById.getOrDefault(bean.getId(), new ArrayList<>())));
        }

        return dataContainers;
    }
}
