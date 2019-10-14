package cn.nulladev.extraacc.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;

import javax.swing.JOptionPane;

import cn.lambdalib2.util.ResourceUtils;
import net.minecraft.util.ResourceLocation;

public class AcCConfigHacker {
	
static final String HEAD = "# ExtraAcC HEAD";
	
	public static void try_to_register_firstly() {
		
		ResourceLocation defaultRes = new ResourceLocation("extraacc:config/default.conf");

        File customFile = new File("config/academy-craft-data.conf");
        try {
        	if (customFile.isFile() && customFile.exists()) {
        		InputStreamReader read = new InputStreamReader(new FileInputStream(customFile));
    			BufferedReader bufferedReader = new BufferedReader(read);
    			String s = bufferedReader.readLine();
    			if (s.equals(HEAD)) {
        			bufferedReader.close();	
    				return;
    			} else {
    		        if (!customFile.delete()) {
    		        	String str = "警告：你的游戏不能正常运行。\n"
    		        			+ "这是因为无良AcademyCraft开发者使用硬编码生成技能的配置文件，因此，ExtraAcC中的新技能无法被注册。\n"
    		        			+ "要解决此问题，请删除academy-craft-data.conf配置文件并重新启动游戏。\n"
    		        			+ "如果该解决方法不能奏效，请联系bug-report@nulladev.cn。";
    		        	JOptionPane.showMessageDialog(null, str, "错误!", JOptionPane.ERROR_MESSAGE);
    		        }
    			}
    			bufferedReader.close();	
            }
            Files.copy(ResourceUtils.getResourceStream(defaultRes), customFile.toPath());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
