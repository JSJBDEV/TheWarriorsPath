package ace.actually.twp.mixin;

import ace.actually.twp.interfaces.StatsAccessor;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.util.Arm;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity implements StatsAccessor {
    private static final TrackedData<NbtCompound> TWP_STATS = DataTracker.registerData(PlayerEntity.class, TrackedDataHandlerRegistry.NBT_COMPOUND);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void setStrength(int i) {
        NbtCompound compound = dataTracker.get(TWP_STATS);
        compound.putInt("strength",i);
        dataTracker.set(TWP_STATS,compound);
    }

    @Override
    public int getStrength() {
        if(dataTracker.get(TWP_STATS).contains("strength"))
        {
            return dataTracker.get(TWP_STATS).getInt("strength");
        }
        return 0;
    }

    @Override
    public void addCompletedStar(String name) {
        NbtCompound compound = dataTracker.get(TWP_STATS);
        NbtList list;
        if(!dataTracker.get(TWP_STATS).contains("completed"))
        {
            list=new NbtList();
        }
        else
        {
            list= (NbtList) dataTracker.get(TWP_STATS).get("completed");
        }

        list.add(NbtString.of(name));
        compound.put("completed",list);
        dataTracker.set(TWP_STATS,compound);
    }

    @Override
    public NbtList getCompletedStars() {
        if(dataTracker.get(TWP_STATS).contains("completed"))
        {
            return (NbtList) dataTracker.get(TWP_STATS).get("completed");
        }
        return new NbtList();
    }

    @Inject(method = "initDataTracker", at = @At("TAIL"))
    protected void initDataTracker(CallbackInfo ci) {
        //super.initDataTracker();
        dataTracker.startTracking(TWP_STATS,new NbtCompound());

    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    public void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        dataTracker.set(TWP_STATS,nbt.getCompound("twp_stats"));

    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    public void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.put("twp_stats",dataTracker.get(TWP_STATS));

    }


}
