package cn.nulladev.exac.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityCooler extends EntityHasOwner {
	
	public EntityCooler(World world) {
		super(world);
	}
	
	public EntityCooler(World _world, EntityPlayer owner) {
		super(_world);
		this.setOwner(owner);
		this.setPosition(owner.posX, owner.posY, owner.posZ);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		EntityPlayer owner = this.getOwner();
		if (owner == null || this.ticksExisted > 20) {
			this.setDead();
			return;
		}
		this.setPosition(owner.posX, owner.posY, owner.posZ);
	}

}
