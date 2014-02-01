package playerbeacons.block;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import playerbeacons.common.DamageBehead;
import playerbeacons.common.PlayerBeacons;
import playerbeacons.item.CrystalItem;
import playerbeacons.tileentity.TileEntityPlayerBeacon;

import java.util.Random;

public class BlockPlayerBeacon extends Block implements ITileEntityProvider {

	public BlockPlayerBeacon(int id) {
		super(id, Material.rock);
		setHardness(8f);
		setResistance(100.0F);
		setCreativeTab(PlayerBeacons.tabPlayerBeacons);
		setUnlocalizedName("playerBeacon");
		setTextureName("playerbeacon:pyramidBrick");
	}

	public boolean isOpaqueCube() {
		return false;
	}

	public int getRenderType() {
		return -1;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public TileEntity createNewTileEntity(World world) {
		return new TileEntityPlayerBeacon();
	}

	public boolean canEntityDestroy(World world, int x, int y, int z, Entity entity) {
		return !(entity instanceof EntityDragon);
	}

	public boolean removeBlockByPlayer(World world, EntityPlayer player, int x, int y, int z) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPlayerBeacon) {
				TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) tileEntity;
				if ((player.username.equals(((TileEntityPlayerBeacon) tileEntity).getOwner())) || (player.capabilities.isCreativeMode) || ((TileEntityPlayerBeacon) tileEntity).getOwner().equals(" ")) {
					tileEntityPlayerBeacon.doCorruption(true);
					tileEntity.invalidate();
					return world.setBlockToAir(x, y, z);
				}
				else {
					player.attackEntityFrom(new DamageBehead(), 10);
					player.sendChatToPlayer(ChatMessageComponent.createFromText("§d§oA mystical energy seems to guard this device"));
				}
			}
		}
		return false;
	}

	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer entityPlayer, int meta, float par7, float par8, float par9) {
		if (!world.isRemote) {
			if (entityPlayer.getCurrentItemOrArmor(0) != null) {
				if (entityPlayer.getCurrentItemOrArmor(0).getItem().itemID == Item.emerald.itemID) {
					ItemStack itemStack = entityPlayer.getCurrentItemOrArmor(0);
					if (itemStack.stackSize == 1) entityPlayer.setCurrentItemOrArmor(0, null);
					else entityPlayer.setCurrentItemOrArmor(0, new ItemStack(Item.emerald, itemStack.stackSize - 1));
					EntityItem item = new EntityItem(world, x, y + 0.5, z, new ItemStack(PlayerBeacons.crystalItem));
					world.spawnEntityInWorld(item);
					return true;
				}
				//If they right click with depleted, disperse all corruption
				else if (entityPlayer.getCurrentItemOrArmor(0).getItem() instanceof CrystalItem) {
                    entityPlayer.inventory.setInventorySlotContents(entityPlayer.inventory.currentItem, null);
					TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon) world.getBlockTileEntity(x, y, z);
					tileEntityPlayerBeacon.doCorruption(true);
					tileEntityPlayerBeacon.setCorruption(0, true);
					world.markBlockForUpdate(x, y, z);
					entityPlayer.sendChatToPlayer(ChatMessageComponent.createFromText("§3§oThe crystal fizzles away as it interacts with the beacon, releasing the corruption from within it"));
					return true;
				}
			}
		}
		return false;
	}

	public void onBlockClicked(World world, int x, int y, int z, EntityPlayer player) {
		if (!world.isRemote) {
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tileEntity instanceof TileEntityPlayerBeacon) {
				if (!(player.username.equals(((TileEntityPlayerBeacon) tileEntity).getOwner())) && !(player.capabilities.isCreativeMode) && !((TileEntityPlayerBeacon) tileEntity).getOwner().equals(" ")) {
					player.attackEntityFrom(new DamageBehead(), 2);
					player.sendChatToPlayer(ChatMessageComponent.createFromText("§d§oA mystical energy seems to guard this device"));
				}
			}
		}
	}

	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack) {
		if (!world.isRemote) {
			NBTTagCompound tagCompound = PlayerBeacons.beaconData.loadBeaconInformation(world, par5EntityLivingBase.getEntityName());
			TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
			if (tagCompound != null) {
				EntityPlayer player = (EntityPlayer) par5EntityLivingBase;
				if (tileEntity instanceof TileEntityPlayerBeacon) ((TileEntityPlayerBeacon) tileEntity).initialSetup(null);
				player.sendChatToPlayer(ChatMessageComponent.createFromText("§e§oYour soul is already bound to this dimension, rendering the beacon unbound"));
			}
			else {
				if (tileEntity instanceof TileEntityPlayerBeacon) ((TileEntityPlayerBeacon) tileEntity).initialSetup((EntityPlayer) par5EntityLivingBase);
			}
		}
	}

	public void randomDisplayTick(World world, int x, int y, int z, Random random) {
		if (world.getBlockId(x, y + 1, z) == Block.skull.blockID) {
			TileEntityPlayerBeacon tileEntityPlayerBeacon = (TileEntityPlayerBeacon)world.getBlockTileEntity(x, y, z);
			float corrupution = tileEntityPlayerBeacon.getCorruption();
			for (int l = 0; l < (corrupution / 500); ++l) {
				double d1 = (double)((float)y + random.nextFloat());
				int i1 = random.nextInt(2) * 2 - 1;
				int j1 = random.nextInt(2) * 2 - 1;
				double d3 = ((double)random.nextFloat() - 0.5D) * 0.125D;
				double d5 = (double)z + 0.5D + 0.25D * (double)j1;
				double d4 = (double)(random.nextFloat() * 1.0F * (float)j1);
				double d6 = (double)x + 0.5D + 0.25D * (double)i1;
				double d2 = (double)(random.nextFloat() * 1.0F * (float)i1);
				world.spawnParticle("portal", d6, d1, d5, d2, d3, d4);
			}
		}
	}
}
