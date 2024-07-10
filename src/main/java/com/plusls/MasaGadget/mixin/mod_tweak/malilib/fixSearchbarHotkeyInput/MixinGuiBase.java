package com.plusls.MasaGadget.mixin.mod_tweak.malilib.fixSearchbarHotkeyInput;

import com.plusls.MasaGadget.game.Configs;
import fi.dy.masa.malilib.gui.GuiBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = GuiBase.class, remap = false)
public class MixinGuiBase {
    @Shadow
    private int keyInputCount;
    @Unique
    private long masa_gadget$openTime;

    @Inject(
            method = "init",
            at = @At(
                    value = "HEAD"
            ),
            remap = true
    )
    private void preInit(CallbackInfo ci) {
        if (!Configs.fixSearchbarHotkeyInput.getBooleanValue()) {
            return;
        }

        this.masa_gadget$openTime = System.nanoTime();
        this.keyInputCount = 0;
    }

    @Inject(
            method = "charTyped",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true,
            remap = true
    )
    private void preCharTyped(char charIn, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        if (!Configs.fixSearchbarHotkeyInput.getBooleanValue()) {
            return;
        }

        if (this.keyInputCount <= 0 && System.nanoTime() - this.masa_gadget$openTime <= 100000000) {
            this.keyInputCount++;
            cir.setReturnValue(true);
        }
    }
}
