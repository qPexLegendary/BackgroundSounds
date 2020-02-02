package ru.dragonestia;

import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import ru.dragonestia.commands.BindSoundCommand;
import ru.dragonestia.commands.PlaySoundCommand;
import ru.dragonestia.commands.RemoveSoundsCommand;

import java.util.Arrays;
import java.util.HashMap;

public class BackgroundSounds extends PluginBase {

    public Config sounds;
    public static Sound[] soundsArray = Sound.values();
    public HashMap<String, Long> playedTime;

    @SuppressWarnings("deprecation")
    @Override
    public void onEnable(){
        sounds = new Config("plugins/BackgroundSounds/sounds.yml", Config.YAML);

        getServer().getCommandMap().registerAll("", Arrays.asList(
                new BindSoundCommand(this),
                new PlaySoundCommand(),
                new RemoveSoundsCommand(this)
        ));

        playedTime = new HashMap<>();
        getServer().getScheduler().scheduleRepeatingTask(() -> {
            sounds.getAll().forEach((level, sounds) -> {
                ((HashMap<String, Object>) sounds).forEach((soundsUid, params) -> {
                    String index = level + "." + soundsUid;

                    if(playedTime.containsKey(index)){
                        if(System.currentTimeMillis() > playedTime.get(index)){
                            getServer().getLevelByName(level).addSound(new Vector3(this.sounds.getInt(index + ".x"), this.sounds.getInt(index + ".y"), this.sounds.getInt(index + ".z")), soundsArray[this.sounds.getInt(index + ".sound")]);

                            playedTime.put(index, System.currentTimeMillis() + this.sounds.getInt(index + ".interval") * 1000);
                        }
                    }else{
                        getServer().getLevelByName(level).addSound(new Vector3(this.sounds.getInt(index + ".x"), this.sounds.getInt(index + ".y"), this.sounds.getInt(index + ".z")), soundsArray[this.sounds.getInt(index + ".sound")]);

                        playedTime.put(index, System.currentTimeMillis() + this.sounds.getInt(index + ".interval") * 1000);
                    }
                });
            });
        }, 10);
    }
}
