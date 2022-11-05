package ace.actually.twp.interfaces;

import net.minecraft.nbt.NbtList;

import java.util.List;

public interface StatsAccessor {
    int getStrength();
    void setStrength(int i);

    void addCompletedStar(String name);
    NbtList getCompletedStars();
}
