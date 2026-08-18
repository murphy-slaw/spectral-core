package net.funkpla.spectral_core.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import net.funkpla.spectral_core.Constants;


@Config(name = Constants.MOD_ID)
public class CoreConfig implements ConfigData {
    public int lavaLevel = -117;
    public int coconutRipeningDelay = 5;
}
