package com.github.ttran17.blockmods.tv;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet132TileEntityData;
import net.minecraft.tileentity.TileEntity;

public class TVTileEntity extends TileEntity {
	
	//The coordinates of tv block will be stored here
	public int tv_x;
	public int tv_y;
	public int tv_z;
	
	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setInteger("tv_x", tv_x);
		par1NBTTagCompound.setInteger("tv_y", tv_y);
		par1NBTTagCompound.setInteger("tv_z", tv_z);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound)
	{
		super.readFromNBT(par1NBTTagCompound);
		this.tv_x = par1NBTTagCompound.getInteger("tv_x");
		this.tv_y = par1NBTTagCompound.getInteger("tv_y");
		this.tv_z = par1NBTTagCompound.getInteger("tv_z");
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
    	this.readFromNBT(pkt.customParam1);
    }

}
