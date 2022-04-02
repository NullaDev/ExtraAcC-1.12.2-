package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import cn.academy.entity.EntityMineRayBasic;
import cn.academy.event.BlockDestroyEvent;
import cn.academy.item.ItemEnergyBase;
import cn.lambdalib2.util.EntitySelectors;
import cn.lambdalib2.util.Raytrace;
import cn.nulladev.exac.core.EXACUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class ItemLasorGun extends ItemEnergyBase {

	public static final double ENERGY_COST = 2D;

	public ItemLasorGun() {
		super(100000, 200);
		this.setUnlocalizedName("lasorGun");
		this.setCreativeTab(AcademyCraft.cct);
		this.bFull3D = true;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if (world.isRemote) {
			if (!EXACUtils.isActive(stack)) {
				createRay(player);
			} else {
				killRay(player);
			}
			return new ActionResult(EnumActionResult.PASS, stack);
		}

		if (EXACUtils.isActive(stack)) {
			turnOff(stack);
		} else if (player.capabilities.isCreativeMode || itemManager.getEnergy(stack) > 10 * ENERGY_COST) {
			EXACUtils.setActive(stack, true);
		}

		return new ActionResult(EnumActionResult.SUCCESS, stack);
	}

	private static void turnOff(ItemStack stack) {
		EXACUtils.setActive(stack, false);
		setHarvestBlockPos(stack, new BlockPos(-1, -1, -1));
		setHarvestLeft(stack, Float.MAX_VALUE);
	}

	@SideOnly(Side.CLIENT)
	private static void createRay(EntityPlayer player) {
		EntityMineRayBasic entityRay = new EntityMineRayBasic(player);
		entityRay.length = 8D;
		player.world.spawnEntity(entityRay);
	}

	@SideOnly(Side.CLIENT)
	private static void killRay(EntityPlayer player) {
		AxisAlignedBB aabb = player.getEntityBoundingBox().grow(5);
		for(EntityMineRayBasic ray: player.world.getEntitiesWithinAABB(EntityMineRayBasic.class, aabb)) {
			ray.setDead();
		}
	}

	private static BlockPos getHarvestBlockPos(ItemStack stack) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		if (nbt.hasKey("harvestX") && nbt.hasKey("harvestY") && nbt.hasKey("harvestZ")) {
			return new BlockPos(nbt.getInteger("harvestX"), nbt.getInteger("harvestY"), nbt.getInteger("harvestZ"));
		} else {
			nbt.setInteger("harvestX", -1);
			nbt.setInteger("harvestY", -1);
			nbt.setInteger("harvestZ", -1);
			return new BlockPos(-1, -1, -1);
		}
	}
	
	private static void setHarvestBlockPos(ItemStack stack, BlockPos pos) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		nbt.setInteger("harvestX", pos.getX());
		nbt.setInteger("harvestY", pos.getY());
		nbt.setInteger("harvestZ", pos.getZ());
	}
	
	private static float getHarvestLeft(ItemStack stack) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		if (nbt.hasKey("harvestLeft")) {
			return nbt.getFloat("harvestLeft");
		} else {
			nbt.setFloat("harvestLeft", Float.MAX_VALUE);
			return Float.MAX_VALUE;
		}
	}
	
	private static void setHarvestLeft(ItemStack stack, float value) {
		NBTTagCompound nbt = EXACUtils.get_or_create_nbt(stack);
		nbt.setFloat("harvestLeft", value);
	}

	@Override
	public void onUpdate(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
		if (!(entity instanceof EntityPlayer)) {
			return;
		}
		EntityPlayer player = (EntityPlayer) entity;

		if (!EXACUtils.isActive(stack)) {
			return;
		}

		if (!isSelected) {
			turnOff(stack);
			killRay(player);
			return;
		}

		if (!player.capabilities.isCreativeMode && itemManager.pull(stack, 2, true) < 2) {
			turnOff(stack);
			killRay(player);
			return;
		}

		RayTraceResult result = Raytrace.traceLiving(player, 8, EntitySelectors.nothing());
		if (result != null) {
			BlockPos pos = result.getBlockPos();
			if (!pos.equals(getHarvestBlockPos(stack))) {
				IBlockState is = player.world.getBlockState(pos);
				Block block = is.getBlock();
				if (block == Blocks.AIR)
					return;
				if (!(MinecraftForge.EVENT_BUS.post(new BlockDestroyEvent(player, pos))) && block.getHarvestLevel(is) <= 3) {
					setHarvestBlockPos(stack, pos);
					float hardness = block.getBlockHardness(is, player.world, pos);
					if (hardness < 0)
						hardness = Float.MAX_VALUE;
					setHarvestLeft(stack, hardness);
				} else {
					setHarvestBlockPos(stack, new BlockPos(-1, -1, -1));
				}
			} else {
				float hardnessLeft = getHarvestLeft(stack) - 0.2F;
				if (hardnessLeft > 0) {
					setHarvestLeft(stack, hardnessLeft);
				} else {
					IBlockState is = player.world.getBlockState(pos);
					Block block = is.getBlock();
					onBlockBreak(player.world, pos, block, player);
					setHarvestBlockPos(stack, new BlockPos(-1, -1, -1));
					setHarvestLeft(stack, Float.MAX_VALUE);
				}
			}
		} else {
			setHarvestBlockPos(stack, new BlockPos(-1, -1, -1));
			setHarvestLeft(stack, Float.MAX_VALUE);
		}
	}
	
	private static void onBlockBreak(World world, BlockPos pos, Block block, EntityPlayer player) {
		IBlockState blockstate = world.getBlockState(pos);
		SoundEvent snd = blockstate.getBlock().getSoundType(blockstate, world, pos, player).getBreakSound();
		world.playSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, snd, SoundCategory.BLOCKS, .5f, 1f, false);
		block.dropBlockAsItemWithChance(world, pos, world.getBlockState(pos), 1.0f, 0);
		world.setBlockState(pos, Blocks.AIR.getDefaultState());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		if (EXACUtils.isActive(stack)) {
			tooltip.add(I18n.translateToLocal("item.imagitem.enabled"));
		} else {
			tooltip.add(I18n.translateToLocal("item.imagitem.disabled"));
		}
		super.addInformation(stack, world, tooltip, flag);
	}

	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

}
