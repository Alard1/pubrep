package input.model;

import dataloader.BeanLoader;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.nio.file.Path;
import java.util.List;

public class InputLoader {

    public static Pair<List<EventBlockBean>, List<CharacteristicBean>> loadInputData(Path itemPath, Path charPath) {

        String blocksPath = itemPath.toString();
        String characteristicsPath = charPath.toString();

        BeanLoader loader = new BeanLoader();

        List<EventBlockBean> eventBlockBeans =
                loader.txtFileToBean(blocksPath, EventBlockBean.class);

        BeanLoader loader2 = new BeanLoader();

        List<CharacteristicBean> charBeans =
                loader2.txtFileToBean(characteristicsPath, CharacteristicBean.class);

        return ImmutablePair.of(eventBlockBeans, charBeans);
    }
}
