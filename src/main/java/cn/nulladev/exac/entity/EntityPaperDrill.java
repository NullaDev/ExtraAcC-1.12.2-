package cn.nulladev.exac.entity;

import cn.academy.ability.SkillDamageSource;
import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.lambdalib2.util.MathUtils;
import cn.nulladev.exac.ability.telekinesis.skill.PaperDrill;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.Optional;

public class EntityPaperDrill extends EntityHasOwner {

    public static final float BASIC_DAMAGE = 10;
    public static final float MAX_DAMAGE = 15;

    public EntityPaperDrill(World _world) {
        super(_world);
    }

    public EntityPaperDrill(World _world, EntityPlayer owner) {
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
        EntityPlayer owner = this.getOwner();
        if (this.getOwner() == null) {
            this.setDead();
            if (!this.world.isRemote) {
                EntityItem drop = new EntityItem(this.world, posX, posY, posZ, new ItemStack(Items.PAPER, 64));
                this.world.spawnEntity(drop);
            }
            return;
        }
        Optional<PaperDrill.ContextPaperDrill> context = ContextManager.instance.find(PaperDrill.ContextPaperDrill.class);
        if(!context.isPresent() || (context.get().getStatus() != Context.Status.ALIVE && context.get().player == this.getOwner())) {
            this.setDead();
            if (!this.world.isRemote) {
                EntityItem drop = new EntityItem(this.world, posX, posY, posZ, new ItemStack(Items.PAPER, 64));
                this.world.spawnEntity(drop);
            }
            return;
        }
        this.setPosition(owner.posX, owner.posY + owner.eyeHeight, owner.posZ);
        this.calcRotation();

        double x = owner.getLookVec().x * 5;
        double y = owner.getLookVec().y * 5;
        double z = owner.getLookVec().z * 5;
        List<EntityLivingBase> list = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                new AxisAlignedBB(posX, posY, posZ, posX + x, posY + y, posZ + z));
        for (EntityLivingBase entity : list) {
            if (entity == this.getOwner())
                continue;
            if (context.get().ctx.consume(0, 100 * entity.height)) {
                float exp = context.get().ctx.getSkillExp();
                float dmg = MathUtils.lerpf(BASIC_DAMAGE, MAX_DAMAGE, exp);
                entity.attackEntityFrom(new SkillDamageSource(this.getOwner(), PaperDrill.INSTANCE), dmg);
                context.get().ctx.addSkillExp(0.002f);
            } else {
                context.get().terminate();
            }
        }
    }

    protected void calcRotation() {
        Vec3d vec = getOwner().getLookVec();
        float xz = MathHelper.sqrt(vec.x * vec.x + vec.z * vec.z);
        this.rotationYaw = (float)(Math.atan2(vec.x, vec.z) * 180.0D / Math.PI);
        this.rotationPitch = (float)(Math.atan2(vec.y, xz) * 180.0D / Math.PI);

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
    }
}
