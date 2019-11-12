package cn.nulladev.exac.entity;

import java.util.UUID;

import javax.annotation.Nullable;

import com.google.common.base.Optional;

import net.minecraft.entity.Entity;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.server.management.PreYggdrasilConverter;
import net.minecraft.world.World;

public class EntityHasOwner extends Entity implements IEntityOwnable {
	
    protected static final DataParameter<Optional<UUID>> OWNER_UNIQUE_ID = EntityDataManager.<Optional<UUID>>createKey(EntityHasOwner.class, DataSerializers.OPTIONAL_UNIQUE_ID);
	
	public EntityHasOwner(World world) {
        super(world);
        this.ticksExisted = 0;
        this.isImmuneToFire = true;
    }

	@Override
	protected void entityInit() {
		this.dataManager.register(OWNER_UNIQUE_ID, Optional.absent());
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound nbt) {
		String s = "";

        if (nbt.hasKey("OwnerUUID", 8)) {
            s = nbt.getString("OwnerUUID");
        }

        if (s != "") {
        	this.setOwnerId(UUID.fromString(s));
        }
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound nbt) {
		if (this.getOwnerId() == null) {
			nbt.setString("OwnerUUID", "");
        } else {
        	nbt.setString("OwnerUUID", this.getOwnerId().toString());
        }
	}
	
	public void setOwner(EntityPlayer player) {
		setOwnerId(player.getUniqueID());
    }
	
	public void setOwnerId(@Nullable UUID uuid) {
        this.dataManager.set(OWNER_UNIQUE_ID, Optional.fromNullable(uuid));
    }

	@Override
	public EntityPlayer getOwner() {
		try {
            UUID uuid = this.getOwnerId();
            return uuid == null ? null : this.world.getPlayerEntityByUUID(uuid);
        } catch (IllegalArgumentException e) {
            return null;
        }
	}

	@Override
	@Nullable
    public UUID getOwnerId() {
        return this.dataManager.get(OWNER_UNIQUE_ID).orNull();
    }

}