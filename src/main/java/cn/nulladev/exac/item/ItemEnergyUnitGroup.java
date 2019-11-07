package cn.nulladev.exac.item;

import cn.academy.AcademyCraft;
import cn.academy.item.ItemEnergyBase;

public class ItemEnergyUnitGroup extends ItemEnergyBase {

	public ItemEnergyUnitGroup() {
		super(50000, 100);
		this.setUnlocalizedName("energyUnitGroup");
		this.setCreativeTab(AcademyCraft.cct);
	}

}
