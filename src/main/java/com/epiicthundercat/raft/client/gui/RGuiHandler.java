package com.epiicthundercat.raft.client.gui;

import com.epiicthundercat.raft.block.BlockSeperator;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class RGuiHandler implements IGuiHandler{
	
	public static int GUIID = 1;
	public static int GUIID2 = 2;
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		// TODO Auto-generated method stub
		return null;
	}
	
}