package com.mcupdater.safespawn.world;

import com.mcupdater.safespawn.SafeSpawn;
import com.mcupdater.safespawn.setup.Config;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.loot.LootTables;
import net.minecraft.state.properties.*;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

import static com.mcupdater.safespawn.setup.Registration.SPAWNHEARTBLOCK;

public class SpawnFortFeature extends Feature<NoFeatureConfig> {
    public SpawnFortFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(ISeedReader worldGen, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, NoFeatureConfig noFeatureConfig) {
        SafeSpawn.LOGGER.info("Generating at " + blockPos.toShortString());
        clearArea(worldGen, blockPos);
        createFloor(worldGen, blockPos, random);
        createWalls(worldGen, blockPos, random);
        createTrim(worldGen, blockPos, random);
        createDais(worldGen, blockPos);
        createRailing(worldGen, blockPos);
        decorate(worldGen, blockPos, random);
        return true;
    }

    private void createTrim(ISeedReader worldGen, BlockPos blockPos, Random random) {
        int xOffset = blockPos.getX();
        int yOffset = blockPos.getY();
        int zOffset = blockPos.getZ();
        for (int x = -11; x <= 11; x++){
            for (int z = -11; z <= 11; z++){
                if (Math.abs(x) == 11 || Math.abs(z) == 11) {
                    BlockState block = getStoneBlock(random,true);
                    block = block.setValue(BlockStateProperties.HALF, Half.TOP);
                    block = block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT);
                    if (x == -11) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
                    if (z == -11) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
                    if (x == 11) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
                    if (z == 11) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
                    if (Math.abs(x) == Math.abs(z)) block = block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_LEFT);
                    BlockPos pos = new BlockPos(x+xOffset,yOffset+3,z+zOffset);
                    worldGen.setBlock(pos, block, 2);
                }
            }
        }
        for (int x = -9; x <= 9; x++){
            for (int z = -9; z <= 9; z++){
                if ((Math.abs(x) == 9 || Math.abs(z) == 9)) {
                    if (!(Math.abs(x) == 4 || Math.abs(x) == 6 || Math.abs(z) == 4 || Math.abs(z) == 6)) {
                        int xOffset2 = 0;
                        int zOffset2 = 0;
                        BlockState block = getStoneBlock(random, true);
                        block = block.setValue(BlockStateProperties.HALF, Half.TOP);
                        block = block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT);
                        if (x == 9) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
                        if (z == 9) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
                        if (x == -9) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
                        if (z == -9) block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
                        if (Math.abs(x) == 5) zOffset2 = z<0 ? 1 : -1;
                        if (Math.abs(z) == 5) xOffset2 = x<0 ? 1 : -1;
                        if (Math.abs(x) == Math.abs(z)) block = block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_LEFT);
                        BlockPos pos = new BlockPos(x + xOffset + xOffset2, yOffset + 3, z + zOffset + zOffset2);
                        worldGen.setBlock(pos, block, 2);
                    }
                    createNook(worldGen, random, blockPos.north(9).west(6).above(3), Direction.NORTH);
                    createNook(worldGen, random, blockPos.north(9).east(4).above(3), Direction.NORTH);
                    createNook(worldGen, random, blockPos.east(9).north(6).above(3), Direction.EAST);
                    createNook(worldGen, random, blockPos.east(9).south(4).above(3), Direction.EAST);
                    createNook(worldGen, random, blockPos.south(9).east(6).above(3), Direction.SOUTH);
                    createNook(worldGen, random, blockPos.south(9).west(4).above(3), Direction.SOUTH);
                    createNook(worldGen, random, blockPos.west(9).south(6).above(3), Direction.WEST);
                    createNook(worldGen, random, blockPos.west(9).north(4).above(3), Direction.WEST);
                }
            }
        }
    }

    private void createNook(ISeedReader worldGen, Random random, BlockPos nookPos, Direction facing) {
        for (int i = 0; i < 4; i++) {
            BlockState block = getStoneBlock(random, true);
            block = block.setValue(BlockStateProperties.HALF, Half.TOP).setValue(BlockStateProperties.HORIZONTAL_FACING, facing);
            if (i == 0) {
                block=block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_RIGHT);
                worldGen.setBlock(nookPos, block, 3);
            }
            if (i == 1){
                block=block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_LEFT).setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getClockWise());
                worldGen.setBlock(nookPos.relative(facing.getOpposite(),1),block,3);
            }
            if (i == 2){
                block=block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_RIGHT).setValue(BlockStateProperties.HORIZONTAL_FACING, facing.getCounterClockWise());
                worldGen.setBlock(nookPos.relative(facing.getOpposite(), 1).relative(facing.getClockWise(),2),block,3);
            }
            if (i == 3){
                block=block.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.INNER_LEFT);
                worldGen.setBlock(nookPos.relative(facing.getClockWise(),2),block,3);
            }
        }
    }

    private void clearArea(ISeedReader worldGen, BlockPos blockPos) {
        int level = blockPos.getY();
        int minX = blockPos.getX()-15;
        int maxX = blockPos.getX()+15;
        int minZ = blockPos.getZ()-15;
        int maxZ = blockPos.getZ()+15;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                BlockPos pos = new BlockPos(x, level, z);
                clearAbove(worldGen, pos);
                fillBelow(worldGen, pos);
            }
        }
        if (Config.EXTEND_PATHS.get()) {
            probeRoute(worldGen, blockPos.north(16), Direction.NORTH, 0);
            probeRoute(worldGen, blockPos.east(16), Direction.EAST, 0);
            probeRoute(worldGen, blockPos.south(16), Direction.SOUTH, 0);
            probeRoute(worldGen, blockPos.west(16), Direction.WEST, 0);
        }
    }

    private void probeRoute(ISeedReader worldGen, BlockPos blockPos, Direction direction, int recurse) {
        if (recurse > 50) {
            return;
        }
        BlockState blockState = Blocks.COBBLESTONE_STAIRS.defaultBlockState().setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT).setValue(BlockStateProperties.HALF, Half.BOTTOM).setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        SafeSpawn.LOGGER.info("probeRoute " + direction.getName() + " (" + blockPos.toShortString() + ") " + worldGen.getBlockState(blockPos).toString());
        if (worldGen.getBlockState(blockPos).canBeReplacedByLeaves(worldGen, blockPos)) {
            SafeSpawn.LOGGER.info("Probe: " + direction.getName() + " " + recurse + " Stairs down");
            worldGen.setBlock(blockPos,blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 3);
            clearAbove(worldGen, blockPos.above());
            worldGen.setBlock(blockPos.relative(direction.getCounterClockWise()),blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 3);
            clearAbove(worldGen, blockPos.above().relative(direction.getCounterClockWise()));
            worldGen.setBlock(blockPos.relative(direction.getClockWise()),blockState.setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()), 3);
            clearAbove(worldGen, blockPos.above().relative(direction.getClockWise()));
            if (worldGen.getBlockState(blockPos.below()).isAir()) {
                worldGen.setBlock(blockPos.below(), blockState.setValue(BlockStateProperties.HALF, Half.TOP), 3);
                worldGen.setBlock(blockPos.below().relative(direction.getCounterClockWise()), blockState.setValue(BlockStateProperties.HALF, Half.TOP), 3);
                worldGen.setBlock(blockPos.below().relative(direction.getClockWise()), blockState.setValue(BlockStateProperties.HALF, Half.TOP), 3);
            }
            probeRoute(worldGen, blockPos.below().relative(direction), direction, recurse+1);
            if (worldGen.getBlockState(blockPos.below()).is(Blocks.WATER)) {
                SafeSpawn.LOGGER.info("Probe: " + direction.getName() + " " + recurse + " Dock");
                buildDock(worldGen, blockPos.below(), direction);
            }
            return;
        } else if (!worldGen.getBlockState(blockPos.above()).canBeReplacedByLeaves(worldGen, blockPos)){
            SafeSpawn.LOGGER.info("Probe: " + direction.getName() + " " + recurse + " Stairs up");
            worldGen.setBlock(blockPos.above(1), blockState,3);
            clearAbove(worldGen, blockPos.above(2));
            worldGen.setBlock(blockPos.above(1).relative(direction.getCounterClockWise()), blockState,3);
            clearAbove(worldGen, blockPos.above(2).relative(direction.getCounterClockWise()));
            worldGen.setBlock(blockPos.above(1).relative(direction.getClockWise()), blockState,3);
            clearAbove(worldGen, blockPos.above(2).relative(direction.getClockWise()));
            probeRoute(worldGen, blockPos.above().relative(direction), direction, recurse+1);
        }
    }

    private void clearAbove(ISeedReader worldGen, BlockPos blockPos) {
        for (int i = 0; i <= 256; i++) {
            worldGen.removeBlock(blockPos.above(i), true);
            if (worldGen.canSeeSky(blockPos.above())) {
                return;
            }
        }
    }

    private void fillBelow(ISeedReader worldGen, BlockPos blockPos) {
        for (int i = 0; i <= 128; i++) {
            if (i <10 || worldGen.isEmptyBlock(blockPos.below(i+1)) || worldGen.containsAnyLiquid(AxisAlignedBB.unitCubeFromLowerCorner(Vector3d.atCenterOf(blockPos.below(i+1))))) {
                worldGen.setBlock(blockPos.below(i+1), blockPos.below(i+1).getY() < 1 ? Blocks.BEDROCK.defaultBlockState() : (i<5 ? Blocks.DIRT.defaultBlockState() : Blocks.STONE.defaultBlockState()), 3);
            } else {
                return;
            }
        }
    }

    private void buildDock(ISeedReader worldGen, BlockPos blockPos, Direction direction) {
        BlockState dock = Blocks.OAK_PLANKS.defaultBlockState();
        BlockState stairs = Blocks.OAK_STAIRS.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite()).setValue(BlockStateProperties.HALF, Half.BOTTOM).setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT);
        for (int i = 0; i <= 5; i++) {
            clearAbove(worldGen,blockPos.relative(direction, i));
            worldGen.setBlock(blockPos.relative(direction, i), dock, 3);
            clearAbove(worldGen,blockPos.relative(direction, i).relative(direction.getClockWise()));
            worldGen.setBlock(blockPos.relative(direction, i).relative(direction.getClockWise()), dock, 3);
            clearAbove(worldGen,blockPos.relative(direction, i).relative(direction.getCounterClockWise()));
            worldGen.setBlock(blockPos.relative(direction, i).relative(direction.getCounterClockWise()), dock, 3);
            if (i == 0) {
                worldGen.setBlock(blockPos.above().relative(direction, i), stairs, 3);
                worldGen.setBlock(blockPos.above().relative(direction, i).relative(direction.getClockWise()), stairs, 3);
                worldGen.setBlock(blockPos.above().relative(direction, i).relative(direction.getCounterClockWise()), stairs, 3);
            }
            if (i == 5) {
                placePerimeterLantern(worldGen, blockPos.relative(direction, i).relative(direction.getCounterClockWise()));
                placePerimeterLantern(worldGen, blockPos.relative(direction, i).relative(direction.getClockWise()));
            }
        }
    }

    private void decorate(ISeedReader worldGen, BlockPos blockPos, Random random) {
        placeFloorlights(worldGen, blockPos);

        // Place carpets
        placeCarpet(worldGen, blockPos.above(), Direction.NORTH,3,9);
        placeCarpet(worldGen, blockPos.above(), Direction.EAST,3,9);
        placeCarpet(worldGen, blockPos.above(), Direction.SOUTH,3,9);
        placeCarpet(worldGen, blockPos.above(), Direction.WEST,3,9);

        // Place doors
        placeDoor(worldGen,blockPos.above(),Direction.NORTH,10);
        placeDoor(worldGen,blockPos.above(),Direction.EAST,10);
        placeDoor(worldGen,blockPos.above(),Direction.SOUTH,10);
        placeDoor(worldGen,blockPos.above(),Direction.WEST,10);

        // Place corner crafting tables
        BlockState craftingTable = Blocks.CRAFTING_TABLE.defaultBlockState();
        worldGen.setBlock(blockPos.offset(-9,1,-9),craftingTable,2);
        worldGen.setBlock(blockPos.offset(-9,1,9),craftingTable,2);
        worldGen.setBlock(blockPos.offset(9,1,9),craftingTable,2);
        worldGen.setBlock(blockPos.offset(9,1,-9),craftingTable,2);

        // Place perimeter lanterns
        placePerimeterLantern(worldGen, blockPos.north(13).west(3));
        placePerimeterLantern(worldGen, blockPos.north(13).west(8));
        placePerimeterLantern(worldGen, blockPos.north(13).east(3));
        placePerimeterLantern(worldGen, blockPos.north(13).east(8));
        placePerimeterLantern(worldGen, blockPos.north(13).east(13));
        placePerimeterLantern(worldGen, blockPos.east(13).north(3));
        placePerimeterLantern(worldGen, blockPos.east(13).north(8));
        placePerimeterLantern(worldGen, blockPos.east(13).south(3));
        placePerimeterLantern(worldGen, blockPos.east(13).south(8));
        placePerimeterLantern(worldGen, blockPos.east(13).south(13));
        placePerimeterLantern(worldGen, blockPos.south(13).east(3));
        placePerimeterLantern(worldGen, blockPos.south(13).east(8));
        placePerimeterLantern(worldGen, blockPos.south(13).west(3));
        placePerimeterLantern(worldGen, blockPos.south(13).west(8));
        placePerimeterLantern(worldGen, blockPos.south(13).west(13));
        placePerimeterLantern(worldGen, blockPos.west(13).south(3));
        placePerimeterLantern(worldGen, blockPos.west(13).south(8));
        placePerimeterLantern(worldGen, blockPos.west(13).north(3));
        placePerimeterLantern(worldGen, blockPos.west(13).north(8));
        placePerimeterLantern(worldGen, blockPos.west(13).north(13));

        // Place catwalk lanterns
        BlockState railingLantern = Blocks.LANTERN.defaultBlockState();
        worldGen.setBlock(blockPos.above(5).north(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).north(9).east(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).east(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).east(9).south(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).south(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).south(9).west(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).west(9), railingLantern, 3);
        worldGen.setBlock(blockPos.above(5).west(9).north(9), railingLantern, 3);

        // Place ladders
        placeLadder(worldGen, blockPos.above().north(9).west(5), Direction.NORTH);
        placeLadder(worldGen, blockPos.above().north(9).east(5), Direction.NORTH);
        placeLadder(worldGen, blockPos.above().east(9).north(5), Direction.EAST);
        placeLadder(worldGen, blockPos.above().east(9).south(5), Direction.EAST);
        placeLadder(worldGen, blockPos.above().south(9).east(5), Direction.SOUTH);
        placeLadder(worldGen, blockPos.above().south(9).west(5), Direction.SOUTH);
        placeLadder(worldGen, blockPos.above().west(9).south(5), Direction.WEST);
        placeLadder(worldGen, blockPos.above().west(9).north(5), Direction.WEST);

        // Place windows
        placeWindow(worldGen, blockPos.above(2).north(10).west(7), Direction.NORTH);
        placeWindow(worldGen, blockPos.above(2).north(10).west(3), Direction.NORTH);
        placeWindow(worldGen, blockPos.above(2).north(10).east(3), Direction.NORTH);
        placeWindow(worldGen, blockPos.above(2).north(10).east(7), Direction.NORTH);
        placeWindow(worldGen, blockPos.above(2).east(10).north(7), Direction.EAST);
        placeWindow(worldGen, blockPos.above(2).east(10).north(3), Direction.EAST);
        placeWindow(worldGen, blockPos.above(2).east(10).south(3), Direction.EAST);
        placeWindow(worldGen, blockPos.above(2).east(10).south(7), Direction.EAST);
        placeWindow(worldGen, blockPos.above(2).south(10).east(7), Direction.SOUTH);
        placeWindow(worldGen, blockPos.above(2).south(10).east(3), Direction.SOUTH);
        placeWindow(worldGen, blockPos.above(2).south(10).west(3), Direction.SOUTH);
        placeWindow(worldGen, blockPos.above(2).south(10).west(7), Direction.SOUTH);
        placeWindow(worldGen, blockPos.above(2).west(10).south(7), Direction.WEST);
        placeWindow(worldGen, blockPos.above(2).west(10).south(3), Direction.WEST);
        placeWindow(worldGen, blockPos.above(2).west(10).north(3), Direction.WEST);
        placeWindow(worldGen, blockPos.above(2).west(10).north(7), Direction.WEST);

        // Place interior lights
        placeLampPost(worldGen, blockPos.above().north(5).west(5));
        placeLampPost(worldGen, blockPos.above().west(5).south(5));
        placeLampPost(worldGen, blockPos.above().south(5).east(5));
        placeLampPost(worldGen, blockPos.above().east(5).north(5));

        // Place beds
        placeBed(worldGen, blockPos.above().north(9).west(7), random, Direction.WEST);
        placeBed(worldGen, blockPos.above().north(9).east(7), random, Direction.EAST);
        placeBed(worldGen, blockPos.above().east(9).north(7), random, Direction.NORTH);
        placeBed(worldGen, blockPos.above().east(9).south(7), random, Direction.SOUTH);
        placeBed(worldGen, blockPos.above().south(9).east(7), random, Direction.EAST);
        placeBed(worldGen, blockPos.above().south(9).west(7), random, Direction.WEST);
        placeBed(worldGen, blockPos.above().west(9).south(7), random, Direction.SOUTH);
        placeBed(worldGen, blockPos.above().west(9).north(7), random, Direction.NORTH);

        // Place chests
        placeChest(worldGen, blockPos.above().north(9).west(3), Direction.SOUTH, random);
        placeChest(worldGen, blockPos.above().north(9).east(3), Direction.SOUTH, random);
        placeChest(worldGen, blockPos.above().east(9).north(3), Direction.WEST, random);
        placeChest(worldGen, blockPos.above().east(9).south(3), Direction.WEST, random);
        placeChest(worldGen, blockPos.above().south(9).west(3), Direction.NORTH, random);
        placeChest(worldGen, blockPos.above().south(9).east(3), Direction.NORTH, random);
        placeChest(worldGen, blockPos.above().west(9).south(3), Direction.EAST, random);
        placeChest(worldGen, blockPos.above().west(9).north(3), Direction.EAST, random);
    }

    private void placeChest(ISeedReader worldGen, BlockPos blockPos, Direction direction, Random random) {
        BlockState block = Blocks.CHEST.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        worldGen.setBlock(blockPos, block, 3);
        LockableLootTileEntity.setLootTable(worldGen,random,blockPos, LootTables.SPAWN_BONUS_CHEST);
    }

    private void placeLampPost(ISeedReader worldGen, BlockPos blockPos) {
        BlockState post = Blocks.STONE_BRICK_WALL.defaultBlockState();
        BlockState lamp = Blocks.SEA_LANTERN.defaultBlockState();
        worldGen.setBlock(blockPos, post, 3);
        worldGen.setBlock(blockPos.above(), post, 3);
        worldGen.setBlock(blockPos.above(2), lamp, 3);
    }

    private void placeBed(ISeedReader worldGen, BlockPos blockPos, Random random, Direction direction) {
        BlockState block = null;
        switch(random.nextInt(16)){
            case 0:
                block = Blocks.WHITE_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 1:
                block = Blocks.ORANGE_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 2:
                block = Blocks.MAGENTA_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 3:
                block = Blocks.LIGHT_BLUE_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 4:
                block = Blocks.YELLOW_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 5:
                block = Blocks.LIME_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 6:
                block = Blocks.PINK_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 7:
                block = Blocks.GRAY_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 8:
                block = Blocks.LIGHT_GRAY_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 9:
                block = Blocks.CYAN_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 10:
                block = Blocks.PURPLE_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 11:
                block = Blocks.BLUE_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 12:
                block = Blocks.BROWN_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 13:
                block = Blocks.GREEN_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 14:
                block = Blocks.RED_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
            case 15:
                block = Blocks.BLACK_BED.defaultBlockState().setValue(BlockStateProperties.BED_PART, BedPart.FOOT);
                break;
        }
        block = block.setValue(BlockStateProperties.HORIZONTAL_FACING, direction);
        worldGen.setBlock(blockPos, block, 3);
        worldGen.setBlock(blockPos.relative(direction), block.setValue(BlockStateProperties.BED_PART, BedPart.HEAD), 3);
    }

    private void placeWindow(ISeedReader worldGen, BlockPos blockPos, Direction direction) {
        BlockState block = Blocks.GLASS_PANE.defaultBlockState()
                .setValue(BlockStateProperties.NORTH,direction.getAxis().equals(Direction.Axis.X))
                .setValue(BlockStateProperties.SOUTH,direction.getAxis().equals(Direction.Axis.X))
                .setValue(BlockStateProperties.EAST,direction.getAxis().equals(Direction.Axis.Z))
                .setValue(BlockStateProperties.WEST,direction.getAxis().equals(Direction.Axis.Z));
        worldGen.setBlock(blockPos, block, 3);
        worldGen.setBlock(blockPos.relative(direction.getCounterClockWise()),block,3);
        worldGen.setBlock(blockPos.relative(direction.getClockWise()),block,3);
    }

    private void placeLadder(ISeedReader worldGen, BlockPos blockPos, Direction direction) {
        BlockState block = Blocks.LADDER.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
        worldGen.setBlock(blockPos, block, 3);
        worldGen.setBlock(blockPos.above(), block, 3);
        worldGen.setBlock(blockPos.above(2), block, 3);
    }

    private void placePerimeterLantern(ISeedReader worldGen, BlockPos blockPos) {
        worldGen.setBlock(blockPos.above(), Blocks.OAK_FENCE.defaultBlockState(), 3);
        worldGen.setBlock(blockPos.above(2), Blocks.SOUL_LANTERN.defaultBlockState(), 3);
    }


    private void placeDoor(ISeedReader worldGen, BlockPos blockPos, Direction direction, int offset) {
        BlockState block = Blocks.OAK_DOOR.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction.getOpposite());
        worldGen.setBlock(blockPos.relative(direction,offset), block.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER),3);
        worldGen.setBlock(blockPos.relative(direction,offset).above(), block.setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER),3);
    }

    private void placeCarpet(ISeedReader worldGen, BlockPos blockPos, Direction direction, int start, int end) {
        BlockState mainCarpet = Config.getPrimaryCarpet();
        BlockState sideCarpet = Config.getSecondaryCarpet();
        for (int i = start; i <= end; i++){
            worldGen.setBlock(blockPos.relative(direction, i),mainCarpet,3);
            worldGen.setBlock(blockPos.relative(direction, i).relative(direction.getClockWise(),1),sideCarpet,3);
            worldGen.setBlock(blockPos.relative(direction, i).relative(direction.getCounterClockWise(),1),sideCarpet,3);
        }
    }

    private void placeFloorlights(ISeedReader worldGen, BlockPos blockPos) {
        BlockState block = Blocks.SEA_LANTERN.defaultBlockState();
        worldGen.setBlock(blockPos.north(9), block, 3);
        worldGen.setBlock(blockPos.east(9), block, 3);
        worldGen.setBlock(blockPos.south(9), block, 3);
        worldGen.setBlock(blockPos.west(9), block, 3);
    }

    private void createRailing(ISeedReader worldGen, BlockPos blockPos) {
        int xOffset = blockPos.getX();
        int yOffset = blockPos.getY();
        int zOffset = blockPos.getZ();
        BlockState block = Blocks.OAK_FENCE.defaultBlockState();
        for (int x = -11; x <= 11; x++){
            for (int z = -11; z <= 11; z++){
                if (Math.abs(x) == 11 || Math.abs(z) == 11) {
                    BlockPos pos = new BlockPos(x+xOffset,yOffset+4,z+zOffset);
                    worldGen.setBlock(pos, block, 3);
                }
            }
        }
        for (int x = -9; x <= 9; x++){
            for (int z = -9; z <= 9; z++){
                if ((Math.abs(x) == 9 || Math.abs(z) == 9)) {
                    int xOffset2 = 0;
                    int zOffset2 = 0;
                    if (Math.abs(x) == 5) zOffset2 = z<0 ? 1 : -1;
                    if (Math.abs(z) == 5) xOffset2 = x<0 ? 1 : -1;
                    BlockPos pos = new BlockPos(x + xOffset + xOffset2, yOffset + 4, z + zOffset + zOffset2);
                    worldGen.setBlock(pos, block, 3);
                    if (xOffset2 != 0){
                        worldGen.setBlock(pos.north(), block, 3);
                        worldGen.setBlock(pos.south(), block, 3);
                    }
                    if (zOffset2 != 0){
                        worldGen.setBlock(pos.east(), block, 3);
                        worldGen.setBlock(pos.west(), block, 3);
                    }
                }
            }
        }
    }

    private void createDais(ISeedReader worldGen, BlockPos blockPos) {
        BlockState blockSolid = Blocks.POLISHED_DIORITE.defaultBlockState();
        BlockState blockStair = Blocks.POLISHED_DIORITE_STAIRS.defaultBlockState();
        for (int x=-2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                blockStair = blockStair.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT);
                if (x == -2) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
                if (z == -2) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
                if (x == 2) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
                if (z == 2) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
                if (Math.abs(x) == Math.abs(z)) blockStair = blockStair.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_LEFT);
                worldGen.setBlock(blockPos.above(1).offset(x,0,z), (Math.abs(x) == 2 || Math.abs(z) == 2) ? blockStair : blockSolid, 3);
            }
        }
        for (int x=-1; x <= 1; x++) {
            for (int z = -1; z <= 1; z++) {
                blockStair = blockStair.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.STRAIGHT);
                if (x == -1) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.EAST);
                if (z == -1) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
                if (x == 1) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST);
                if (z == 1) blockStair = blockStair.setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
                if (Math.abs(x) == Math.abs(z)) blockStair = blockStair.setValue(BlockStateProperties.STAIRS_SHAPE, StairsShape.OUTER_LEFT);
                worldGen.setBlock(blockPos.above(2).offset(x,0,z), (Math.abs(x) == 1 || Math.abs(z) == 1) ? blockStair : blockSolid, 3);
            }
        }
        worldGen.setBlock(blockPos.above(), SPAWNHEARTBLOCK.get().defaultBlockState(), 3);
        worldGen.setBlock(blockPos.above(3), Config.getDaisFocal(), 3);
    }

    private void createWalls(ISeedReader worldGen, BlockPos blockPos, Random random) {
        int xOffset = blockPos.getX();
        int yOffset = blockPos.getY();
        int zOffset = blockPos.getZ();
        for (int x = -10; x <= 10; x++){
            for (int z = -10; z <= 10; z++){
                for (int y = 1; y <= 3; y++) {
                    if (Math.abs(x) == 10 || Math.abs(z) == 10) {
                        BlockState block = getStoneBlock(random,false);
                        BlockPos pos = new BlockPos(x+xOffset,y+yOffset,z+zOffset);
                        worldGen.setBlock(pos, block, 2);
                    }
                }
            }
        }
    }

    private void createFloor(ISeedReader worldGen, BlockPos blockPos, Random random) {
        int y = blockPos.getY();
        int xOffset = blockPos.getX();
        int zOffset = blockPos.getZ();
        for (int x = -15; x <= 15; x++){
            for (int z = -15; z <= 15; z++){
                BlockState block = Blocks.GRASS_BLOCK.defaultBlockState();
                if ((x >= -12 && x <= 12 && z >= -12 && z <= 12) || ((x < -10 || x > 10) && (z >= -1 && z <= 1)) ||  ((z < -10 || z > 10) && (x >= -1 && x <= 1))) {
                    block = Blocks.GRASS_PATH.defaultBlockState();
                }
                if ((x >= -10 && x <= 10) && (z >= -10 && z <= 10)) {
                    block = getStoneBlock(random,false);
                }
                worldGen.setBlock(new BlockPos(x+xOffset,y,z+zOffset), block, 2);
            }
        }
    }

    private BlockState getStoneBlock(Random random, boolean stairs) {
        if (stairs){
            return random.nextBoolean() ? Blocks.STONE_BRICK_STAIRS.defaultBlockState() : Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState();
        } else {
            switch (random.nextInt(3)) {
                case 0:
                    return Blocks.CRACKED_STONE_BRICKS.defaultBlockState();
                case 1:
                    return Blocks.STONE_BRICKS.defaultBlockState();
                case 2:
                    return Blocks.MOSSY_STONE_BRICKS.defaultBlockState();
            }
        }
        return null;
    }
}
