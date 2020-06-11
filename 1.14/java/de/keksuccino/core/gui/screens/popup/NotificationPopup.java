package de.keksuccino.core.gui.screens.popup;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import de.keksuccino.core.gui.content.AdvancedButton;
import de.keksuccino.core.input.KeyboardData;
import de.keksuccino.core.input.KeyboardHandler;
import de.keksuccino.fancymenu.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.gui.screen.Screen;

public class NotificationPopup extends Popup {
	
	private List<String> text;
	private AdvancedButton accept;
	private int width;
	private Color color = new Color(76, 0, 128);
	private Runnable callback;
	
	public NotificationPopup(int width, @Nullable Color color, int backgroundAlpha, @Nullable Runnable callback, @Nonnull String... text) {
		super(backgroundAlpha);
		
		this.setNotificationText(text);
		this.width = width;
		this.accept = new AdvancedButton(0, 0, 100, 20, Locals.localize("popup.notification.accept"), true, (press) -> {
			this.setDisplayed(false);
			if (this.callback != null) {
				this.callback.run();
			}
		});
		this.addButton(this.accept);
		
		if (color != null) {
			this.color = color;
		}
		
		this.callback = callback;
		
		KeyboardHandler.addKeyPressedListener(this::onEnterOrEscapePressed);
	}
	
	@Override
	public void render(int mouseX, int mouseY, Screen renderIn) {
		super.render(mouseX, mouseY, renderIn);
		
		if (this.isDisplayed()) {
			int height = 50;
			
			for (int i = 0; i < this.text.size(); i++) {
				height += 10;
			}
			
			GlStateManager.enableBlend();
			IngameGui.fill((renderIn.width / 2) - (this.width / 2), (renderIn.height / 2) - (height / 2), (renderIn.width / 2) + (this.width / 2), (renderIn.height / 2) + (height / 2), this.color.getRGB());
			GlStateManager.disableBlend();
			
			int i = 0;
			for (String s : this.text) {
				renderIn.drawCenteredString(Minecraft.getInstance().fontRenderer, s, renderIn.width / 2, (renderIn.height / 2) - (height / 2) + 10 + i, Color.WHITE.getRGB());
				i += 10;
			}
			
			this.accept.x = (renderIn.width / 2) - (this.accept.getWidth() / 2);
			this.accept.y = ((renderIn.height / 2) + (height / 2)) - this.accept.getHeight() - 5;
			
			this.renderButtons(mouseX, mouseY);
		}
	}
	
	public void setNotificationText(String... text) {
		if (text != null) {
			List<String> l = new ArrayList<String>();
			for (String s : text) {
				if (s.contains("%n%")) {
					for (String s2 : s.split("%n%")) {
						l.add(s2);
					}
				} else {
					l.add(s);
				}
			}
			this.text = l;
		}
	}
	
	public void onEnterOrEscapePressed(KeyboardData d) {
		if (((d.keycode == 257) || (d.keycode == 256)) && this.isDisplayed()) {
			this.setDisplayed(false);
			if (this.callback != null) {
				this.callback.run();
			}
		}
	}
}
