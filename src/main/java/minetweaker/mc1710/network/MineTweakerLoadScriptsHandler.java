/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */

package minetweaker.mc1710.network;

import minetweaker.MineTweakerAPI;
import minetweaker.MineTweakerImplementationAPI;
import minetweaker.mc1710.MineTweakerConfig;
import minetweaker.runtime.providers.ScriptProviderMemory;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

/**
 *
 * @author Stan
 */
public class MineTweakerLoadScriptsHandler implements IMessageHandler<MineTweakerLoadScriptsPacket, IMessage> {

    @Override
    public IMessage onMessage(MineTweakerLoadScriptsPacket message, MessageContext ctx) {
        if (MineTweakerAPI.server == null && MineTweakerConfig.handleLoadScripts) {
            MineTweakerImplementationAPI.setScriptProvider(new ScriptProviderMemory(message.getData()));
            MineTweakerImplementationAPI.reload();
        }
        return null;
    }
}
