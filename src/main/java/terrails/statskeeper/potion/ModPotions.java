package terrails.statskeeper.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import terrails.statskeeper.Constants;
import terrails.terracore.registry.PotionRegistry;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class ModPotions extends PotionRegistry {

    private static Map<String, Potion> potionMap = new HashMap<>();

    public static void init() {
        setPotionMap(potionMap, Constants.MOD_NAME);
        addPotion("appetite", new NoAppetiteEffect("appetite"));
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        event.getRegistry().registerAll(getPotions());
    }
}