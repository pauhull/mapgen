package de.pauhull.mapgen.util;

import net.darkhax.opennbt.NBTHelper;
import net.darkhax.opennbt.tags.CompoundTag;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class MapManager {

    public static ItemStack[][] getMapItem(JavaMap map, String worldName) {
        int[] mapIDs = saveMap(map, worldName);
        ItemStack[][] stacks = new ItemStack[map.width][map.height];

        for (int i = 0; i < map.width; i++) {
            for (int j = 0; j < map.height; j++) {
                stacks[i][j] = new ItemStack(Material.MAP, 1, (short) mapIDs[i + j * map.width]);
            }
        }

        return stacks;
    }

    public static int[] saveMap(JavaMap map, String worldName) {
        CompoundTag[][] tags = saveToNBT(map);
        return copyToDataFolder(worldName, tags);
    }

    public static int[] copyToDataFolder(String worldName, CompoundTag[][] maps) {
        int[] mapIDs = getNextMapIDs(worldName, maps.length * maps[0].length);

        for (int i = 0; i < maps.length; i++) {
            for (int j = 0; j < maps[0].length; j++) {
                File saveTo = new File(worldName + "/data/map_" + mapIDs[i + j * maps.length] + ".dat");
                NBTHelper.writeFile(maps[i][j], saveTo);
            }
        }

        return mapIDs;
    }

    public static int[] getNextMapIDs(String worldName, int neededIDs) {
        int[] IDs = new int[neededIDs];

        File dataFolder = new File(worldName + "/data");
        if (dataFolder.listFiles() == null) {
            for (int i = 0; i < neededIDs; i++) {
                IDs[i] = i;
            }
        }

        boolean searchingForIDs = true;
        int currentID = 0;
        int foundIDs = 0;
        while (searchingForIDs) {

            if (currentID >= Short.MAX_VALUE) {
                for (int i = 0; i < neededIDs; i++) {
                    IDs[i] = i;
                }
            }

            File file = new File(dataFolder, "map_" + currentID + ".dat");
            if (!file.exists()) {
                IDs[foundIDs++] = currentID;
                if (foundIDs >= neededIDs) {
                    searchingForIDs = false;
                }
            }

            currentID++;
        }

        return IDs;
    }

    public static CompoundTag[][] saveToNBT(JavaMap map) {

        CompoundTag[][] tags = new CompoundTag[map.width][map.height];

        for (int x = 0; x < map.width; x++) {
            for (int y = 0; y < map.height; y++) {
                CompoundTag data = new CompoundTag("data");
                data.setByte("scale", (byte) 0);
                data.setByte("dimension", (byte) 0);
                data.setShort("height", (short) 128);
                data.setShort("width", (short) 128);
                data.setByte("trackingPosition", (byte) 0);
                data.setByte("unlimitedTracking", (byte) 0);
                data.setInt("xCenter", Integer.MAX_VALUE);
                data.setInt("zCenter", Integer.MAX_VALUE);
                data.setByteArray("colors", getMapSection(x, y, map));

                CompoundTag mapTag = new CompoundTag("map");
                mapTag.setCompoundTag("data", data);

                tags[x][y] = mapTag;
            }
        }

        return tags;
    }

    public static byte[] getMapSection(int x, int y, JavaMap map) {
        byte[] section = new byte[128 * 128];

        for (int i = 0; i < 128; i++) {
            for (int j = 0; j < 128; j++) {
                int newX = i + x * 128;
                int newY = j + y * 128;
                section[i + j * 128] = map.colors[newX + newY * 128 * map.width];
            }
        }

        return section;
    }

}
