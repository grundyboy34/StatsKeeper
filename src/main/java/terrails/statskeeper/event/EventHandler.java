package terrails.statskeeper.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import terrails.statskeeper.config.ConfigHandler;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void keepFoodStats(PlayerEvent.Clone player) {

        if (player.isWasDeath() && ConfigHandler.keepHunger) {
            int originalFoodLevel = player.getOriginal().getFoodStats().getFoodLevel();
            int hungerValue = ConfigHandler.minHungerAmount >= originalFoodLevel ? ConfigHandler.minHungerAmount : originalFoodLevel;
            player.getEntityPlayer().getFoodStats().setFoodLevel(hungerValue);
        }

        if (player.isWasDeath() && ConfigHandler.keepSaturation) {
            FoodStats foodStats = player.getEntityPlayer().getFoodStats();
            float originalSaturationLevel = player.getOriginal().getFoodStats().getSaturationLevel();
            float saturationLevel = ConfigHandler.minSaturationAmount >= originalSaturationLevel ? ConfigHandler.minSaturationAmount : originalSaturationLevel;
            setServerSaturationLevel(foodStats, saturationLevel);
        }
    }

    @SubscribeEvent
    public static void keepExp(PlayerEvent.Clone player) {
        boolean checkGameRule = player.getOriginal().getEntityWorld().getGameRules().getBoolean("keepInventory");
        if (ConfigHandler.keepXP && player.isWasDeath() && !checkGameRule) {
            player.getEntityPlayer().addExperience(player.getOriginal().experienceTotal);
        }
    }

    @SubscribeEvent
    public static void dontDropExp(LivingExperienceDropEvent event) {
        if (!ConfigHandler.dropXP && event.getEntity() instanceof EntityPlayer) {
            event.setCanceled(true);
        }
    }

    private static void setServerSaturationLevel(FoodStats theStats, float saturationAmount) {
        try {
            Field setSaturationLevel = ReflectionHelper.findField(FoodStats.class, "foodSaturationLevel", "field_75125_b", "b");
            setSaturationLevel.set(theStats, saturationAmount);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}

