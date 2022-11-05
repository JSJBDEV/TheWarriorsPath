package ace.actually.twp;

import ace.actually.twp.interfaces.StatsAccessor;
import ace.actually.twp.items.CheckItem;
import ace.actually.twp.items.StatItem;
import ace.actually.twp.screens.StatScreen;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Item;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TWP implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Identifier STAT_PACKET = new Identifier("twp","stat_packet");
	public static final Identifier BOOK_LOGIC = new Identifier("twp","book_logic_packet");
	public static final Logger LOGGER = LoggerFactory.getLogger("twp");

	public static final StatItem STAT_ITEM = new StatItem(new Item.Settings());
	public static final CheckItem CHECK_ITEM = new CheckItem(new Item.Settings());

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		Registry.register(Registry.ITEM,new Identifier("twp","stat"),STAT_ITEM);
		Registry.register(Registry.ITEM,new Identifier("twp","check"),CHECK_ITEM);
		LOGGER.info("Hello Fabric world!");

		ServerPlayNetworking.registerGlobalReceiver(STAT_PACKET,(server, player, handler, buf, responseSender) ->
		{
			String star = buf.readString();
			String command = buf.readString();
			String[] data = command.split(";");



			server.execute(()->
			{
				StatsAccessor accessor = (StatsAccessor) player;
				switch (data[0])
				{
					case "add":

						if(data[1].equals("strength"))
						{
							accessor.setStrength(accessor.getStrength()+Integer.parseInt(data[2]));
							System.out.println("set strength");
						}
						if(data[1].equals("unlock"))
						{
							//variable+data[2]
						}
				}
				accessor.addCompletedStar(star);
			});
		});

		ClientPlayNetworking.registerGlobalReceiver(BOOK_LOGIC, (client, handler, buf, responseSender) ->
		{
			String[] completed = buf.readString().split(",");
			client.execute(()->client.setScreen(new StatScreen(completed)));
		});
	}
}
