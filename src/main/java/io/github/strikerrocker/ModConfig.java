package io.github.strikerrocker;

import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = LimitedSpawner.MODID)
public class ModConfig implements ConfigData {
    public int limit = 1;
}
