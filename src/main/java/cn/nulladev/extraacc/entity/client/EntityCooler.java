package cn.nulladev.extraacc.entity.client;

import cn.lambdalib2.util.MathUtils;
import cn.nulladev.extraacc.entity.EntityHasOwner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityCooler extends EntityHasOwner {
	
	public float h = 2F;

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
		if (owner == null) {
			this.setDead();
			return;
		}
		this.setPosition(owner.posX, owner.posY, owner.posZ);
		if (this.h >= 0)
			this.h -= 0.1F;
		else
			this.setDead();
	}

}
