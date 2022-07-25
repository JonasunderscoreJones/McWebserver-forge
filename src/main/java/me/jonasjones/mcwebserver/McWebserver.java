package me.jonasjones.mcwebserver;

import com.mojang.logging.LogUtils;
import me.jonasjones.mcwebserver.config.ModConfigs;
import me.jonasjones.mcwebserver.web.HTTPServer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.net.Socket;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(McWebserver.MODID)
public class McWebserver
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "mcwebserver";
    public static String MOD_ID = "mcwebserver";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final Logger VERBOSELOGGER = LogUtils.getLogger();

    public McWebserver()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        LOGGER.info("McWebserver initialized!");

        // register configs
        ModConfigs.registerConfigs();

        if (ModConfigs.IS_ENABLED) {
            LOGGER.info("Starting Webserver...");

            new Thread(() -> {
                new HTTPServer(new Socket());
                HTTPServer.main();
            }).start();
        } else {
            LOGGER.info("Webserver disabled in the config file.");
        }
    }
}
