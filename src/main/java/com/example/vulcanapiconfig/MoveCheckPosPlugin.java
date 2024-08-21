package com.example.vulcanapiconfig;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class MoveCheckPosPlugin extends JavaPlugin {

    private ConfigManager configManager;
    private License license;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        loadLicense();
        if (license.isValid()) {
            Bukkit.getServer().getConsoleSender().sendMessage("-------VulcanAPIConfig-------");
            Bukkit.getServer().getConsoleSender().sendMessage(" |- Вход в систему принят");
            Bukkit.getServer().getConsoleSender().sendMessage(" |- Добро пожаловать: " + license.getLicensedTo());
            Bukkit.getServer().getConsoleSender().sendMessage(" |- Лицензия: " + license.getLicense());
            Bukkit.getServer().getConsoleSender().sendMessage(" |- Я включаю все функции для вас");
            Bukkit.getServer().getConsoleSender().sendMessage("-------VulcanAPIConfig-------");
        } else {
            Bukkit.getServer().getConsoleSender().sendMessage("-------VulcanAPIConfig-------");
            Bukkit.getServer().getConsoleSender().sendMessage(" |- Вход заблокирован");
            Bukkit.getServer().getConsoleSender().sendMessage(" |- Возвращаемая ошибка " + license.getReturn());
            Bukkit.getServer().getConsoleSender().sendMessage("-------VulcanAPIConfig-------");

            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        getCommand("movecheckpos").setExecutor(this);
        getCommand("movecheckpos").setTabCompleter(new MoveCheckPosTabCompleter());
    }

    private void loadLicense() {
        String licenseKey = configManager.license();
        String serverUrl = "https://panel.gots.enko.moe";
        license = new License(licenseKey, serverUrl, this);
        license.request();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("movecheckpos")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.hasPermission("vulcanapiconfig.reload")) {
                reloadPlugin(sender);
                return true;
            }

            if (!(sender instanceof ConsoleCommandSender)) {
                sender.sendMessage("Эта команда может быть выполнена только консолью.");
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage("Использование: /movecheckpos <игрок>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                sender.sendMessage("Игрок не найден.");
                return true;
            }

            if (target.isSneaking() || target.isJumping() || target.isSprinting()) {
                executePunishments(sender, target);
            }
            return true;
        }
        return false;
    }

    private void executePunishments(CommandSender sender, Player target) {
        String[] punishments = {
                configManager.getPunishment(),
                configManager.getPunishment2(),
                configManager.getPunishment4()
        };
        String[] messages = {
                configManager.getPunishment1(),
                configManager.getPunishment3(),
                configManager.getPunishment5()
        };

        for (int i = 0; i < punishments.length; i++) {
            Bukkit.dispatchCommand((ConsoleCommandSender) sender, punishments[i] + " " + target.getName() + " " + messages[i]);
        }
    }

    private void reloadPlugin(CommandSender sender) {
        reloadConfig();
        this.configManager = new ConfigManager(this);
        sender.sendMessage("Плагин успешно перезагружен.");
    }

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