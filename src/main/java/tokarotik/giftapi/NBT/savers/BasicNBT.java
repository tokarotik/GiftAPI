package tokarotik.giftapi.NBT.savers;

import net.minecraft.server.v1_6_R3.EntityPlayer;
import net.minecraft.server.v1_6_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_6_R3.NBTTagCompound;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_6_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

public final class BasicNBT {

    private final Path worldPath;

    public BasicNBT(String world) {
        this.worldPath = Paths.get(getBasePath(), world);
    }

    /**
     * Writes the player's NBT data to disk.
     *
     * @param compound     the NBTTagCompound representing player data
     * @param entityPlayer the player entity
     * @return true if write succeeded, false otherwise
     */
    public boolean writePlayerNBT(NBTTagCompound compound, EntityPlayer entityPlayer) {
        if (compound == null || entityPlayer == null) {
            Bukkit.getLogger().warning("Attempted to write NBT with null compound or player");
            return false;
        }

        // Update NBT with entityPlayer state before saving
        entityPlayer.f(compound);

        Path savePath = getSavePath(entityPlayer);
        File saveFile = savePath.toFile();

        // Ensure parent directories exist
        File parentDir = saveFile.getParentFile();
        if (parentDir != null && !parentDir.exists() && !parentDir.mkdirs()) {
            Bukkit.getLogger().warning("Failed to create directories for player save path: " + parentDir);
            return false;
        }

        try (FileOutputStream out = new FileOutputStream(saveFile)) {
            NBTCompressedStreamTools.a(compound, out);
            return true;
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to write player NBT for " + entityPlayer.getName(), e);
            return false;
        }
    }

    /**
     * Reads the player's NBT data from disk.
     * If the file does not exist or fails to read, returns a fresh NBTTagCompound
     * initialized with the player's current state.
     *
     * @param entityPlayer the player entity
     * @return the player's NBTTagCompound data or null if unrecoverable error occurs
     */
    public NBTTagCompound readPlayerNBT(EntityPlayer entityPlayer) {
        if (entityPlayer == null) {
            Bukkit.getLogger().warning("Attempted to read NBT for null player");
            return null;
        }

        Path savePath = getSavePath(entityPlayer);
        File saveFile = savePath.toFile();

        if (!saveFile.exists()) {
            // No existing data, return fresh compound from entity state
            return getFreshPlayerNBT(entityPlayer);
        }

        try (FileInputStream in = new FileInputStream(saveFile)) {
            NBTTagCompound compound = NBTCompressedStreamTools.a(in);
            // Load data into entityPlayer
            entityPlayer.e(compound);
            return compound;
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to read player NBT for " + entityPlayer.getName(), e);
            return getFreshPlayerNBT(entityPlayer);
        }
    }

    private NBTTagCompound getFreshPlayerNBT(EntityPlayer entityPlayer) {
        try {
            NBTTagCompound compound = new NBTTagCompound();
            entityPlayer.e(compound);
            return compound;
        } catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Failed to initialize fresh NBT for " + entityPlayer.getName(), e);
            return null;
        }
    }

    public static EntityPlayer getPlayer(Player player) {
        if (!(player instanceof CraftPlayer)) {
            throw new IllegalArgumentException("Player must be a CraftPlayer instance");
        }
        return ((CraftPlayer) player).getHandle();
    }

    /**
     * Returns the base directory path of the running server jar or working directory.
     */
    private String getBasePath() {
        try {
            return new File(".").getCanonicalPath();
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.WARNING, "Failed to get base path", e);
            return System.getProperty("user.dir"); // fallback
        }
    }

    /**
     * Constructs the file path where the player's NBT data should be saved.
     *
     * @param entityPlayer the player entity
     * @return Path to the player's .dat file
     */
    private Path getSavePath(EntityPlayer entityPlayer) {
        return worldPath.resolve(Paths.get("players", entityPlayer.getName() + ".dat"));
    }
}

