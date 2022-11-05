package ace.actually.twp.items;

import ace.actually.twp.interfaces.StatsAccessor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CheckItem extends Item {
    public CheckItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        StatsAccessor accessor = (StatsAccessor) user;
        user.sendMessage(Text.of(accessor.getStrength()+" strength"),false);
        return super.use(world, user, hand);
    }
}
