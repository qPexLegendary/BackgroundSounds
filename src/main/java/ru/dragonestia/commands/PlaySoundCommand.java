package ru.dragonestia.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Sound;
import ru.dragonestia.BackgroundSounds;

public class PlaySoundCommand extends Command {

    public PlaySoundCommand(){
        super("playsound", "Plays a sound", "/playsound <id>");

        setPermission("backgroundsound.playsound");

        getCommandParameters().clear();
        addCommandParameters("default", new CommandParameter[]{
            new CommandParameter("soundId", CommandParamType.INT, false)
        });
    }

    @Override
    public boolean execute(CommandSender commandSender, String label, String[] args) {
        if(!(commandSender instanceof Player)) return false;

        Player player = (Player) commandSender;

        if(args.length < 1){
            player.sendMessage(getUsage());
            return false;
        }

        try{
            int soundId = Integer.parseInt(args[0]);
            Sound sound = BackgroundSounds.soundsArray[soundId];
            player.getLevel().addSound(player, sound);

            player.sendMessage("§ePlaying now §6" + sound.getSound() + "§e(id: " + soundId + ")");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | ClassCastException ex) {
            player.sendMessage("§cInvalid sound id.");
        }

        return true;
    }
}
