package cn.nulladev.exac.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExtraAcademy.MODID, name = ExtraAcademy.MODNAME, version = ExtraAcademy.VERSION, dependencies="required-after:academy")
public class ExtraAcademy {

	public static final String MODID = "exac";
	public static final String MODNAME = "ExtraAcademy";
	public static final String VERSION = "1.2.0-pre";
	
	static {
		AcademyConfigHacker.modifyConfig();
	}

	@Instance(MODID)
	public static ExtraAcademy instance = new ExtraAcademy();

	@SidedProxy(clientSide = "cn.nulladev.exac.client.MyClientProxy",
				serverSide = "cn.nulladev.exac.core.MyCommonProxy")
	public static MyCommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		EXACRegistry.INSTANCE.registerEvents();
		proxy.preInit(event);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		EXACRegistry.INSTANCE.registerAbility();
		EXACRegistry.INSTANCE.registerEntities(instance);
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		EXACRegistry.INSTANCE.registerRecipes();
		proxy.postInit(event);
	}

}
