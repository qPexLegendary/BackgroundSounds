package ru.dragonestia.commands;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.ConfigSection;
import ru.dragonestia.BackgroundSounds;

public class RemoveSoundsCommand extends Command {

    private BackgroundSounds mainClass;
    public RemoveSoundsCommand(BackgroundSounds main){
        super("removesounds",  "Deletes sounds around you", "/removesounds <radius>");

        mainClass = main;

        setPermission("backgroundsound.removesound");

        getCommandParameters().clear();
        addCommandParameters("default", new CommandParameter[]{
                new CommandParameter("radius", CommandParamType.INT, false)
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
            int radius = Integer.parseInt(args[0]);

            if(radius < 1){
                player.sendMessage("§cInvalid radius.");
                return false;
            }

            if(!mainClass.sounds.exists(player.getLevel().getName())){
                player.sendMessage("§eSounds within a radius of " + radius + " blocks have been successfully deleted around you!");
                return false;
            }

            ConfigSection configSection = mainClass.sounds.getSection(player.getLevel().getName());
            configSection.getAllMap().forEach((key, value) -> {
                if(new Vector3(configSection.getInt(key + ".x"), configSection.getInt(key + ".y"), configSection.getInt(key + ".z")).distance(player) > radius) return;

                configSection.remove(key);
                mainClass.sounds.remove(player.getLevel().getName() + "." + key);
                mainClass.sounds.save(true);
            });

            player.sendMessage("§eSounds within a radius of " + radius + " blocks have been successfully deleted around you!");
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException | ClassCastException ex) {
            player.sendMessage("§cInvalid radius format.");
        }

        return true;
    }
}
