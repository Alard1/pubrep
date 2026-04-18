package versatilesetup;

import input.model.CharacteristicBean;
import input.model.EventBlockBean;
import lombok.Getter;
import lombok.Setter;

import model.TaskPlus;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class SingleEventDataContainer extends TaskPlus {

    private final EventBlockBean eventBlockBean;
    private final List<CharacteristicBean> characteristicBeans;

    private final Map<String, String> values = new LinkedHashMap<>();

    public SingleEventDataContainer(
            EventBlockBean eventBlockBean, List<CharacteristicBean> characteristicBeans) {
        super(eventBlockBean.getStart(), eventBlockBean.getEnd());
        this.eventBlockBean = eventBlockBean;
        this.characteristicBeans = characteristicBeans;

        for (CharacteristicBean bean : characteristicBeans) {
            values.put(bean.getCharacteristic(), bean.getValue());
        }
    }

    public String getId() {
        return eventBlockBean.getId();
    }

    public String getHtml() {
        return eventBlockBean.getHtml();
    }

    public static SingleEventDataContainer cloneSingle(SingleEventDataContainer container) {
        return new SingleEventDataContainer(
                container.getEventBlockBean(), container.getCharacteristicBeans());
    }

    public String getValue(String field) {
        return this.values.getOrDefault(field, "Unknown");
    }

}
