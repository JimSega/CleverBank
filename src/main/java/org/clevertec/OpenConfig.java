package org.clevertec;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;

public class OpenConfig {

    public static Map<String, Object> openFile () {
        InputStream inputStream = Main.class.getResourceAsStream("/config.yml");
        Yaml yaml = new Yaml();
        return yaml.load(inputStream);
    }

}
