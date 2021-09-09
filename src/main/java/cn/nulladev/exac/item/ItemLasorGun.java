package cn.nulladev.exac.item;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import cn.academy.AcademyCraft;
import cn.academy.entity.EntityMineRayBasic;
import cn.academy.event.BlockDestroyEvent;
import cn.academy.item.ItemEnergyBase;
import cn.lambdalib2.util.EntitySelectors;
import cn.lambdalib2.util.Raytrace;
import cn.nulladev.exac.core.EXACUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemLasorGun extends ItemEnergyBase {
		
	public ItemLasorGun() {
		super(100000, 200);
		this.setUnlocalizedName("lasorGun");
		this.setCreativeTab(AcademyCraft.cct);
		this.bFull3D = true;
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
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
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void harvest(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		ItemStack stack = player.getHeldItemMainhand();
		if (stack == null || stack.getItem() != this) {
			return;
		}
		if (!Minecraft.getMinecraft().gameSettings.keyBindUseItem.isKeyDown()) {
			setHarvestBlockPos(stack, new BlockPos(-1, -1, -1));
			setHarvestLeft(stack, Float.MAX_VALUE);
			RayFinder.killRay(player);
			return;
		}
		if (!player.capabilities.isCreativeMode && itemManager.pull(stack, 20, true) < 20) {
			return;
		}
		RayFinder.pushRay(player);
		RayTraceResult result = Raytrace.traceLiving(player, 8, EntitySelectors.nothing());
		if (result != null) {
			BlockPos pos = result.getBlockPos();
			if (!pos.equals(getHarvestBlockPos(stack))) {
				IBlockState is = player.world.getBlockState(pos);
				Block block = is.getBlock();
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
		tooltip.add(I18n.translateToLocal("item.lasorGun.desc"));
		super.addInformation(stack, world, tooltip, flag);
    }
	
	@Override
	public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

}

class RayFinder {
	public static final HashMap<EntityPlayer, Entity> map = new HashMap<EntityPlayer, Entity>();
	
	public static boolean hasRay(EntityPlayer entity) {
		return map.containsKey(entity);
	}
	
	public static Entity getRay(EntityPlayer entity) {
		if (hasRay(entity)) {
			return map.get(entity);
		} else {
			return null;
		}
	}
	
	public static void removeRay(EntityPlayer entity) {
		if (hasRay(entity))
			map.remove(entity);
	}
	
	@SideOnly(Side.CLIENT)
	public static void pushRay(EntityPlayer player) {
		if (RayFinder.getRay(player) == null || RayFinder.getRay(player).isDead) {
			removeRay(player);
			Entity entityRay = new EntityMineRayBasic(player);
			player.world.spawnEntity(entityRay);
			map.put(player, entityRay);
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void killRay(EntityPlayer player) {
		if (RayFinder.getRay(player) != null) {
			RayFinder.getRay(player).setDead();
			RayFinder.removeRay(player);
		}
	}
}
