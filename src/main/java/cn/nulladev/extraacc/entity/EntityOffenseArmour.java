package cn.nulladev.extraacc.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityOffenseArmour extends EntityHasOwner {
	
	public EntityOffenseArmour(World world) {
		super(world);
	}
	
	public EntityOffenseArmour(World _world, EntityPlayer owner) {
		super(_world);
		this.setOwner(owner);
		this.setPosition(owner.posX, owner.posY, owner.posZ);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if (this.world.isRemote) {
			return;
		}
		if (this.getOwner() == null) {
			this.setDead();
			return;
		}		
		if (!this.getOwner().getEntityData().hasKey("offense_armour") || !this.getOwner().getEntityData().getBoolean("offense_armour")) {
			this.setDead();
			return;
		}
		this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);
	}

}