package cn.nulladev.extraacc.entity;

import java.util.Iterator;
import java.util.List;

import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityAirWall extends EntityHasOwner {
	
	public static final float BASIC_RADIUS = 6;
	public static final float MAX_RADIUS = 9;
	public static final float BASIC_DAMAGE = 8;
	public static final float MAX_DAMAGE = 12;
	
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
	
	private float getDamage(float exp) {
    	return MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, exp);
    }
	
	@Override
	protected void entityInit() {
		super.entityInit();
		this.dataManager.register(CurrentRadius, Float.valueOf(0.0F));
		this.dataManager.register(MaxRadius, Float.valueOf(0.0F));
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
		            	entity.attackEntityFrom(DamageSource.causePlayerDamage(this.getOwner()), getDamage(exp));
		            	Vec3d direc = new Vec3d(entity.posX - this.posX, 0, entity.posZ - this.posZ).normalize();
		            	entity.addVelocity(direc.x, 0, direc.z);
		            }
				} else if (target instanceof IProjectile) {
					target.setDead();
				}
				
	        }
		}
		this.setCurrentRadius(curRadius + 0.2f);
	}

	
	public void setCurrentRadius(float radius) {
		this.dataManager.set(CurrentRadius, Float.valueOf((float)radius));
	}
	
	public float getCurrentRadius() {
		return this.dataManager.get(CurrentRadius);
	}
	
	public void setMaxRadius(float maxRadius) {
		this.dataManager.set(MaxRadius, Float.valueOf((float)maxRadius));
	}
	
	public float getMaxRadius() {
		return this.dataManager.get(MaxRadius);
	}

}