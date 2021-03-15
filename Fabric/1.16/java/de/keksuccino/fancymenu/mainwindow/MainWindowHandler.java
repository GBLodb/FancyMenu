package de.keksuccino.fancymenu.mainwindow;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;

import de.keksuccino.fancymenu.FancyMenu;
import net.minecraft.client.MinecraftClient;

public class MainWindowHandler {

	private static String windowtitle = null;
	private static File icondir = new File("config/fancymenu/minecraftwindow/icons");
	
	public static void init() {
		if (!icondir.exists()) {
			icondir.mkdirs();
		}
	}
	
	public static void updateWindowIcon() {
		if (FancyMenu.config.getOrDefault("customwindowicon", false)) {
			try {
				File i16 = new File(icondir.getPath() + "/icon16x16.png");
				File i32 = new File(icondir.getPath() + "/icon32x32.png");
				if (!i16.exists() || !i32.exists()) {
					System.out.println("## ERROR ## [FANCYMENU] Unable to set custom icons: 'icon16x16.png' or 'icon32x32.png' missing!");
					return;
				}
				//Yes, I need to do this to get the image size.
				BufferedImage i16buff = ImageIO.read(i16);
				if ((i16buff.getHeight() != 16) || (i16buff.getWidth() != 16)) {
					System.out.println("'## ERROR ## [FANCYMENU] Unable to set custom icons: 'icon16x16.png' is not 16x16 pixels!");
					return;
				}
				BufferedImage i32buff = ImageIO.read(i32);
				if ((i32buff.getHeight() != 32) || (i32buff.getWidth() != 32)) {
					System.out.println("'## ERROR ## [FANCYMENU] Unable to set custom icons: 'icon32x32.png' is not 32x32 pixels!");
					return;
				}
				InputStream icon16 = new FileInputStream(i16);
				InputStream icon32 = new FileInputStream(i32);
				
				MinecraftClient.getInstance().getWindow().setIcon(icon16, icon32);
				System.out.println("[FANCYMENU] Custom minecraft icon successfully loaded!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void updateWindowTitle() {
		String s = FancyMenu.config.getOrDefault("customwindowtitle", "");
		if ((s != null) && (!s.equals(""))) {
			windowtitle = s;
			setWindowTitle();
		} else {
			windowtitle = null;
			MinecraftClient.getInstance().updateWindowTitle();
		}
	}

	private static void setWindowTitle() {
		if (windowtitle != null) {
			MinecraftClient.getInstance().getWindow().setTitle(windowtitle);
		}
	}
	
	public static String getCustomWindowTitle() {
		return windowtitle;
	}
	
}