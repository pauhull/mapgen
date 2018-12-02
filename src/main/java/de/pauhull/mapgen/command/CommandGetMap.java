package de.pauhull.mapgen.command;

import de.pauhull.mapgen.util.JavaMap;
import de.pauhull.mapgen.util.MapManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class CommandGetMap implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("getmap")) {
            if(!(sender instanceof Player) || !sender.hasPermission("getmap")) {
                return true;
            }
            if (args.length == 3) {
                Player player = (Player) sender;
                BufferedImage image = null;
                try {
                    URL url = new URL(args[0]);
                    URLConnection connection = url.openConnection();
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0");
                    InputStream in = connection.getInputStream();
                    image = ImageIO.read(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ItemStack[][] stacks = MapManager.getMapItem(new JavaMap(image, Integer.parseInt(args[1]), Integer.parseInt(args[2])), /* player.getWorld().getName()*/ "world");
                for (int i = 0; i < stacks.length; i++) {
                    for (int j = 0; j < stacks[0].length; j++) {
                        player.getInventory().addItem(stacks[i][j]);
                    }
                }
            }
        }
        return true;
    }

}
