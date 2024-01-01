package me.djtheredstoner.devauth.neoforge.mixins;

import me.djtheredstoner.devauth.neoforge.NeoForgeBootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(targets = "net.minecraft.client.main.Main")
public class MainMixin {

    @ModifyVariable(method = "main", at = @At("HEAD"), argsOnly = true, remap = false)
    private static String[] modifyArgs(String[] args) {
        return NeoForgeBootstrap.processArguments(args);
    }

}
