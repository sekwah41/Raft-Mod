package com.epiicthundercat.raft.block;

import java.util.Random;

import com.epiicthundercat.raft.creativetab.RCreativeTab;
import com.epiicthundercat.raft.init.RBlocks;
import com.epiicthundercat.raft.world.WorldGenPalmTree;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.event.terraingen.TerrainGen;

public class BlockPalmSapling extends BlockBush implements IGrowable {
	public static final PropertyInteger STAGE = PropertyInteger.create("stage", 0, 1);

	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D,
			0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	public BlockPalmSapling(String name) {
		addToBlocks(this);
		this.setRegistryName(name.toLowerCase());
		this.setUnlocalizedName(name.toLowerCase());
		this.setCreativeTab(RCreativeTab.RTabs);
		this.setSoundType(SoundType.PLANT);

		this.setDefaultState(blockState.getBaseState().withProperty(STAGE, Integer.valueOf(0)));
	}

	private void addToBlocks(Block block) {
		RBlocks.blocks.add(block);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { STAGE });
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(STAGE, Integer.valueOf((meta & 8) >> 3));
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = 0;

		meta |= state.getValue(STAGE).intValue() << 3;

		return meta;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SAPLING_AABB;
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!world.isRemote) {
			super.updateTick(world, pos, state, rand);

			if (world.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
				grow(world, rand, pos, state);
			}
		}
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		if (state.getValue(STAGE).intValue() == 0) {
			world.setBlockState(pos, state.cycleProperty(STAGE), 3);
		} else {
			generateTree(world, pos, state, rand);
		}
	}

	public void generateTree(World world, BlockPos pos, IBlockState state, Random rand) {
		if (!TerrainGen.saplingGrowTree(world, rand, pos)) {
			return;
		}

		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);

		if (!WorldGenPalmTree.TREE_GEN.generate(world, rand, pos)) {
			world.setBlockState(pos, state, 3);
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return world.rand.nextFloat() < 0.2D;
	}
}
