package cn.nulladev.exac.core;

import cn.academy.ACItems;
import cn.academy.ability.CategoryManager;
import cn.academy.crafting.ImagFusorRecipes;
import cn.academy.energy.api.IFItemManager;
import cn.lambdalib2.crafting.RecipeRegistry;
import cn.nulladev.exac.ability.aerohand.CatAeroHand;
import cn.nulladev.exac.ability.aerohand.skill.Airflow;
import cn.nulladev.exac.ability.aerohand.skill.AscendingAir;
import cn.nulladev.exac.ability.psychokinesist.CatTelekinesis;
import cn.nulladev.exac.ability.psychokinesist.skill.SideArm;
import cn.nulladev.exac.entity.*;
import cn.nulladev.exac.item.ItemTeleporter;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EXACRegistry {
	
	public static final EXACRegistry INSTANCE = new EXACRegistry();
	
	public static EnumRarity RarityGolden = EnumHelper.addRarity("GOLDEN", TextFormatting.GOLD, "GOLDEN");
	
	public void registerAbility() {
	    CatAeroHand aerohand = new CatAeroHand();
		CatTelekinesis telekinesis = new CatTelekinesis();
	    CategoryManager.INSTANCE.register(aerohand);
		CategoryManager.INSTANCE.register(telekinesis);
	}
	
	public void registerEntities(Object ModObject) {
		int modID = 1;
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:air_blade"), EntityAirBlade.class, "air_blade", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:air_wall"), EntityAirWall.class, "air_wall", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:bomber_lance"), EntityBomberLance.class, "bomber_lance", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:cooler"), EntityCooler.class, "cooler", modID++, ModObject, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation("exac:helicopter"), EntityHelicopter.class, "helicopter", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:offense_armour"), EntityOffenseArmour.class, "offense_armour", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:vacuum"), EntityVacuum.class, "vacuum", modID++, ModObject, 128, 1, true);
		EntityRegistry.registerModEntity(new ResourceLocation("exac:volcanic_ball"), EntityVolcanicBall.class, "volcanic_ball", modID++, ModObject, 128, 1, true);
	}
	
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(EXACRegistry.INSTANCE);
		MinecraftForge.EVENT_BUS.register(AscendingAir.INSTANCE);
		MinecraftForge.EVENT_BUS.register(Airflow.INSTANCE);
		MinecraftForge.EVENT_BUS.register(SideArm.INSTANCE);
		MinecraftForge.EVENT_BUS.register(EXACItems.lasor_gun);
	}
	
	public void registerRecipes() {
		RecipeRegistry recipes = new RecipeRegistry();
		recipes.addRecipeFromResourceLocation(new ResourceLocation("exac:recipes/default.recipe"));
		 
		ImagFusorRecipes ifr = ImagFusorRecipes.INSTANCE;
        ifr.addRecipe(new ItemStack(Items.GLASS_BOTTLE), 1000, new ItemStack(EXACItems.cp_potion));
        /*
        MetalFormerRecipes mfr = MetalFormerRecipes.INSTANCE;
        mfr.add(new ItemStack(ACItems.imag_silicon_ingot), new ItemStack(ACItems.wafer, 2), Mode.INCISE);
		 */
	}
	
	@SubscribeEvent
	/*Register<T>具有10种泛型类型参数：<Item>,<Block>，<Potion>（药水），<Biome>（生物群系），<SoundEvent>
    （声音事件），<PotionType>（药水类型），<Enchantment>（附魔），<VillagerProfession>（村民职业），<EntityEntry>
     （实体），<IRecipe>（合成表），你可以在ForgeRegistries类中看到这些泛型参数，也可以不使用泛型参数，直接用
      RegisterEvent监听所有事件*/
	public void addItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(EXACItems.optical_chip.setRegistryName(ExtraAcademy.MODID, "optical_chip"));
		event.getRegistry().register(EXACItems.lasor_component.setRegistryName(ExtraAcademy.MODID, "lasor_component"));
		event.getRegistry().register(EXACItems.ray_twister.setRegistryName(ExtraAcademy.MODID, "ray_twister"));
		event.getRegistry().register(EXACItems.energy_unit_group.setRegistryName(ExtraAcademy.MODID, "energy_unit_group"));
		event.getRegistry().register(EXACItems.electricalibur.setRegistryName(ExtraAcademy.MODID, "electricalibur"));
		event.getRegistry().register(EXACItems.avalon.setRegistryName(ExtraAcademy.MODID, "avalon"));
		event.getRegistry().register(EXACItems.cp_potion.setRegistryName(ExtraAcademy.MODID, "cp_potion"));
		event.getRegistry().register(EXACItems.lasor_gun.setRegistryName(ExtraAcademy.MODID, "lasor_gun"));
		event.getRegistry().register(EXACItems.air_jet.setRegistryName(ExtraAcademy.MODID, "air_jet"));
		event.getRegistry().register(EXACItems.teleporter.setRegistryName(ExtraAcademy.MODID, "teleporter"));

		event.getRegistry().register(EXACItems.reso_helmet.setRegistryName(ExtraAcademy.MODID, "reso_helmet"));
		event.getRegistry().register(EXACItems.reso_chestplate.setRegistryName(ExtraAcademy.MODID, "reso_chestplate"));
		event.getRegistry().register(EXACItems.reso_leggings.setRegistryName(ExtraAcademy.MODID, "reso_leggings"));
		event.getRegistry().register(EXACItems.reso_boots.setRegistryName(ExtraAcademy.MODID, "reso_boots"));
		
		event.getRegistry().register(EXACItems.imag_helmet.setRegistryName(ExtraAcademy.MODID, "imag_helmet"));
		event.getRegistry().register(EXACItems.imag_chestplate.setRegistryName(ExtraAcademy.MODID, "imag_chestplate"));
		event.getRegistry().register(EXACItems.imag_leggings.setRegistryName(ExtraAcademy.MODID, "imag_leggings"));
		event.getRegistry().register(EXACItems.imag_boots.setRegistryName(ExtraAcademy.MODID, "imag_boots"));
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            registerItemRenderers();
        }
	}
	
	@SideOnly(Side.CLIENT)
	public void registerItemRenderers() {
		ModelLoader.setCustomModelResourceLocation(EXACItems.optical_chip, 0, new ModelResourceLocation("exac:optical_chip", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.lasor_component, 0, new ModelResourceLocation("exac:lasor_component", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.electricalibur, 0, new ModelResourceLocation("exac:electricalibur", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.avalon, 0, new ModelResourceLocation("exac:avalon", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.cp_potion, 0, new ModelResourceLocation("exac:cp_potion", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.lasor_gun, 0, new ModelResourceLocation("exac:lasor_gun", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.air_jet, 0, new ModelResourceLocation("exac:air_jet", "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(EXACItems.reso_helmet, 0, new ModelResourceLocation("exac:reso_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.reso_chestplate, 0, new ModelResourceLocation("exac:reso_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.reso_leggings, 0, new ModelResourceLocation("exac:reso_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.reso_boots, 0, new ModelResourceLocation("exac:reso_boots", "inventory"));
		
		ModelLoader.setCustomModelResourceLocation(EXACItems.imag_helmet, 0, new ModelResourceLocation("exac:imag_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.imag_chestplate, 0, new ModelResourceLocation("exac:imag_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.imag_leggings, 0, new ModelResourceLocation("exac:imag_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.imag_boots, 0, new ModelResourceLocation("exac:imag_boots", "inventory"));
		
		ModelLoader.setCustomMeshDefinition(EXACItems.energy_unit_group, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				int value = (int)(IFItemManager.instance.getEnergy(stack) + 5000) / 10000;
				String name = "energy_unit_group_" + value;
				return new ModelResourceLocation("exac:" + name, "inventory");
			}
	    });
		ModelBakery.registerItemVariants(EXACItems.ray_twister, 
				new ModelResourceLocation("exac:energy_unit_group_0", "inventory"),
				new ModelResourceLocation("exac:energy_unit_group_1", "inventory"),
				new ModelResourceLocation("exac:energy_unit_group_2", "inventory"),
				new ModelResourceLocation("exac:energy_unit_group_3", "inventory"),
				new ModelResourceLocation("exac:energy_unit_group_4", "inventory"),
				new ModelResourceLocation("exac:energy_unit_group_5", "inventory")
		);
		
		ModelLoader.setCustomMeshDefinition(EXACItems.teleporter, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				String name = ItemTeleporter.hasTeleportPos(stack)? "teleporter_pos" : "teleporter";
				return new ModelResourceLocation("exac:" + name, "inventory");
			}
	    });
		ModelBakery.registerItemVariants(EXACItems.ray_twister, 
				new ModelResourceLocation("exac:teleporter", "inventory"),
				new ModelResourceLocation("exac:teleporter_pos", "inventory")
		);
		
		ModelLoader.setCustomMeshDefinition(EXACItems.ray_twister, new ItemMeshDefinition() {
			@Override
			public ModelResourceLocation getModelLocation(ItemStack stack) {
				String name = EXACUtils.isActive(stack)? "ray_twister_active" : "ray_twister";
				return new ModelResourceLocation("exac:" + name, "inventory");
			}
	    });
		ModelBakery.registerItemVariants(EXACItems.ray_twister, 
				new ModelResourceLocation("exac:ray_twister", "inventory"),
				new ModelResourceLocation("exac:ray_twister_active", "inventory")
		);
		
        ModelLoader.setCustomModelResourceLocation(ACItems.induction_factor, 0, new ModelResourceLocation("exac:factor_aerohand", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ACItems.induction_factor, 1, new ModelResourceLocation("academy:factor_electromaster", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ACItems.induction_factor, 2, new ModelResourceLocation("academy:factor_meltdowner", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ACItems.induction_factor, 3, new ModelResourceLocation("exac:factor_telekinesis", "inventory"));
		ModelLoader.setCustomModelResourceLocation(ACItems.induction_factor, 4, new ModelResourceLocation("academy:factor_teleporter", "inventory"));
        ModelLoader.setCustomModelResourceLocation(ACItems.induction_factor, 5, new ModelResourceLocation("academy:factor_vecmanip", "inventory"));
	}

}
