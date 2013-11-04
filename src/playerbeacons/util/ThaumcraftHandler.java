package playerbeacons.util;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import playerbeacons.common.PlayerBeacons;
import thaumcraft.api.ItemApi;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.api.crafting.CrucibleRecipe;
import thaumcraft.api.crafting.IArcaneRecipe;
import thaumcraft.api.crafting.InfusionRecipe;
import thaumcraft.api.research.ResearchItem;
import thaumcraft.api.research.ResearchPage;

//TODO Balance everything
public class ThaumcraftHandler {

	public final String KEY_CRYSTAL = "PB_CRYSTAL";
	public final String KEY_BEACON = "PB_BEACON";
	public final String KEY_DIMENSION = "PB_DIMENSION";

	private IArcaneRecipe crystalRecipe;
	private IArcaneRecipe defiledConductorBlockRecipe;
	private IRecipe defiledPylonBlockRecipe;
	private CrucibleRecipe brownCrystalRecipe;
	private CrucibleRecipe greenCrystalRecipe;
	private CrucibleRecipe lightBlueCrystalRecipe;
	private CrucibleRecipe blackCrystalRecipe;
	private InfusionRecipe beaconRecipe;

	public ThaumcraftHandler() {
		registerCraftingRecipes();
		registerCrucibleRecipes();
		registerInfusionRecipes();
		registerResearch();
	}

	private void registerCraftingRecipes() {
		crystalRecipe = ThaumcraftApi.addArcaneCraftingRecipe(KEY_CRYSTAL, new ItemStack(PlayerBeacons.crystalItem, 2), new AspectList().add(Aspect.TRAP, 4).add(Aspect.EXCHANGE, 4), "cPO", "CEv", "oPV", 'c', ItemApi.getItem("itemShard", 0), 'C', ItemApi.getItem("itemShard", 1), 'o', ItemApi.getItem("itemShard", 2), 'O', ItemApi.getItem("itemShard", 3), 'v', ItemApi.getItem("itemShard", 4), 'V', ItemApi.getItem("itemShard", 5), 'P', new ItemStack(Item.enderPearl), 'E', new ItemStack(Item.emerald));
		defiledConductorBlockRecipe = ThaumcraftApi.addArcaneCraftingRecipe(KEY_BEACON, new ItemStack(PlayerBeacons.defiledSoulConductorBlock, 4), new AspectList().add(Aspect.EXCHANGE, 4), "OPO", "MMM", "OPO", 'O', new ItemStack(Block.obsidian), 'P', new ItemStack(Item.enderPearl), 'M', ItemApi.getBlock("blockCosmeticSolid", 6));
		defiledPylonBlockRecipe = GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.defiledSoulPylonBlock, 2), "OPO", "G G", "OPO", 'O', new ItemStack(PlayerBeacons.defiledSoulConductorBlock), 'P', new ItemStack(Item.enderPearl), 'G', new ItemStack(Item.ingotGold));
		GameRegistry.addShapedRecipe(new ItemStack(PlayerBeacons.beheaderItem), "LIL", "IPI", "S S", 'P', new ItemStack(Item.enderPearl), 'S', new ItemStack(Item.swordIron), 'L', new ItemStack(Item.leather), 'I', new ItemStack(Item.ingotIron));
	}

	private void registerInfusionRecipes() {
		ItemStack[] itemStacks = new ItemStack[8];
		for (int i = 0; i < 8; i++) {
			if (i % 2 == 0) itemStacks[i] = new ItemStack(PlayerBeacons.defiledSoulConductorBlock);
			else itemStacks[i] = new ItemStack(Item.enderPearl);
		}
		beaconRecipe = ThaumcraftApi.addInfusionCraftingRecipe(KEY_BEACON, new ItemStack(PlayerBeacons.playerBeaconBlock), 10, new AspectList().add(Aspect.SOUL, 30).add(Aspect.ELDRITCH, 30).add(Aspect.EXCHANGE, 20).add(Aspect.AURA, 20), new ItemStack(Block.beacon), itemStacks);
	}

	private void registerCrucibleRecipes() {
		brownCrystalRecipe = ThaumcraftApi.addCrucibleRecipe(KEY_CRYSTAL, new ItemStack(PlayerBeacons.brownCrystalItem), new ItemStack(PlayerBeacons.crystalItem), new AspectList().add(Aspect.EARTH, 10));
		greenCrystalRecipe = ThaumcraftApi.addCrucibleRecipe(KEY_CRYSTAL, new ItemStack(PlayerBeacons.greenCrystalItem), new ItemStack(PlayerBeacons.crystalItem), new AspectList().add(Aspect.MOTION, 10));
		lightBlueCrystalRecipe = ThaumcraftApi.addCrucibleRecipe(KEY_CRYSTAL, new ItemStack(PlayerBeacons.lightBlueCrystalItem), new ItemStack(PlayerBeacons.crystalItem), new AspectList().add(Aspect.MOTION, 10));
		blackCrystalRecipe = ThaumcraftApi.addCrucibleRecipe(KEY_CRYSTAL, new ItemStack(PlayerBeacons.blackCrystalItem), new ItemStack(PlayerBeacons.crystalItem), new AspectList().add(Aspect.ARMOR, 10));
	}

	private void registerResearch() {
		ResearchItem researchItem;

		researchItem = new ResearchItem(KEY_DIMENSION, "BASICS", new AspectList(), 0, 2, 0, new ItemStack(Item.enderPearl)).setRound().setAutoUnlock();
		researchItem.setPages(new ResearchPage("research.pbdimensional.page.0")).registerResearchItem();
		researchItem = new ResearchItem(KEY_BEACON, "BASICS", new AspectList().add(Aspect.ELDRITCH, 6).add(Aspect.SOUL, 4), -1, 4, 2, new ItemStack(PlayerBeacons.playerBeaconBlock)).setParents(KEY_DIMENSION).setParentsHidden("ARCANESTONE");
		researchItem.setPages(new ResearchPage("research.pbbeacon.page.0"), new ResearchPage(beaconRecipe), new ResearchPage(defiledConductorBlockRecipe), new ResearchPage(defiledPylonBlockRecipe)).registerResearchItem();
		researchItem = new ResearchItem(KEY_CRYSTAL, "BASICS", new AspectList().add(Aspect.CRYSTAL, 6).add(Aspect.EXCHANGE, 4).add(Aspect.TRAP, 4), -2, 6, 1, new ItemStack(PlayerBeacons.crystalItem)).setRound().setParents(KEY_BEACON);
		researchItem.setPages(new ResearchPage("research.pbcrystal.page.0"), new ResearchPage(crystalRecipe), new ResearchPage(brownCrystalRecipe), new ResearchPage(greenCrystalRecipe), new ResearchPage(lightBlueCrystalRecipe), new ResearchPage(blackCrystalRecipe)).registerResearchItem();

	}
}