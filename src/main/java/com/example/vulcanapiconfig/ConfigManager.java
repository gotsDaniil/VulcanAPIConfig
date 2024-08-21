package com.example.vulcanapiconfig;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {
    private final MoveCheckPosPlugin plugin;
    private FileConfiguration config;

    public ConfigManager(MoveCheckPosPlugin plugin) {
        this.plugin = plugin;
        createConfig();
    }

    private void createConfig() {
        File configFile = new File(plugin.getDataFolder(), "Config.yml");
        // Создание директории, если она не существует
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        try {
            // Если конфигурационный файл не существует, создаем его
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            // Загружаем конфигурацию из файла
            config = YamlConfiguration.loadConfiguration(configFile);
            setDefaults(); // Устанавливаем значения по умолчанию
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setDefaults() {
        // Проверка и установка значений по умолчанию
        if (!config.contains("licenseKey")) {
            config.set("licenseKey", "ВАШ_КЛЮЧ");
        }
        if (!config.contains("//")) {
            config.set("//", "Оставьте наказание в punishment под всеми номерами. Оставьте ' ', чтобы убрать наказание");
        }
        if (!config.contains("punishment2")) {
            config.set("punishment2", "thor");
        }
        if (!config.contains("/")) {
            config.set("/", "Укажите время, если требуется, и укажите причину под всеми номерами message. Оставьте ' ', чтобы убрать причину");
        }
        if (!config.contains("message2")) {
            config.set("message2", "&cAC Вы были кикнуты за подозрение в читах");
        }
        if (!config.contains("punishment1")) {
            config.set("punishment1", "kill");
        }
        if (!config.contains("message1")) {
            config.set("message1", "&cAC Вы были кикнуты за подозрение в читах");
        }
        if (!config.contains("punishment")) {
            config.set("punishment", "minecraft:kick");
        }
        if (!config.contains("message")) {
            config.set("message", "&cAC Вы были кикнуты за подозрение в читах");
        }
        saveConfig(); // Сохраняем конфигурацию после установки значений по умолчанию
    }

    // Возвращаем значение наказания
    public String license() {
        return config.getString("licenseKey"); // Значение по умолчанию
    }
    public String getPunishment() {
        return config.getString("punishment2"); // Значение по умолчанию
    }
    public String getPunishment1() {
        return config.getString("message2"); // Значение по умолчанию
    }
    public String getPunishment2() {
        return config.getString("punishment1"); // Значение по умолчанию
    }
    public String getPunishment3() {
        return config.getString("message1"); // Значение по умолчанию
    }
    public String getPunishment4() {
        return config.getString("punishment"); // Значение по умолчанию
    }
    public String getPunishment5() {
        return config.getString("message"); // Значение по умолчанию
    }
    // Метод для сохранения конфигурации
    public void saveConfig() {
        try {
            config.save(new File(plugin.getDataFolder(), "Config.yml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}