package net.rosemods.betteruiscale;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;

import java.nio.file.Path;

public class Main implements ClientModInitializer {
    public static final String MODID = "crispscale";
    public static final boolean IS_DEV = FabricLoader.getInstance().isDevelopmentEnvironment();
    public static final NamedLogger LOGGER = new NamedLogger(LogManager.getLogger(MODID), !IS_DEV);

    private static Config config;

    public static Config config() {
        return config;
    }

    @Override
    public void onInitializeClient() {
        config = Config.load(configPath());
        config.save(configPath());

        ResourceManagerHelper.get(PackType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public Identifier getFabricId() {
                return Identifier.fromNamespaceAndPath(MODID, "shader-injector");
            }

            @Override
            public void onResourceManagerReload(ResourceManager manager) {
                config.setFontSmoothingUniform();
            }
        });
    }

    public static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve(MODID + ".json");
    }
}
