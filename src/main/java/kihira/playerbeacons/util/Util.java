package kihira.playerbeacons.util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;

public class Util {

    public static enum EnumHeadType {
        NONE(-1),
        SKELETON(0),
        WITHERSKELETON(1),
        ZOMBIE(2),
        PLAYER(3),
        CREEPER(4);

        private int id;

        private EnumHeadType(int id) {
            this.id = id;
        }

        public int getID() {
            return this.id;
        }

        public static EnumHeadType fromId(int id) {
            switch (id) {
                case -1:
                    return NONE;
                case 0:
                    return SKELETON;
                case 1:
                    return WITHERSKELETON;
                case 2:
                    return WITHERSKELETON;
                case 3:
                    return WITHERSKELETON;
                case 4:
                    return WITHERSKELETON;
                default:
                    return null;
            }
        }
    }

	public static MovingObjectPosition getBlockLookAt(EntityPlayer player, double maxBlockDistance) {
		Vec3 vec3 = player.worldObj.getWorldVec3Pool().getVecFromPool(player.posX, player.posY + (player.worldObj.isRemote ? 0.0D : (player.getEyeHeight() - 0.09D)), player.posZ);
		Vec3 vec31 = player.getLookVec();
		Vec3 vec32 = vec3.addVector(vec31.xCoord * maxBlockDistance, vec31.yCoord * maxBlockDistance, vec31.zCoord * maxBlockDistance);
		return player.worldObj.rayTraceBlocks(vec3, vec32);
	}

    public static ItemStack getHead(EnumHeadType headType, String owner) {
        return getHead(headType.getID(), owner);
    }

	public static ItemStack getHead(int id, String owner) {
		ItemStack itemStack = new ItemStack(Items.skull, 1, id);
		if (owner != null) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("SkullOwner", owner);
			itemStack.setTagCompound(tag);
		}
		return itemStack;
	}

    @SideOnly(Side.CLIENT)
	public static void drawLightning(double x1, double y1, double z1, double x2, double y2, double z2, double[] rgba) {
		drawLightning(x1, y1, z1, x2, y2, z2, rgba[3], rgba[0], rgba[1], rgba[2]);
	}

    @SideOnly(Side.CLIENT)
	public static void drawLightning(double x1, double y1, double z1, double x2, double y2, double z2, double a, double r, double g, double b) {
		double tx = x2 - x1, ty = y2 - y1, tz = z2 - z1;

		double ax = 0, ay = 0, az = 0;
		double bx = 0, by = 0, bz = 0;
		double cx = 0, cy = 0, cz = 0;

		double jagfactor = 0.1;
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		//Minecraft.getMinecraft().renderEngine.bindTexture(ClientProxy.lightningTexture);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_LIGHTING_BIT);
		if (Minecraft.isFancyGraphicsEnabled()) {
			GL11.glShadeModel(GL11.GL_SMOOTH);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		}
		GL11.glColor4d(r, g, b, a);
		GL11.glBegin(GL11.GL_QUADS);
		while (Math.abs(cx) < Math.abs(tx) && Math.abs(cy) < Math.abs(ty) && Math.abs(cz) < Math.abs(tz)) {
			ax = x1 + cx;
			ay = y1 + cy;
			az = z1 + cz;
			//cx += Math.random() * tx * jagfactor - 0.1 * tx;
			//cy += Math.random() * ty * jagfactor - 0.1 * ty;
			//cz += Math.random() * tz * jagfactor - 0.1 * tz;
			cx += Math.random() * tx * jagfactor - 0.1 * tx;
			cy += Math.random() * ty * jagfactor - 0.1 * ty;
			cz += Math.random() * tz * jagfactor - 0.1 * tz;
			bx = x1 + cx;
			by = y1 + cy;
			bz = z1 + cz;

			int index = (int) Math.random() * 50;

			drawLightningBetweenPointsFast(ax, ay, az, bx, by, bz, index);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glPopAttrib();
		GL11.glPopAttrib();
	}

    @SideOnly(Side.CLIENT)
	public static void drawLightningBetweenPointsFast(double x1, double y1, double z1, double x2, double y2, double z2, int index) {
		double u1 = index / 50.0;
		double u2 = u1 + 0.02;
		double px = (y1 - y2) * 0.125;
		double py = (x2 - x1) * 0.125;
		GL11.glTexCoord2d(u1, 0);
		GL11.glVertex3d(x1 - px, y1 - py, z1);
		GL11.glTexCoord2d(u2, 0);
		GL11.glVertex3d(x1 + px, y1 + py, z1);
		GL11.glTexCoord2d(u1, 1);
		GL11.glVertex3d(x2 - px, y2 - py, z2);
		GL11.glTexCoord2d(u2, 1);
		GL11.glVertex3d(x2 + px, y2 + py, z2);
	}
}
