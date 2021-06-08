package io.github.strikerrocker;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = LimitedSpawner.MODID)
public class ModConfig implements ConfigData {
    public int limit = 100;
}