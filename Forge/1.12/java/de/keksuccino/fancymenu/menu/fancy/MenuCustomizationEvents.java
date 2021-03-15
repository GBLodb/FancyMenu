package de.keksuccino.fancymenu.menu.fancy;

import java.io.File;
import java.io.IOException;

import de.keksuccino.fancymenu.FancyMenu;
import de.keksuccino.fancymenu.mainwindow.MainWindowHandler;
import de.keksuccino.fancymenu.menu.fancy.MenuCustomization;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutCreatorScreen;
import de.keksuccino.fancymenu.menu.fancy.music.AdvancedMusicTicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MusicTicker;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class MenuCustomizationEvents {
	
	private boolean idle = false;
	private boolean iconSetAfterFullscreen = false;
	private boolean scaleChecked = false;
	private boolean resumeWorldMusic = false;
	
	@SubscribeEvent
	public void onInitPre(GuiScreenEvent.InitGuiEvent.Pre e) {
		//Stopping audio for all menu handlers when changing the screen
		if (MenuCustomization.isValidScreen(e.getGui()) && !LayoutCreatorScreen.isActive) {
			this.idle = false;
		}
		
		if (MenuCustomization.isValidScreen(e.getGui()) && !MenuCustomization.isMenuCustomizable(e.getGui()) && !(e.getGui() instanceof LayoutCreatorScreen)) {
			MenuCustomization.stopSounds();
			MenuCustomization.resetSounds();
		}

		//Stopping menu music when deactivated in config
		if ((Minecraft.getMinecraft().world == null)) {
			if (!FancyMenu.config.getOrDefault("playmenumusic", true)) {
				MusicTicker m = Minecraft.getMinecraft().getMusicTicker();
				if (m instanceof AdvancedMusicTicker) {
					((AdvancedMusicTicker)m).stop();
				}
			}
		} else {
			if (MenuCustomization.isMenuCustomizable(e.getGui()) && FancyMenu.config.getOrDefault("stopworldmusicwhencustomizable", false)) {
				Minecraft.getMinecraft().getSoundHandler().pauseSounds();
				this.resumeWorldMusic = true;
			}
		}
	}
	
	@SubscribeEvent
	public void onTick(ClientTickEvent e) {
		//Stopping audio for all menu handlers if no screen is being displayed
		if ((Minecraft.getMinecraft().currentScreen == null) && !this.idle) {
			MenuCustomization.stopSounds();
			MenuCustomization.resetSounds();
			this.idle = true;
		}
		
		if ((Minecraft.getMinecraft().world != null) && (Minecraft.getMinecraft().currentScreen == null) && this.resumeWorldMusic) {
			Minecraft.getMinecraft().getSoundHandler().resumeSounds();
			this.resumeWorldMusic = false;
		}
		
		if (Minecraft.getMinecraft().isFullScreen()) {
			this.iconSetAfterFullscreen = false;
		} else {
			if (!this.iconSetAfterFullscreen) {
				MainWindowHandler.updateWindowIcon();
				this.iconSetAfterFullscreen = true;
			}
		}
		
		if (!scaleChecked && (Minecraft.getMinecraft().gameSettings != null)) {
			scaleChecked = true;
			
			int scale = FancyMenu.config.getOrDefault("defaultguiscale", -1);
			if (scale != -1) {
				File f = new File("mods/fancymenu");
				if (!f.exists()) {
					f.mkdirs();
				}
				
				File f2 = new File(f.getPath() + "/defaultscaleset.fancymenu");
				if (!f2.exists()) {
					try {
						f2.createNewFile();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					Minecraft.getMinecraft().gameSettings.guiScale = scale;
					Minecraft.getMinecraft().gameSettings.saveOptions();
					
					Minecraft mc = Minecraft.getMinecraft();
					if (mc.currentScreen != null) {
						ScaledResolution scaledresolution = new ScaledResolution(mc);
			            int j = scaledresolution.getScaledWidth();
			            int k = scaledresolution.getScaledHeight();
			            mc.currentScreen.setWorldAndResolution(mc, j, k);
					}
				}
			}
		}
	}
	
}