package net.funkpla.spectral_core.mixin;

import static net.bennyboops.modid.PocketRepose.POCKET_DIMENSION_TYPE_ID;

import com.llamalad7.mixinextras.sugar.Local;
import de.dafuqs.spectrum.items.magic_items.PaintbrushItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.bennyboops.modid.PocketRepose;
import net.bennyboops.modid.block.SuitcaseBlock;
import net.bennyboops.modid.block.entity.SuitcaseBlockEntity;
import net.bennyboops.modid.world.Fantasy;
import net.bennyboops.modid.world.PortalChunkGenerator;
import net.bennyboops.modid.world.RuntimeWorldConfig;
import net.bennyboops.modid.world.RuntimeWorldHandle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SuitcaseBlock.class)
public class SuitcaseBlockMixin {
  @Inject(
      method = "use",
      at =
          @At(
              value = "INVOKE_ASSIGN",
              target =
                  "Lnet/bennyboops/modid/block/entity/SuitcaseBlockEntity;"
                      + "getBoundKeystoneName()Ljava/lang/String;"),
      cancellable = true)
  private void useWithPaintbrush(
      BlockState state,
      Level world,
      BlockPos pos,
      Player player,
      InteractionHand hand,
      BlockHitResult hit,
      CallbackInfoReturnable<InteractionResult> cir,
      @Local(name = "heldItem") ItemStack heldItem,
      @Local(name = "boundKeystone") String boundKeystone,
      @Local(name = "suitcase") SuitcaseBlockEntity suitcase) {
    if (heldItem.getItem() instanceof PaintbrushItem) {

      String keystoneName = player.getStringUUID();
      // Binding logic
      if (boundKeystone == null) {
        suitcase.bindKeystone(keystoneName);
        String dimensionName = "pocket_dimension_" + player.getStringUUID();
        world.playSound(
            null, pos, SoundEvents.LODESTONE_COMPASS_LOCK, SoundSource.BLOCKS, 2.0F, 0.0F);
        player.displayClientMessage(
            Component.translatable("item.spectral_seas.paintbrush.bound"), true);
        createOrLoadPersistentDimension(Objects.requireNonNull(world.getServer()), dimensionName);
        cir.setReturnValue(InteractionResult.SUCCESS);
      }
      if (boundKeystone != null && boundKeystone.equals(keystoneName)) {
        boolean newLockState = !suitcase.isLocked();
        suitcase.setLocked(newLockState);
        world.playSound(
            null,
            pos,
            newLockState ? SoundEvents.IRON_DOOR_CLOSE : SoundEvents.IRON_DOOR_OPEN,
            SoundSource.BLOCKS,
            0.3F,
            2.0F);
        player.displayClientMessage(
            newLockState
                ? Component.translatable("item.spectral_seas.paintbrush.locked")
                : Component.translatable("item.spectral_seas.paintbrush.unlocked"),
            true);
        cir.setReturnValue(InteractionResult.SUCCESS);
      }
    }
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
