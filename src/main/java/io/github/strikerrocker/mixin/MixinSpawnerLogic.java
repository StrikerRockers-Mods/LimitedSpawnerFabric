package io.github.strikerrocker.mixin;

import io.github.strikerrocker.LimitedSpawner;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.MobSpawnerLogic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobSpawnerLogic.class)
public class MixinSpawnerLogic {
    public int spawnedCount = 0;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"))
    public void updateCount(CallbackInfo ci) {
        spawnedCount++;
    }

    @Inject(method = "update", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/EntityType;fromTag(Lnet/minecraft/nbt/CompoundTag;)Ljava/util/Optional;"), cancellable = true)
    public void controlSpawn(CallbackInfo ci) {
        if (spawnedCount >= LimitedSpawner.config.limit) {
            ci.cancel();
        }
    }

    @Inject(method = "fromTag", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/CompoundTag;getShort(Ljava/lang/String;)S", ordinal = 0))
    public void fromTagSpawner(CompoundTag tag, CallbackInfo callbackInfo) {
        this.spawnedCount = tag.getInt("spawnerCount");
    }

    @Inject(method = "toTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putShort(Ljava/lang/String;S)V", ordinal = 1))
    public void toTagSpawner(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir) {
        tag.putInt("spawnerCount", spawnedCount);
    }
}