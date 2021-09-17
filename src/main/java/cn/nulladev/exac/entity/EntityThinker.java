package cn.nulladev.exac.entity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class EntityThinker extends EntityHasOwner {

    public EntityThinker(World world) {
        super(world);
    }

    public EntityThinker(World _world, EntityPlayer owner) {
        super(_world);
        this.setOwner(owner);
        this.setPosition(owner.posX, owner.posY, owner.posZ);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        EntityPlayer owner = this.getOwner();
        if (owner == null || this.ticksExisted > 25) {
            this.setDead();
            return;
        }
        this.setPosition(owner.posX, owner.posY, owner.posZ);
    }

}
