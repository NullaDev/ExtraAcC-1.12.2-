package cn.nulladev.exac.entity;

import java.util.Iterator;
import java.util.List;

import cn.lambdalib2.util.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityVacuum extends EntityHasOwner {
	
	public static final float BASIC_DAMAGE = 40;
	public static final float MAX_DAMAGE = 60;
	
	private float exp;
	private boolean damageCaused = false;
	
	public EntityVacuum(World world) {
		super(world);
	}
	
	public EntityVacuum(World _world, EntityPlayer owner, float _exp) {
		super(_world);
		this.setOwner(owner);
		this.exp = _exp;
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
		if (this.getOwner() == null || this.ticksExisted > 25) {
			this.setDead();
			return;
		}
		this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);
		
		if (this.damageCaused || this.ticksExisted < 5) {
			return;
		}
		
		List list = this.world.getEntitiesWithinAABB(Entity.class,
				new AxisAlignedBB(posX - 3, posY - 3, posZ - 3, posX + 3, posY + 3, posZ + 3));
		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Entity target = (Entity) iterator.next();
			if (target instanceof EntityLivingBase) {
				EntityLivingBase entity = (EntityLivingBase)target;
				if (entity.getEntityBoundingBox().grow(3F).contains(new Vec3d(this.posX, this.posY + this.getOwner().height / 2, this.posZ))) {
					float dmg = MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, exp);
            		entity.attackEntityFrom(new EntityDamageSource("drown", this.getOwner()).setDamageBypassesArmor(), dmg);
	            }
			} else if (target instanceof IProjectile) {
				target.setDead();
			}
		}
		this.damageCaused = true;
	}

}