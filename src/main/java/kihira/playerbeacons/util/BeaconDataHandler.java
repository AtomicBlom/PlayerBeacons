package kihira.playerbeacons.util;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class BeaconDataHandler {

	private NBTTagCompound beaconList = new NBTTagCompound();

	public BeaconDataHandler() {
		loadData();
	}

	private void loadData() {
		File mainFile = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat");

		if (mainFile.exists()) {
			try {
				this.beaconList = CompressedStreamTools.readCompressed(new FileInputStream(mainFile));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void saveData(NBTTagCompound data) {
		try {
			File mainFileNew = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat_new");
			File backupFile = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat_old");
			File mainFile = new File(DimensionManager.getCurrentSaveRootDirectory().getAbsolutePath(), "playerbeacons.dat");
			CompressedStreamTools.writeCompressed(data, new FileOutputStream(mainFileNew));

			if (backupFile.exists()) backupFile.delete();
			mainFile.renameTo(backupFile);
			if (mainFile.exists()) mainFile.delete();
			mainFileNew.renameTo(mainFile);
			if (mainFileNew.exists()) mainFileNew.delete();
		}
		catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public boolean updateBeaconInformation(World world, String player, int x, int y, int z, boolean isActive, float corruption, int levels, short corruptionLevels) {
		if (this.beaconList.hasKey(player)) {
			NBTTagCompound playerData = this.beaconList.getCompoundTag(player);
			if (playerData.hasKey(String.valueOf(world.provider.dimensionId))) {
				NBTTagCompound playerDataWorld = playerData.getCompoundTag(String.valueOf(world.provider.dimensionId));
				playerDataWorld.setInteger("x", x);
				playerDataWorld.setInteger("y", y);
				playerDataWorld.setInteger("z", z);
				playerDataWorld.setBoolean("inactive", isActive);
				playerDataWorld.setFloat("badstuff", corruption);
				playerDataWorld.setInteger("levels", levels);
				playerDataWorld.setShort("badstufflevel", corruptionLevels);
				playerData.setTag(String.valueOf(world.provider.dimensionId), playerDataWorld);
				saveData(this.beaconList);
				return true;
			}
		}
		return false;
	}

	public void deleteBeaconInformation(World world, String username) {
		if (this.beaconList.hasKey(username)) {
			NBTTagCompound playerData = this.beaconList.getCompoundTag(username);
			if (playerData.hasKey(String.valueOf(world.provider.dimensionId))) {
				playerData.removeTag(String.valueOf(world.provider.dimensionId));
				saveData(this.beaconList);
			}
		}
	}

	public NBTTagCompound loadBeaconInformation(World world, String username) {
		if (this.beaconList.hasKey(username)) {
			NBTTagCompound playerData = this.beaconList.getCompoundTag(username);
			if (playerData.hasKey(String.valueOf(world.provider.dimensionId))) {
				return playerData.getCompoundTag(String.valueOf(world.provider.dimensionId));
			}
		}
		return null;
	}

	public void addBeaconInformation(World world, String player, NBTTagCompound nbtTagCompound) {
		if (!this.beaconList.hasKey(player)) {
			NBTTagCompound newPlayerData = new NBTTagCompound();
            this.beaconList.setTag(player, newPlayerData);
		}
		if (this.beaconList.hasKey(player)) {
			NBTTagCompound playerData = this.beaconList.getCompoundTag(player);
			if (!playerData.hasKey(String.valueOf(world.provider.dimensionId))) {
				playerData.setTag(String.valueOf(world.provider.dimensionId), nbtTagCompound);
				saveData(this.beaconList);
			}
		}
	}

	public void addBeaconInformation(World world, String player, int x, int y, int z, boolean isActive, float corruption, int levels, short corruptionLevels) {
		if (!this.beaconList.hasKey(player)) {
			NBTTagCompound newPlayerData = new NBTTagCompound();
            this.beaconList.setTag(player, newPlayerData);
		}
		if (this.beaconList.hasKey(player)) {
			NBTTagCompound playerData = this.beaconList.getCompoundTag(player);
			if (!playerData.hasKey(String.valueOf(world.provider.dimensionId))) {
				NBTTagCompound playerDataWorld = new NBTTagCompound();
				playerDataWorld.setInteger("x", x);
				playerDataWorld.setInteger("y", y);
				playerDataWorld.setInteger("z", z);
				playerDataWorld.setBoolean("inactive", isActive);
				playerDataWorld.setFloat("badstuff", corruption);
				playerDataWorld.setInteger("levels", levels);
				playerDataWorld.setShort("badstufflevel", corruptionLevels);
				playerData.setTag(String.valueOf(world.provider.dimensionId), playerDataWorld);
				saveData(this.beaconList);
			}
		}
	}
}