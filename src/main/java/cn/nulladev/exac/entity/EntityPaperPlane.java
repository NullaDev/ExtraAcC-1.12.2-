package cn.nulladev.exac.entity;

import cn.nulladev.exac.core.EXACItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityPaperPlane extends EntityFlying {

    private boolean _reinforced = false;

    public static final float SIZE = 0.5F;
    public static final float VELOCITY = 0.3F;
    public static final float VELOCITY_REINFORCED = 0.6F;

    private Vec3d _direc;

    public EntityPaperPlane(World world) {
        super(world, SIZE, SIZE);
        this.setGravity(0.01F);
        this.setDecrease(0.98F);
    }

    public EntityPaperPlane(World world, EntityPlayer thrower, Vec3d dir) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, SIZE, SIZE, Integer.MAX_VALUE);
        this.setGravity(0.01F);
        this.setDecrease(0.98F);
        this._direc = dir;
        this.setVelocity(dir, VELOCITY);
        this.harvestStrength = 0.2F;
    }

    public void setReinforced() {
        this._reinforced = true;
        this.setDecrease(0.99F);
        this.setVelocity(this._direc, VELOCITY_REINFORCED);
    }

    public boolean isReinforced() {
        return this._reinforced;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        if (this._reinforced) {
            this.world.createExplosion(this.getOwner(), pos.hitVec.x, pos.hitVec.y, pos.hitVec.z, 2F, false);
        } else {
            EntityItem item = new EntityItem(this.world, this.posX, this.posY, this.posZ, new ItemStack(EXACItems.paper_plane));
            this.world.spawnEntity(item);
        }
        this.setDead();
    }

    @Override
    public void onUpdate() {
        //奇技淫巧
        if (this.world.isRemote && this.ticksExisted == 1) {
            double v2 = Math.pow(this.motionX, 2) + Math.pow(this.motionY, 2) + Math.pow(this.motionZ, 2);
            if (v2 >= 0.25)
                this._reinforced = true;
        }
        super.onUpdate();
        if (this.motionY <= 0) {
            Vec3d vxz = new Vec3d(this.motionX, 0, this.motionZ).normalize();
            this.addVelocity(vxz.x * 0.01, 0, vxz.z * 0.01);
        }
    }

}
