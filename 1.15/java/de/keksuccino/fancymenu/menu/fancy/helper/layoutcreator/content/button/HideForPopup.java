package de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.content.button;

import java.awt.Color;
import java.util.function.Consumer;

import com.mojang.blaze3d.systems.RenderSystem;

import de.keksuccino.core.gui.content.AdvancedButton;
import de.keksuccino.core.gui.screens.popup.TextInputPopup;
import de.keksuccino.core.input.CharacterFilter;
import de.keksuccino.core.input.KeyboardData;
import de.keksuccino.fancymenu.localization.Locals;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;

public class HideForPopup extends TextInputPopup {
	
	private AdvancedButton delayEverytimeBtn;
	private AdvancedButton delayOnlyFirstTimeBtn;
	private Consumer<Boolean> callback2;
	private boolean onlyfirsttime = false;
	private int backgroundalpha = 0;

	public HideForPopup(String title, CharacterFilter filter, int backgroundAlpha, Consumer<String> callback, Consumer<Boolean> callbackDelayOnlyFirstTime, boolean delayOnlyFirstTime) {
		super(new Color(0, 0, 0, 0), title, filter, backgroundAlpha, callback);
		this.callback2 = callbackDelayOnlyFirstTime;
		this.onlyfirsttime = delayOnlyFirstTime;
		this.backgroundalpha = backgroundAlpha;
		
		String eBtn = "§a" + Locals.localize("helper.creator.popup.hidefor.delayeverytime");
		if (delayOnlyFirstTime) {
			eBtn = Locals.localize("helper.creator.popup.hidefor.delayeverytime");
		}
		this.delayEverytimeBtn = new AdvancedButton(0, 0, 100, 20, eBtn, true, (press) -> {
			this.onlyfirsttime = false;
			callback2.accept(this.onlyfirsttime);
			press.setMessage("§a" + Locals.localize("helper.creator.popup.hidefor.delayeverytime"));
			this.delayOnlyFirstTimeBtn.setMessage(Locals.localize("helper.creator.popup.hidefor.delayfirsttime"));
		});
		this.addButton(this.delayEverytimeBtn);
		
		String fBtn = Locals.localize("helper.creator.popup.hidefor.delayfirsttime");
		if (delayOnlyFirstTime) {
			fBtn = "§a" + Locals.localize("helper.creator.popup.hidefor.delayfirsttime");
		}
		this.delayOnlyFirstTimeBtn = new AdvancedButton(0, 0, 100, 20, fBtn, true, (press) -> {
			this.onlyfirsttime = true;
			callback2.accept(this.onlyfirsttime);
			press.setMessage("§a" + Locals.localize("helper.creator.popup.hidefor.delayfirsttime"));
			this.delayEverytimeBtn.setMessage(Locals.localize("helper.creator.popup.hidefor.delayeverytime"));
		});
		this.addButton(this.delayOnlyFirstTimeBtn);
		
	}
	
	@Override
	public void render(int mouseX, int mouseY, Screen renderIn) {
		if (!this.isDisplayed()) {
			return;
		}
		
		int height = 100;
		
		RenderSystem.enableBlend();
		fill(0, 0, renderIn.width, renderIn.height, new Color(0, 0, 0, this.backgroundalpha).getRGB());
		RenderSystem.disableBlend();
		
		renderIn.drawCenteredString(Minecraft.getInstance().fontRenderer, title, renderIn.width / 2, (renderIn.height / 2) - (height / 2) + 10, Color.WHITE.getRGB());
		
		this.textField.x = (renderIn.width / 2) - (this.textField.getWidth() / 2);
		this.textField.y = (renderIn.height / 2) - (this.textField.getHeight() / 2);
		this.textField.renderButton(mouseX, mouseY, Minecraft.getInstance().getRenderPartialTicks());

		this.delayEverytimeBtn.x = (renderIn.width / 2) - this.delayEverytimeBtn.getWidth() - 5;
		this.delayEverytimeBtn.y = ((renderIn.height  / 2) + 50) - this.delayEverytimeBtn.getHeight() - 5;
		
		this.delayOnlyFirstTimeBtn.x = (renderIn.width / 2) + 5;
		this.delayOnlyFirstTimeBtn.y = ((renderIn.height  / 2) + 50) - this.delayOnlyFirstTimeBtn.getHeight() - 5;
		
		this.doneButton.x = (renderIn.width / 2) - (this.doneButton.getWidth() / 2);
		this.doneButton.y = ((renderIn.height / 2) + 90) - this.doneButton.getHeight() - 5;
		
		this.renderButtons(mouseX, mouseY);
	}
	
	@Override
	public void onEnterPressed(KeyboardData d) {
		super.onEnterPressed(d);
		
		if ((d.keycode == 257) && this.isDisplayed()) {
			if (this.callback2 != null) {
				this.callback2.accept(this.onlyfirsttime);
			}
		}
	}

}