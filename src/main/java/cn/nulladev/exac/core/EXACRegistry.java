package cn.nulladev.exac.core;

import cn.academy.ability.CategoryManager;
import cn.academy.ability.RegCategory;
import cn.lambdalib2.crafting.RecipeRegistry;
import cn.nulladev.exac.ability.aerohand.CatAeroHand;
import cn.nulladev.exac.ability.aerohand.skill.Airflow;
import cn.nulladev.exac.ability.aerohand.skill.AscendingAir;
import cn.nulladev.exac.entity.EntityAirBlade;
import cn.nulladev.exac.entity.EntityAirCannon;
import cn.nulladev.exac.entity.EntityAirWall;
import cn.nulladev.exac.entity.EntityBomberLance;
import cn.nulladev.exac.entity.EntityCooler;
import cn.nulladev.exac.entity.EntityOffenseArmour;
import cn.nulladev.exac.item.ItemConstraintArmor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EXACRegistry {
	
	public static final EXACRegistry INSTANCE = new EXACRegistry();
	
	public void registerAbility() {
	    CatAeroHand aerohand = new CatAeroHand();
	    CategoryManager.INSTANCE.register(aerohand);
	}
	
	public void registerEntities(Object ModObject) {
		int modID = 1;
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:air_cannon"), EntityAirCannon.class, "air_cannon", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:air_blade"), EntityAirBlade.class, "air_blade", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:air_wall"), EntityAirWall.class, "air_wall", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:bomber_lance"), EntityBomberLance.class, "bomber_lance", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:cooler"), EntityCooler.class, "cooler", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("exac:offense_armour"), EntityOffenseArmour.class, "offense_armour", modID++, ModObject, 128, 1, true);
	}
	
	public void registerEvents() {
		MinecraftForge.EVENT_BUS.register(EXACRegistry.INSTANCE);
		MinecraftForge.EVENT_BUS.register(AscendingAir.INSTANCE);
		MinecraftForge.EVENT_BUS.register(Airflow.INSTANCE);
	}
	
	public void registerRecipes() {
		RecipeRegistry recipes = new RecipeRegistry();
		recipes.addRecipeFromResourceLocation(new ResourceLocation("exac:recipes/default.recipe"));
	}
	
	@SubscribeEvent
	/**Register<T>具有10种泛型类型参数：<Item>,<Block>，<Potion>（药水），<Biome>（生物群系），<SoundEvent>
    （声音事件），<PotionType>（药水类型），<Enchantment>（附魔），<VillagerProfession>（村民职业），<EntityEntry>
     （实体），<IRecipe>（合成表），你可以在ForgeRegistries类中看到这些泛型参数，也可以不使用泛型参数，直接用
      RegisterEvent监听所有事件*/
	public void addItems(RegistryEvent.Register<Item> event) {
		event.getRegistry().register(EXACItems.constraint_helmet.setRegistryName(ExtraAcademy.MODID, "constraint_helmet"));
		event.getRegistry().register(EXACItems.constraint_chestplate.setRegistryName(ExtraAcademy.MODID, "constraint_chestplate"));
		event.getRegistry().register(EXACItems.constraint_leggings.setRegistryName(ExtraAcademy.MODID, "constraint_leggings"));
		event.getRegistry().register(EXACItems.constraint_boots.setRegistryName(ExtraAcademy.MODID, "constraint_boots"));
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            registerItemRenderers();
        }
	}
	
	@SideOnly(Side.CLIENT)
	public void registerItemRenderers() {
		ModelLoader.setCustomModelResourceLocation(EXACItems.constraint_helmet, 0, new ModelResourceLocation("exac:constraint_helmet", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.constraint_chestplate, 0, new ModelResourceLocation("exac:constraint_chestplate", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.constraint_leggings, 0, new ModelResourceLocation("exac:constraint_leggings", "inventory"));
		ModelLoader.setCustomModelResourceLocation(EXACItems.constraint_boots, 0, new ModelResourceLocation("exac:constraint_boots", "inventory"));
	}

}
