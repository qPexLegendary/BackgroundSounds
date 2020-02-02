package ru.dragonestia.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;
import ru.dragonestia.BackgroundSounds;

import java.util.Random;

public class BindSoundCommand extends Command {

    private BackgroundSounds mainClass;

    public BindSoundCommand(BackgroundSounds main){
        super("bindsound", "Binds sound to a given point", "/bindsound <id> <interval>");

        mainClass = main;

        setPermission("backgroundsound.bindsound");

        getCommandParameters().clear();
        addCommandParameters("default", new CommandParameter[]{
                new CommandParameter("soundId", CommandParamType.INT, false),
                new CommandParameter("interval", CommandParamType.INT, false)
        });
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if(args.length < 2){
            player.sendMessage(getUsage());
            return false;
        }

        try{
            int soundId = Integer.parseInt(args[0]);
            int interval = Integer.parseInt(args[1]);

            if(interval < 1){
                player.sendMessage("§cInvalid interval.");
                return false;
            }

            String index = player.getLevel().getName() + "." + new Random().nextLong();
            mainClass.sounds.set(index + ".x", player.x);
            mainClass.sounds.set(index + ".y", player.y);
            mainClass.sounds.set(index + ".z", player.z);
            mainClass.sounds.set(index + ".sound", soundId);
            mainClass.sounds.set(index + ".interval", interval);
            mainClass.sounds.save(true);

            index = BackgroundSounds.soundsArray[soundId] + "";
            player.sendMessage("§eThe sound was successfully tied to this point!");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | ClassCastException ex) {
            player.sendMessage("§cInvalid sound id or interval format.");
        }

        return true;
    }
}
