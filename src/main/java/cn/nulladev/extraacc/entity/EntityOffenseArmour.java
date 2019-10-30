package cn.nulladev.extraacc.entity;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import cn.academy.ability.SkillDamageSource;
import cn.academy.ability.context.ContextManager;
import cn.lambdalib2.util.MathUtils;
import cn.academy.ability.context.Context.Status;
import cn.nulladev.extraacc.ability.aerohand.skill.OffenseArmour;
import cn.nulladev.extraacc.ability.aerohand.skill.OffenseArmour.ContextOffenseArmour;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityOffenseArmour extends EntityHasOwner {
	
	public static final float BASIC_DAMAGE = 8;
	public static final float MAX_DAMAGE = 12;
	
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
		Optional<ContextOffenseArmour> context = ContextManager.instance.find(ContextOffenseArmour.class);
		if(!context.isPresent() || context.get().getStatus() != Status.ALIVE) {
			this.setDead();
			return;
		}
		this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);
		
		List list = this.world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(posX - 2, posY - 2, posZ - 2, posX + 2, posY + 2, posZ + 2));
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Entity target = (Entity) iterator.next();
			if (target instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)target;
				if (entity == this.getOwner())
					continue;
				float d = (float) Math.sqrt(Math.pow(entity.posX - posX, 2) + Math.pow(entity.posY - posY, 2) + Math.pow(entity.posZ - posZ, 2));
	            if (d <= 1.5F) {
	            	if (context.get().ctx.consume(0, 100 * target.height)) {
	            		float exp = context.get().ctx.getSkillExp();
	            		float dmg = MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, exp);
	            		entity.attackEntityFrom(new SkillDamageSource(this.getOwner(), OffenseArmour.INSTANCE), dmg);
		            	Vec3d direc = new Vec3d(entity.posX - this.posX, entity.posY - this.posY, entity.posZ - this.posZ).normalize();
		            	entity.addVelocity(2 * direc.x, 2 * direc.y, 2 * direc.z);
		            	context.get().ctx.addSkillExp(0.002f);
	            	} else {
	            		context.get().terminate();
	            	}
	            }
			} else if (target instanceof IProjectile) {
				if (context.get().ctx.consume(0, 10)) {
					target.setVelocity(0, 0, 0);
					context.get().ctx.addSkillExp(0.001f);
				} else {
            		context.get().terminate();
            	}
			}
		}
	}

}