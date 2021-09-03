package me.djtheredstoner.devauth.fabric.mixins;

import me.djtheredstoner.devauth.fabric.FabricBootstrap;
import net.minecraft.client.main.Main;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Main.class)
public class MainMixin {

    @ModifyVariable(method = "main", at = @At("HEAD"), argsOnly = true)
    private static String[] modifyArgs(String[] args) {
        return FabricBootstrap.getDevAuth().processArguments(args);
    }

}
