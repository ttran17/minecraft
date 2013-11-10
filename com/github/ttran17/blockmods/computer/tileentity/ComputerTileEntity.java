package com.github.ttran17.blockmods.computer.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class ComputerTileEntity extends TileEntity {
	// The (anti) direction the computer is facing
	public int computer_compass;
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("computer_compass", computer_compass);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.computer_compass = par1NBTTagCompound.getInteger("computer_compass");
	}
	
	@Override
	/**
	 * Holy Crap! Thanks to WiduX (http://widux.net/wiki/index.php?n=ModdingTutorials.BasicTileEntity)
	 */
	public Packet getDescriptionPacket()
	{
		NBTTagCompound customParam1 = new NBTTagCompound();
		this.writeToNBT(customParam1);
		return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, 0, customParam1);
	}
	
	@Override
    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     *
     * @param net The NetworkManager the packet originated from
     * @param pkt The data packet
     */
    public void onDataPacket(INetworkManager net, Packet132TileEntityData pkt)
    {
    	this.readFromNBT(pkt.data);
    }
}
