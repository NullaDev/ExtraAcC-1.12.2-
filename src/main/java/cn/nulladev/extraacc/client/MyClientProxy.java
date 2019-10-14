package cn.nulladev.extraacc.client;

import org.lwjgl.opengl.Display;

import cn.nulladev.extraacc.core.MyCommonProxy;
import cn.nulladev.extraacc.core.ExtraAcCRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class MyClientProxy extends MyCommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		Display.setTitle("Minceraft 1.12.2");
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
    	ExtraAcCRegistry.registerClient();
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		
	}

}