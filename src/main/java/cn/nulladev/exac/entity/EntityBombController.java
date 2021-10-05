package cn.nulladev.exac.entity;

import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.nulladev.exac.ability.telekinesis.skill.CruiseBomb;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class EntityBombController extends EntityHasOwner {

    private final ArrayList<EntityBomb> listBomb = new ArrayList<EntityBomb>();
    private int summon_cd = 0;

    public EntityBombController(World world) {
        super(world);
        this.width = 0;
        this.height = 0;
    }

    public EntityBombController(World _world, EntityPlayer owner) {
        super(_world);
        this.width = 0;
        this.height = 0;
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
            this.kill();
            return;
        }
        Optional<CruiseBomb.ContextCruiseBomb> context = ContextManager.instance.find(CruiseBomb.ContextCruiseBomb.class);
        if(!context.isPresent() || (context.get().getStatus() != Context.Status.ALIVE && context.get().player == this.getOwner())) {
            this.kill();
            return;
        }
        this.setPosition(this.getOwner().posX, this.getOwner().posY, this.getOwner().posZ);

        Iterator<EntityBomb> iterator = listBomb.iterator();
        while(iterator.hasNext()) {
            EntityBomb bomb = iterator.next();
            if (bomb == null || bomb.isDead) {
                iterator.remove();
            }
        }
        float exp = context.get().ctx.getSkillExp();
        float max_size = EntityBombController.getBombAmount(exp);
        if (summon_cd > 0) {
            summon_cd--;
        } else {
            if (listBomb.size() < max_size) {
                EntityBomb bomb = new EntityBomb(this.world, this.getOwner(), exp);
                if (context.get().ctx.consume(0, 50)) {
                    context.get().ctx.addSkillExp(0.0001f);
                    this.world.spawnEntity(bomb);
                    listBomb.add(bomb);
                    summon_cd = 10;
                } else {
                    context.get().terminate();
                    return;
                }
            }
        }

        List<Entity> list = this.world.getEntitiesWithinAABB(Entity.class,
                new AxisAlignedBB(posX - 4, posY - 4, posZ - 4, posX + 4, posY + 4, posZ + 4));
        for (Entity target : list) {
            if (target instanceof EntityLivingBase) {
                EntityLivingBase entity = (EntityLivingBase)target;
                if (entity == this.getOwner())
                    continue;
                if (entity instanceof IEntityOwnable && ((IEntityOwnable)entity).getOwner()==this.getOwner())
                    continue;
                if (entity.isDead)
                    continue;
                if (!targeted(entity) && this.getBomb() != null) {
                    if (context.get().ctx.consume(0, 50)) {
                        context.get().ctx.addSkillExp(0.0002f);
                        this.getBomb().lock(entity);
                    } else {
                        context.get().terminate();
                        return;
                    }
                }
            } else if (target instanceof IProjectile) {
                if (target instanceof IEntityOwnable) {
                    IEntityOwnable target1 = (IEntityOwnable)target;
                    if (target1.getOwner() == this.getOwner()) {
                        continue;
                    }
                }
                if (!targeted(target) && this.getBomb() != null) {
                    if (context.get().ctx.consume(0, 50)) {
                        context.get().ctx.addSkillExp(0.0002f);
                        this.getBomb().lock(target);
                    } else {
                        context.get().terminate();
                        return;
                    }
                }
            }
        }
    }

    private static int getBombAmount(float exp) {
        if (exp >= 1F)
            return 8;
        else if (exp >= 0.75F)
            return 7;
        else if (exp >= 0.5F)
            return 6;
        else if (exp >= 0.25F)
            return 5;
        else
            return 4;
    }

    private boolean targeted(Entity entity) {
        for (EntityBomb bomb : this.listBomb) {
            if (bomb.get_target() == entity)
                return true;
        }
        return false;
    }

    private EntityBomb getBomb() {
        for (EntityBomb bomb : this.listBomb) {
            if (bomb.get_target() == null)
                return bomb;
        }
        return null;
    }

    private void kill() {
        for (EntityBomb bomb : this.listBomb) {
            bomb.setDead();
        }
        this.setDead();
    }

}
