package com.onarandombox.multiverseinventories.group;

import com.google.common.collect.Lists;
import com.onarandombox.multiverseinventories.util.DeserializationException;
import com.onarandombox.multiverseinventories.util.MILog;
import com.onarandombox.multiverseinventories.share.Shares;
import com.onarandombox.multiverseinventories.share.SimpleShares;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashSet;
import java.util.List;

/**
 * @author dumptruckman
 */
public class SimpleWorldGroup implements WorldGroup {

    private String name = "";
    private HashSet<String> worlds = new HashSet<String>();
    private Shares shares = new SimpleShares();
    
    public SimpleWorldGroup(String name) {
        this.name = name;
    }

    public SimpleWorldGroup(String name, ConfigurationSection data) throws DeserializationException {
        if (!data.contains("worlds")) {
            throw new DeserializationException("No worlds specified for world group: " + name);
        }
        List<String> worldList = data.getStringList("worlds");
        if (worldList == null) {
            throw new DeserializationException("Worlds incorrectly formatted for group: " + name);
        }
        for (String worldName : worldList) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                this.addWorld(world);
            } else {
                MILog.warning("");
            }
        }
        if (data.contains("shares")) {
            List<String> sharesList = data.getStringList("shares");
            if (sharesList != null) {
                this.setShares(SimpleShares.parseShares(sharesList));
            } else {
                MILog.warning("Shares formatted incorrectly for group: " + name);
            }
        }
    }

    public void serialize(ConfigurationSection groupData) {
        groupData.set("worlds", Lists.newArrayList(this.getWorlds()));
        List<String> sharesList = this.getShares().toStringList();
        if (!sharesList.isEmpty()) {
            groupData.set("shares", sharesList);
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void addWorld(String worldName) {
        this.worlds.add(worldName);
    }

    @Override
    public void addWorld(World world) {
        this.worlds.add(world.getName());
    }

    @Override
    public HashSet<String> getWorlds() {
        return this.worlds;
    }

    @Override
    public void setShares(Shares shares) {
        this.shares = shares;
    }

    @Override
    public Shares getShares() {
        return this.shares;
    }
}