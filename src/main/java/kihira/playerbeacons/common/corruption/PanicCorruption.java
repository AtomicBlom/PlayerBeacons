package kihira.playerbeacons.common.corruption;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kihira.playerbeacons.api.corruption.CorruptionEffect;
import kihira.playerbeacons.common.PlayerBeacons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.sound.PlaySoundEvent17;
import net.minecraftforge.common.MinecraftForge;
import org.lwjgl.opengl.GL11;

public class PanicCorruption extends CorruptionEffect {

    private final Multiset<EntityPlayer> multiset = HashMultiset.create();
    private boolean allowNextSound;

    public PanicCorruption() {
        super("panic", CORRUPTION_MAX);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void init(EntityPlayer player, float corruption) {

    }

    @Override
    public void onUpdate(EntityPlayer player, float corruption) {
        //Start
        if (!multiset.contains(player)) {
            Minecraft.getMinecraft().getSoundHandler().playSound(new MovingSoundHeartbeat(player)); //TODO move to proxy
            multiset.add(player);
        }
        if (multiset.count(player) < 300) multiset.add(player, 1);

        //if (player.getRNG().nextInt(Math.abs(multiset.count(player) - 400)) == 0) {
            this.allowNextSound = true;
            player.worldObj.playSound(player.posX + MathHelper.getRandomDoubleInRange(player.getRNG(), -10, 10), player.posY + MathHelper.getRandomDoubleInRange(player.getRNG(), -10, 10),
                    player.posZ + MathHelper.getRandomDoubleInRange(player.getRNG(), -10, 10), "mob.spider.say", 1F, player.getRNG().nextFloat() * 0.1F * 0.9F, false);
        //}
    }

    @Override
    public void finish(EntityPlayer player, float corruption) {
        multiset.setCount(player, 0);
    }

    @Override
    public boolean shouldActivate(EntityPlayer player, World world, float corruption) {
        return super.shouldActivate(player, world, corruption);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (multiset.contains(Minecraft.getMinecraft().thePlayer)) {
            GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
            Minecraft.getMinecraft().gameSettings.gammaSetting = -multiset.count(Minecraft.getMinecraft().thePlayer) / 2F;
            event.density = multiset.count(Minecraft.getMinecraft().thePlayer) / 1000F;
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onFogColour(EntityViewRenderEvent.FogColors event) {
        if (multiset.contains(Minecraft.getMinecraft().thePlayer)) {
            event.red -= (multiset.count(Minecraft.getMinecraft().thePlayer) / 200F);
            event.green -= (multiset.count(Minecraft.getMinecraft().thePlayer) / 200F);
            event.blue -= (multiset.count(Minecraft.getMinecraft().thePlayer) / 200F);
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (multiset.count(Minecraft.getMinecraft().thePlayer) > 200F) event.setCanceled(true);
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onSound(PlaySoundEvent17 event) {
        if (multiset.contains(Minecraft.getMinecraft().thePlayer) && (!(event.sound instanceof MovingSoundHeartbeat) && !allowNextSound)) event.result = null;
        allowNextSound = false;
    }

    @SideOnly(Side.CLIENT)
    public class MovingSoundHeartbeat extends MovingSound {

        private final EntityPlayer player;

        public MovingSoundHeartbeat(EntityPlayer player) {
            super(new ResourceLocation(PlayerBeacons.MOD_ID, "player.heartbeat"));

            this.player = player;
            this.field_147666_i = ISound.AttenuationType.NONE;
            this.repeat = true;
            this.field_147665_h = 5;
            this.volume = 0.01F;
        }

        @Override
        public void update() {
            if (!this.player.isDead && multiset.contains(this.player)) {
                this.volume = multiset.count(this.player) / 300F;
            }
            else {
                this.donePlaying = true;
            }
        }
    }
}
