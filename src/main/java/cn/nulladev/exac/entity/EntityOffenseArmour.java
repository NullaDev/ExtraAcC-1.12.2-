package cn.nulladev.exac.entity;

import java.util.List;
import java.util.Optional;

import cn.academy.ability.SkillDamageSource;
import cn.academy.ability.context.ContextManager;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour;
import cn.nulladev.exac.ability.aerohand.skill.OffenseArmour.ContextOffenseArmour;
import cn.academy.ability.context.Context.Status;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
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
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
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
		if(!context.isPresent() || (context.get().getStatus() != Status.ALIVE && context.get().player == this.getOwner())) {
			this.setDead();
			return;
		}
		this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);
		
		List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(posX - 2, posY - 2, posZ - 2, posX + 2, posY + 2, posZ + 2));
		for (Entity target : list) {
			if (target instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)target;
				if (entity == this.getOwner())
					continue;
				if (entity.getEntityBoundingBox().grow(1.5F).contains(new Vec3d(this.posX, this.posY + this.getOwner().height / 2, this.posZ))) {
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
				if (target instanceof IEntityOwnable) {
					IEntityOwnable target1 = (IEntityOwnable)target;
					if (target1.getOwner() == this.getOwner()) {
						continue;
					}
				}
				/*if (isProjectileInGround(target)) {
					continue;
				}*/
				if (context.get().ctx.consume(0, 10)) {
					target.setDead();
					context.get().ctx.addSkillExp(0.001f);
				} else {
            		context.get().terminate();
            	}
			}
		}
	}
	
	/*private static boolean isProjectileInGround(Entity target) {	
		try {
			Class c = target.getClass();
			while (!c.equals(Object.class)) {
				Field[] fields = c.getDeclaredFields();
				for(Field field : fields){
			        if(Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())){
			            continue;
			        }
			        if (field.getName().equals("inGround") && field.getType() == boolean.class) {
			        	field.setAccessible(true);
						if (field.getBoolean(target)) {
							return true;
						} else {
							return false;
						}
			        }
			    }
				c = c.getSuperclass();
			}
		} catch(Exception e) {}
		return false;
	}*/

}