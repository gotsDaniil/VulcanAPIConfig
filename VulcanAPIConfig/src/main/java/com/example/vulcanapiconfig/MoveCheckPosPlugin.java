package com.example.vulcanapiconfig;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class MoveCheckPosPlugin extends JavaPlugin {

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        this.configManager = new ConfigManager(this);
        getCommand("movecheckpos").setExecutor(this);
        getCommand("movecheckpos").setTabCompleter(new MoveCheckPosTabCompleter());
    }

    Player target;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Проверяем команду на перезагрузку
        if (command.getName().equalsIgnoreCase("vulcanapiconfig")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                // Проверяем, является ли отправитель игроком и имеет ли он разрешение
                if (sender instanceof Player && sender.hasPermission("vulcanapiconfig.reload")) {
                    reloadPlugin(sender);
                } else {
                    sender.sendMessage("У вас недостаточно прав для выполнения этой команды.");
                }
                return true;
            }
        }

        // Проверяем, что команду может выполнять только консоль
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage("Эта команда может быть выполнена только консолью.");
            return true;
        }

        // Проверяем количество аргументов
        if (args.length != 1) {
            sender.sendMessage("Использование: /movecheckpos <игрок>");
            return true;
        }
        target = Bukkit.getPlayer(args[0]);

        // Получаем игрока по имени
        if (target == null) {
            sender.sendMessage("Игрок не найден.");
            return true;
        }

        // Проверяем, движется ли целевой игрок
        if (target.isSneaking() || target.isJumping() || target.isSprinting()) {
            // Наказание игрока
            String punishment2 = configManager.getPunishment();
            String punishment1 = configManager.getPunishment2();
            String message2 = configManager.getPunishment1();
            String message1 = configManager.getPunishment3();
            Bukkit.dispatchCommand((ConsoleCommandSender) sender, punishment2 + " " + target.getName() + " " + message2);
            Bukkit.dispatchCommand((ConsoleCommandSender) sender, punishment1 + " " + target.getName() + " " + message1);
            String punishment = configManager.getPunishment4();
            String message = configManager.getPunishment5();

            Bukkit.dispatchCommand((ConsoleCommandSender) sender, punishment + " " + target.getName() + " " + message);
        }
        return true;
    }

    // Метод для перезагрузки плагина и конфигурации
    private void reloadPlugin(CommandSender sender) {
        this.configManager = new ConfigManager(this); // Создаем новый экземпляр ConfigManager
        sender.sendMessage("Плагин успешно перезагружен.");
    }

    // Вспомогательный класс для табуляции
    private class MoveCheckPosTabCompleter implements TabCompleter {
        @Override
        public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
            List<String> completions = new ArrayList<>();
            if (args.length == 1 && sender instanceof Player) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    completions.add(player.getName());
                }
            }
            return completions;
        }
    }
}
