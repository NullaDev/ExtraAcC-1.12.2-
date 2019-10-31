package cn.nulladev.extraacc.core;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ExtraAcC.MODID, name = ExtraAcC.MODNAME, version = ExtraAcC.VERSION, dependencies="required-after:academy")
public class ExtraAcC {

	public static final String MODID = "extraacc";
	public static final String MODNAME = "ExtraAcC";
	public static final String VERSION = "beta-1.31";
	
	static {
		AcCConfigHacker.try_to_register_firstly();
	}

	@Instance(MODID)
	public static ExtraAcC instance = new ExtraAcC();

	@SidedProxy(clientSide = "cn.nulladev.extraacc.client.MyClientProxy",
				serverSide = "cn.nulladev.extraacc.core.MyCommonProxy")
	public static MyCommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void Init(FMLInitializationEvent event) {
		EXACRegistry.register(instance);
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

}
