package cn.nulladev.exac.entity;

import cn.academy.ability.context.Context;
import cn.academy.ability.context.ContextManager;
import cn.nulladev.exac.ability.telekinesis.skill.PsychoHarden.ContextPsychoHarden;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import java.util.Optional;

public class EntityShell extends EntityHasOwner {

    public EntityShell(World _world) {
        super(_world);
    }

    public EntityShell(World _world, EntityPlayer owner) {
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
            return;
        }
        Optional<ContextPsychoHarden> context = ContextManager.instance.find(ContextPsychoHarden.class);
        if(!context.isPresent() || (context.get().getStatus() != Context.Status.ALIVE && context.get().player == this.getOwner())) {
            this.setDead();
            return;
        }
        this.setPosition(owner.posX, owner.posY, owner.posZ);
    }
}
