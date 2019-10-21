package cn.nulladev.extraacc.core;

import cn.academy.ability.RegCategory;
import cn.nulladev.extraacc.ability.aerohand.CatAeroHand;
import cn.nulladev.extraacc.ability.aerohand.skill.Airflow;
import cn.nulladev.extraacc.ability.aerohand.skill.AscendingAir;
import cn.nulladev.extraacc.client.render.RenderAirGun;
import cn.nulladev.extraacc.entity.EntityAirBlade;
import cn.nulladev.extraacc.entity.EntityAirCannon;
import cn.nulladev.extraacc.entity.EntityAirWall;
import cn.nulladev.extraacc.entity.EntityBomberLance;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EXACRegistry {
	
	@RegCategory
    public static final CatAeroHand aerohand = new CatAeroHand();
	
	public static void register(Object ModObject) {
		registerEntities(ModObject);
		registerEvents();
	}
	
	private static void registerEntities(Object ModObject) {
		int modID = 1;
    	EntityRegistry.registerModEntity(new ResourceLocation("extraacc:air_cannon"), EntityAirCannon.class, "air_cannon", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("extraacc:air_blade"), EntityAirBlade.class, "air_blade", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("extraacc:air_wall"), EntityAirWall.class, "air_wall", modID++, ModObject, 128, 1, true);
    	EntityRegistry.registerModEntity(new ResourceLocation("extraacc:bomber_lance"), EntityBomberLance.class, "bomber_lance", modID++, ModObject, 128, 1, true);
	}
	
	private static void registerEvents() {
		MinecraftForge.EVENT_BUS.register(AscendingAir.INSTANCE);
		MinecraftForge.EVENT_BUS.register(Airflow.INSTANCE);
		FMLCommonHandler.instance().bus().register(Airflow.INSTANCE);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerClient() {

	}

}
