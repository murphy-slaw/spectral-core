package net.funkpla.spectral_core.mixin;

import static net.bennyboops.modid.PocketRepose.POCKET_DIMENSION_TYPE_ID;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import de.dafuqs.spectrum.items.magic_items.PaintbrushItem;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import net.bennyboops.modid.PocketRepose;
import net.bennyboops.modid.world.Fantasy;
import net.bennyboops.modid.world.PortalChunkGenerator;
import net.bennyboops.modid.world.RuntimeWorldConfig;
import net.bennyboops.modid.world.RuntimeWorldHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelResource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(PaintbrushItem.class)
public abstract class PaintbrushItemMixin {

  @WrapMethod(method = "use")
  public InteractionResultHolder<ItemStack> use(
      Level world,
      Player player,
      InteractionHand hand,
      Operation<InteractionResultHolder<ItemStack>> original) {

    ItemStack stack = player.getItemInHand(hand);
    if (world.isClientSide) {
      return InteractionResultHolder.sidedSuccess(stack, true);
    }
    String dimensionName = "pocket_dimension_" + player.getStringUUID();

    createOrLoadPersistentDimension(Objects.requireNonNull(world.getServer()), dimensionName);

    // Mark keystone as bound
    world.playSound(
        null,
        player.getX(),
        player.getY(),
        player.getZ(),
        SoundEvents.AMETHYST_CLUSTER_FALL,
        SoundSource.PLAYERS,
        2.0F,
        2.0F);
    return InteractionResultHolder.sidedSuccess(stack, false);
  }

  @Unique
  private void createOrLoadPersistentDimension(MinecraftServer server, String dimensionName) {
    ResourceLocation worldId = new ResourceLocation("pocket-repose", dimensionName);

    Path worldSavePath =
        server
            .getWorldPath(LevelResource.ROOT)
            .resolve("dimensions")
            .resolve("pocket-repose")
            .resolve(dimensionName);
    boolean dimensionExists = Files.exists(worldSavePath);

    ResourceKey<DimensionType> typeKey =
        ResourceKey.create(Registries.DIMENSION_TYPE, POCKET_DIMENSION_TYPE_ID);

    Registry<Biome> biomeRegistry = server.registryAccess().registryOrThrow(Registries.BIOME);

    ChunkGenerator generator = new PortalChunkGenerator(biomeRegistry);

    long seed = server.overworld().getSeed();

    RuntimeWorldConfig config =
        new RuntimeWorldConfig().setDimensionType(typeKey).setGenerator(generator).setSeed(seed);

    RuntimeWorldHandle handle = Fantasy.get(server).getOrOpenPersistentWorld(worldId, config);

    registerDimension(server, dimensionName);

    if (!dimensionExists) {
      ServerLevel world = handle.asWorld();
      placeStructureImmediately(server, world, dimensionName);
      System.out.println("Created new dimension with structure: " + dimensionName);
    } else {
      System.out.println("Loaded existing dimension: " + dimensionName);
    }
  }

  @Unique
  private void placeStructureImmediately(
      MinecraftServer server, ServerLevel world, String dimensionName) {
    try {
      StructureTemplate template =
          server
              .getStructureManager()
              .get(new ResourceLocation("pocket-repose", "pocket_island_01"))
              .orElse(null);

      if (template != null) {
        BlockPos pos = new BlockPos(0, 64, 0);

        world.getChunk(pos);

        template.placeInWorld(
            world,
            pos,
            pos,
            new StructurePlaceSettings()
                .setMirror(Mirror.NONE)
                .setRotation(Rotation.NONE)
                .setIgnoreEntities(false),
            world.getRandom(),
            Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);

        System.out.println(
            "Immediately placed pocket island structure in new dimension: " + dimensionName);
      } else {
        System.err.println("Could not find structure template: pocket_island_01");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Unique
  private void registerDimension(MinecraftServer server, String dimensionName) {
    Path registryDir =
        server
            .getWorldPath(LevelResource.ROOT)
            .resolve("data")
            .resolve("pocket-repose")
            .resolve("dimension_registry");

    try {
      Files.createDirectories(registryDir);
      Path registryFile = registryDir.resolve("registry.txt");

      // load existing lines
      Set<String> dims = new HashSet<>();
      if (Files.exists(registryFile)) {
        dims.addAll(Files.readAllLines(registryFile));
      }

      // add + save only if new
      if (dims.add(dimensionName)) {
        Files.write(registryFile, dims);
      }
    } catch (IOException e) {
      PocketRepose.LOGGER.error("Failed to write dimension registry", e);
    }
  }
}
