package cn.nulladev.extraacc.entity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class EntityFlying extends EntityHasOwner implements IProjectile {
	
    protected float gravity = 0.03F;
    protected float velocityDecreaseRate = 0.99F;
    public final int age;

    public EntityFlying(World world, float width, float height) {
        super(world);
        this.setSize(width, height);
        this.age = Integer.MAX_VALUE;
    }  

    public EntityFlying(World world, EntityPlayer thrower, double _posX, double _posY, double _posZ, float _width, float _height, int _age) {
        super(world);
        this.setOwner(thrower);
        this.setSize(_width, _height);
        this.setPosition(_posX, _posY, _posZ);
        this.age = _age;
    }
    
    @Override
	public void shoot(double vdx, double vdy, double vdz, float v_value, float inaccuracy) {
		Vec3d vdr = new Vec3d(vdx, vdy, vdz).normalize();
		this.setVelocity(v_value * vdr.x, v_value * vdr.y, v_value * vdr.z);
	}

    @Override
    public void setVelocity(double vx, double vy, double vz) {
        this.motionX = vx;
        this.motionY = vy;
        this.motionZ = vz;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F) {
            float f = MathHelper.sqrt(vx * vx + vz * vz);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(vx, vz) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(vy, (double)f) * 180.0D / Math.PI);
        }
    }
    
    public void setVelocity(Vec3d vDir, float v) {
    	vDir.normalize();
    	this.setVelocity(v * vDir.x, v * vDir.y, v * vDir.z);
    }

    @Override
    public void onUpdate() {    	
        this.lastTickPosX = this.posX;
        this.lastTickPosY = this.posY;
        this.lastTickPosZ = this.posZ;
        super.onUpdate();

        this.ticksExisted++;
		if (this.getOwner() == null || this.ticksExisted >= this.age) {
			this.setDead();
			return;
		}

        Vec3d currentPos = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d nextPos = new Vec3d(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
        RayTraceResult raytraceresult = this.world.rayTraceBlocks(currentPos, nextPos);

        if (raytraceresult != null) {
            nextPos = new Vec3d(raytraceresult.hitVec.x, raytraceresult.hitVec.y, raytraceresult.hitVec.z);
        }

        if (!this.world.isRemote) {
            Entity flag = null;
            List list = this.world.getEntitiesWithinAABBExcludingEntity(this, this.getEntityBoundingBox().expand(this.motionX, this.motionY, this.motionZ));
            EntityPlayer player = this.getOwner();

            for (int j = 0; j < list.size(); ++j) {
                Entity entity = (Entity)list.get(j);

                if (entity.canBeCollidedWith() && (entity != player)) {
                	RayTraceResult raytraceresult1 = entity.getEntityBoundingBox().calculateIntercept(currentPos, nextPos);
                    flag = entity;
                }
            }

            if (flag != null) {
            	raytraceresult = new RayTraceResult(flag);
            }
        }

        if (raytraceresult != null) {
            if (raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.world.getBlockState(raytraceresult.getBlockPos()).getBlock() == Blocks.PORTAL) {
            	this.setPortal(raytraceresult.getBlockPos());
            } else if (!ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                this.onImpact(raytraceresult);
            }
        }

        this.posX = nextPos.x;
        this.posY = nextPos.y;
        this.posZ = nextPos.z;
        double xz = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
        this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

        for (this.rotationPitch = (float)(Math.atan2(this.motionY, xz) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
            ;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
            this.prevRotationYaw += 360.0F;
        }

        this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
        this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

        float f2 = velocityDecreaseRate;
        if (this.isInWater()) {
            for (int i = 0; i < 4; ++i) {
                this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX - this.motionX * 0.25D, this.posY - this.motionY * 0.25D, this.posZ - this.motionZ * 0.25D, this.motionX, this.motionY, this.motionZ);
            }
            f2 = 0.8F;
        }

        this.motionX *= (double)f2;
        this.motionY *= (double)f2;
        this.motionZ *= (double)f2;
        
        this.motionY -= (double)this.gravity;
        this.setPosition(this.posX, this.posY, this.posZ);
    }

    @Deprecated
    protected float getGravity() {
        return gravity;
    }
    
    @Deprecated
    protected void setGravity(float g) {
        this.gravity = g;
    }
    
    public void setNoGravity() {
        this.gravity = 0;
    }
    
    @Deprecated
    public void setDecrease(float f) {
        this.velocityDecreaseRate = f;
    }
    
    public void setNoDecrease() {
        this.velocityDecreaseRate = 1;
    }

    protected abstract void onImpact(RayTraceResult p_70184_1_);

    @SideOnly(Side.CLIENT)
    public float getShadowSize() {
        return 0.0F;
    }
}
