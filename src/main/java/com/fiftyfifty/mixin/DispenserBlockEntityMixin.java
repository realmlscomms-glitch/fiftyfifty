package com.fiftyfifty.mixin;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DispenserBlockEntity;
import net.minecraft.world.level.block.entity.DropperBlockEntity;

import com.fiftyfifty.FiftyFifty;
import com.fiftyfifty.ForceMode;

/**
 * Hooks the exact spot where Minecraft decides which slot a dropper fires.
 * Vanilla picks a random non-empty slot; we override that choice.
 */
@Mixin(DispenserBlockEntity.class)
public abstract class DispenserBlockEntityMixin {

	@Inject(method = "getRandomSlot", at = @At("HEAD"), cancellable = true)
	private void fiftyfifty$forcePull(RandomSource random, CallbackInfoReturnable<Integer> cir) {
		// Only droppers, not dispensers (DropperBlockEntity extends DispenserBlockEntity).
		if (!((Object) this instanceof DropperBlockEntity)) {
			return;
		}

		DispenserBlockEntity self = (DispenserBlockEntity) (Object) this;

		List<Integer> waterSlots = new ArrayList<>();
		List<Integer> lavaSlots = new ArrayList<>();
		for (int i = 0; i < self.getContainerSize(); i++) {
			ItemStack stack = self.getItem(i);
			if (stack.is(Items.WATER_BUCKET)) {
				waterSlots.add(i);
			} else if (stack.is(Items.LAVA_BUCKET)) {
				lavaSlots.add(i);
			}
		}

		// No buckets in this dropper: leave it completely vanilla.
		if (waterSlots.isEmpty() && lavaSlots.isEmpty()) {
			return;
		}

		ForceMode mode = FiftyFifty.mode;
		List<Integer> pick;

		if (mode == ForceMode.WATER && !waterSlots.isEmpty()) {
			pick = waterSlots;
		} else if (mode == ForceMode.LAVA && !lavaSlots.isEmpty()) {
			pick = lavaSlots;
		} else if (!waterSlots.isEmpty() && !lavaSlots.isEmpty()) {
			// Fair mode: a true 50/50 between the two ITEMS,
			// no matter how many slots each one fills.
			pick = random.nextBoolean() ? waterSlots : lavaSlots;
		} else {
			// Only one kind is loaded (or the forced one is missing): vanilla behaviour.
			return;
		}

		cir.setReturnValue(pick.get(random.nextInt(pick.size())));
	}
}
