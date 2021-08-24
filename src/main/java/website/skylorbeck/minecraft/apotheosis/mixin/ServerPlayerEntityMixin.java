package website.skylorbeck.minecraft.apotheosis.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.ModifyDamageTakenPower;
import io.github.apace100.origins.origin.OriginLayer;
import io.github.apace100.origins.origin.OriginLayers;
import io.github.apace100.origins.registry.ModComponents;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import website.skylorbeck.minecraft.apotheosis.Declarar;
import website.skylorbeck.minecraft.apotheosis.powers.ScalingModifyDamageTakenPower;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @ModifyArg(
            method = {"damage"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/PlayerEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private float modifyDamageAmountApo(DamageSource source, float originalAmount) {
        return PowerHolderComponent.modify((ServerPlayerEntity)(Object)this, ScalingModifyDamageTakenPower.class, originalAmount,(p) -> {
            return p.doesApply(source, originalAmount);
        }, (p) -> {
            p.executeActions(source.getAttacker());
        });
    }
}
