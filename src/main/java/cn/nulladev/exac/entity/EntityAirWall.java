package cn.nulladev.exac.entity;

import java.util.Iterator;
import java.util.List;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.aerohand.skill.AirWall;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityAirWall extends EntityHasOwner {
	
	public static final float BASIC_RADIUS = 6;
	public static final float MAX_RADIUS = 9;
	public static final float BASIC_DAMAGE = 10;
	public static final float MAX_DAMAGE = 15;
	
	private float exp;
		
	protected static final DataParameter<Float> CurrentRadius = EntityDataManager.<Float>createKey(EntityAirWall.class, DataSerializers.FLOAT);
	protected static final DataParameter<Float> MaxRadius = EntityDataManager.<Float>createKey(EntityAirWall.class, DataSerializers.FLOAT);

	public EntityAirWall(World world) {
		super(world);
	}
	
	public EntityAirWall(World _world, EntityPlayer owner, float _exp) {
		super(_world);
        this.exp = _exp;
		this.setMaxRadius(MathUtils.lerpf(BASIC_RADIUS, MAX_RADIUS, _exp));
		this.setOwner(owner);
		this.setPosition(owner.posX, owner.posY, owner.posZ);
	}
    
    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }
	
	private float getDamage() {
    	return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this.exp);
    }
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CurrentRadius, 0.0F);
		this.dataManager.register(MaxRadius, 0.0F);
	}
	
	@Override
    public void onUpdate() {
		super.onUpdate();
		
		float curRadius = this.getCurrentRadius();
		if (!this.world.isRemote && curRadius > this.getMaxRadius()) {
			this.setDead();
			return;
		}
		
		// 检测波面附近的实体
		if (!this.world.isRemote && this.ticksExisted % 2 == 0) {
			float r = getCurrentRadius();
			List list = this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(
					posX - r, posY - 0.5, posZ - r, posX + r, posY + 2.5, posZ + r));
			Iterator iterator = list.iterator();
			while (iterator.hasNext()) {
				Entity target = (Entity) iterator.next();
				if (target instanceof EntityLivingBase) {
					EntityLivingBase entity = (EntityLivingBase)target;
					if (entity == this.getOwner())
						continue;
					float d = (float) Math.sqrt(Math.pow(entity.posX - posX, 2) + Math.pow(entity.posZ - posZ, 2));
		            if (d >= curRadius - 0.4 && d <= curRadius + 0.4) {
		            	entity.attackEntityFrom(new SkillDamageSource(this.getOwner(), AirWall.INSTANCE), this.getDamage());
		            	Vec3d direc = new Vec3d(entity.posX - this.posX, 0, entity.posZ - this.posZ).normalize();
		            	entity.addVelocity(direc.x, 0, direc.z);
		            }
				} else if (target instanceof IProjectile) {
					target.setDead();
					//target.setVelocity(0, target.motionY, 0);
				}

	        }
		}
		this.setCurrentRadius(curRadius + 0.2f);
	}

	
	public void setCurrentRadius(float radius) {
		this.dataManager.set(CurrentRadius, radius);
	}
	
	public float getCurrentRadius() {
		return this.dataManager.get(CurrentRadius);
	}
	
	public void setMaxRadius(float maxRadius) {
		this.dataManager.set(MaxRadius, maxRadius);
	}
	
	public float getMaxRadius() {
		return this.dataManager.get(MaxRadius);
	}

}