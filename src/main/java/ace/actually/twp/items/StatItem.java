package ace.actually.twp.items;

import ace.actually.twp.TWP;
import ace.actually.twp.interfaces.StatsAccessor;
import ace.actually.twp.screens.StatScreen;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.stream.Collectors;

public class StatItem extends Item {
    public StatItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient && hand==Hand.MAIN_HAND)
        {
            StatsAccessor accessor = (StatsAccessor) user;
            PacketByteBuf buf = PacketByteBufs.create();

            buf.writeString(String.join(",",accessor.getCompletedStars().stream().map(a->(NbtString)a).map(NbtString::asString).toList()));
            ServerPlayNetworking.send((ServerPlayerEntity) user, TWP.BOOK_LOGIC,buf);
        }

        return super.use(world, user, hand);
    }
}
