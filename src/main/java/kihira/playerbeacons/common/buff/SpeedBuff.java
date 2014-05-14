package kihira.playerbeacons.common.buff;

import kihira.playerbeacons.api.IBeacon;
import kihira.playerbeacons.api.buff.Buff;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MathHelper;

public class SpeedBuff extends Buff {

	public SpeedBuff() {
		super("speed", 10, 1, 1);
	}

	@Override
	public void doBuff(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
		player.addPotionEffect(new PotionEffect(Potion.moveSpeed.id, 300, MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) - 1, true));
	}

	@Override
	public float getCorruption(EntityPlayer player, IBeacon theBeacon, int crystalCount) {
        return MathHelper.clamp_int(crystalCount, 0, theBeacon.getLevels()) * this.corruptionGenerated;
	}

    @Override
    public float[] getRGBA() {
        return new float[] {0.5F, 0.5F, 1F, 1F};
    }

	@Override
	public String getName() {
		return "Speed";
	}
}