package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import net.minecraft.item.Item;

public class EXACItemNormal extends Item {
	
	public EXACItemNormal(String unlocalizedName) {
		super();
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(AcademyCraft.cct);
	}

}
