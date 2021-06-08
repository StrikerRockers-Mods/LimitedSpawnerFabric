package io.github.strikerrocker.mixin;

import io.github.strikerrocker.LimitedSpawner;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.MobSpawnerLogic;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobSpawnerLogic.class)
public class MixinSpawnerLogic {
    public int spawnedCount = 0;

    @Inject(method = "serverTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"))
    public void updateCount(CallbackInfo ci) {
        spawnedCount++;
    }

    @Inject(method = "serverTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/entity/EntityType;fromNbt(Lnet/minecraft/nbt/NbtCompound;)Ljava/util/Optional;"), cancellable = true)
    public void controlSpawn(CallbackInfo ci) {
        if (spawnedCount >= LimitedSpawner.config.limit) {
            ci.cancel();
        }
    }

    @Inject(method = "readNbt", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/nbt/NbtCompound;getShort(Ljava/lang/String;)S", ordinal = 0))
    public void readNbt(World world, BlockPos pos, NbtCompound nbt, CallbackInfo ci) {
        this.spawnedCount = nbt.getInt("spawnerCount");
    }

    @Inject(method = "writeNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtCompound;putShort(Ljava/lang/String;S)V", ordinal = 1))
    public void writeNbt(World world, BlockPos pos, NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        nbt.putInt("spawnerCount", spawnedCount);
    }
}