package minetweaker.mc1710;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.api.event.PlayerCraftedEvent;
import minetweaker.api.event.PlayerLoggedInEvent;
import minetweaker.api.event.PlayerLoggedOutEvent;
import minetweaker.api.event.PlayerSmeltedEvent;
import minetweaker.api.minecraft.MineTweakerMC;
import minetweaker.api.player.IPlayer;
import minetweaker.mc1710.network.MineTweakerLoadScriptsPacket;
import minetweaker.mc1710.recipes.MCCraftingInventory;
import minetweaker.mc1710.recipes.MCRecipeManager;

import net.minecraft.entity.player.EntityPlayerMP;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;

/**
 *
 * @author Stan
 */
public class FMLEventHandler {

    private static boolean shiftDown = false;

    public static boolean isShiftDown() {
        return shiftDown;
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent ev) {
        if (ev.phase == TickEvent.Phase.START) {
            shiftDown = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent ev) {
        if (ev.player instanceof EntityPlayerMP && MineTweakerConfig.sendLoadScripts) {
            EntityPlayerMP player = (EntityPlayerMP) ev.player;
            MineTweakerMod.NETWORK
                    .sendTo(new MineTweakerLoadScriptsPacket(MineTweakerAPI.tweaker.getScriptData()), player);
        }

        MineTweakerImplementationAPI.events
                .publishPlayerLoggedIn(new PlayerLoggedInEvent(MineTweakerMC.getIPlayer(ev.player)));
    }

    @SubscribeEvent
    public void onPlayerItemCrafted(PlayerEvent.ItemCraftedEvent ev) {
        IPlayer iPlayer = MineTweakerMC.getIPlayer(ev.player);
        if (MCRecipeManager.hasTransformerRecipes()) {
            MCRecipeManager.applyTransformations(MCCraftingInventory.get(ev.craftMatrix, ev.player), iPlayer);
        }

        if (MineTweakerImplementationAPI.events.hasPlayerCrafted()) {
            MineTweakerImplementationAPI.events.publishPlayerCrafted(
                    new PlayerCraftedEvent(
                            iPlayer,
                            MineTweakerMC.getIItemStack(ev.crafting),
                            MCCraftingInventory.get(ev.craftMatrix, ev.player)));
        }
    }

    @SubscribeEvent
    public void onPlayerItemSmelted(PlayerEvent.ItemSmeltedEvent ev) {
        if (MineTweakerImplementationAPI.events.hasPlayerSmelted()) {
            MineTweakerImplementationAPI.events.publishPlayerSmelted(
                    new PlayerSmeltedEvent(
                            MineTweakerMC.getIPlayer(ev.player),
                            MineTweakerMC.getIItemStack(ev.smelting)));
        }
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent ev) {
        MineTweakerImplementationAPI.events
                .publishPlayerLoggedOut(new PlayerLoggedOutEvent(MineTweakerMC.getIPlayer(ev.player)));
    }

}
