package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.psychokinesist.skill.PsychoThrowing;
import cn.nulladev.exac.core.EXACItems;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityCobblestone extends EntityFlying {

    public static final int ACC_TIME = 20;
    public static final float BASIC_VELOCITY = 1F;
    public static final float MAX_VELOCITY = 2F;
    public static final float BASIC_DAMAGE = 12;
    public static final float MAX_DAMAGE = 18;

    private float exp;
    private Vec3d direc;
    private boolean isEtched;

    public EntityCobblestone(World world) {
        super(world, 1, 1);
        this.setDecrease(0.99F);
    }

    public EntityCobblestone(World world, EntityPlayer thrower, float _exp, Vec3d _dir, boolean etched) {
        super(world, thrower, thrower.posX, thrower.posY + thrower.eyeHeight, thrower.posZ, 1, 1, Integer.MAX_VALUE);
        this.setDecrease(0.99F);
        this.isEtched = etched;
        this.exp = _exp;
        this.direc = _dir;
        this.setVelocity(this.direc, this.getAcc());
        this.harvestStrength = 0F;
    }

    @Override
    public boolean shouldRenderInPass(int pass) {
        return pass == 1;
    }

    private float getAcc() {
        float bonus = isEtched? 1.5F:1F;
        return bonus * MathUtils.lerpf(BASIC_VELOCITY, MAX_VELOCITY, this.exp);
    }

    private float getDamage() {
        float bonus = isEtched? 1.1F:1F;
        return bonus * MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, this.exp);
    }

    public void addVelocity(Vec3d vDir, float v) {
        vDir.normalize();
        this.addVelocity(v * vDir.x, v * vDir.y, v * vDir.z);
    }

    @Override
    protected void onImpact(RayTraceResult pos) {
        BlockPos genPos;
        if (pos.entityHit != null) {
            pos.entityHit.attackEntityFrom(new SkillDamageSource(this.getOwner(), PsychoThrowing.INSTANCE).setProjectile(), getDamage());
            genPos = new BlockPos(pos.hitVec);
        } else {
            genPos = pos.getBlockPos();
        }
        ItemStack stack = this.isEtched? new ItemStack(EXACItems.etched_cobblestone):new ItemStack(Blocks.COBBLESTONE);
        EntityItem drop = new EntityItem(this.world, genPos.getX(), genPos.getY(), genPos.getZ(), stack);
        this.world.spawnEntity(drop);
        this.setDead();
    }

    @Override
    public void onUpdate() {
        if (this.age <= ACC_TIME)
            this.addVelocity(this.direc, this.getAcc());
        super.onUpdate();
    }
}
