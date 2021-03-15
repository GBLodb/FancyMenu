package de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.content;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import de.keksuccino.konkrete.localization.Locals;
import de.keksuccino.fancymenu.menu.fancy.helper.layoutcreator.LayoutCreatorScreen;
import de.keksuccino.fancymenu.menu.fancy.item.StringCustomizationItem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import de.keksuccino.konkrete.gui.screens.popup.PopupHandler;
import de.keksuccino.konkrete.gui.screens.popup.TextInputPopup;
import de.keksuccino.konkrete.input.CharacterFilter;
import de.keksuccino.konkrete.input.StringUtils;
import de.keksuccino.konkrete.math.MathUtils;
import de.keksuccino.konkrete.properties.PropertiesSection;
import de.keksuccino.konkrete.rendering.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;

public class LayoutString extends LayoutObject {
	
	public LayoutString(StringCustomizationItem parent, LayoutCreatorScreen handler) {
		super(parent, true, handler);
		this.setScale(this.getStringScale());
	}
	
	@Override
	protected void init() {
		super.init();
		
		AdvancedButton scaleB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.string.setscale"), true, (press) -> {
			this.handler.setMenusUseable(false);
			PopupHandler.displayPopup(new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.string.setscale") + ":", CharacterFilter.getDoubleCharacterFiler(), 240, this::setScaleCallback));
		});
		this.rightclickMenu.addContent(scaleB);
		LayoutCreatorScreen.colorizeCreatorButton(scaleB);
		
		String sLabel = Locals.localize("helper.creator.items.string.setshadow");
		if (this.getObject().shadow) {
			sLabel = Locals.localize("helper.creator.items.string.setnoshadow");
		}
		AdvancedButton shadowB = new AdvancedButton(0, 0, 0, 16, sLabel, true, (press) -> {
			if (this.getObject().shadow) {
				((AdvancedButton)press).setMessage(Locals.localize("helper.creator.items.string.setshadow"));
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				
				this.getObject().shadow = false;
			} else {
				((AdvancedButton)press).setMessage(Locals.localize("helper.creator.items.string.setnoshadow"));
				this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
				
				this.getObject().shadow = true;
			}
		});
		this.rightclickMenu.addContent(shadowB);
		LayoutCreatorScreen.colorizeCreatorButton(shadowB);
		
		AdvancedButton editTextB = new AdvancedButton(0, 0, 0, 16, Locals.localize("helper.creator.items.string.edit"), true, (press) -> {
			this.handler.setMenusUseable(false);
			TextInputPopup i = new TextInputPopup(new Color(0, 0, 0, 0), "§l" + Locals.localize("helper.creator.items.string.edit") + ":", null, 240, this::setTextCallback);
			i.setText(StringUtils.convertFormatCodes(this.object.value, "§", "&"));
			PopupHandler.displayPopup(i);
		});
		this.rightclickMenu.addContent(editTextB);
		LayoutCreatorScreen.colorizeCreatorButton(editTextB);
		
	}
	
	@Override
	protected void renderBorder(MatrixStack matrix, int mouseX, int mouseY) {
		//horizontal line top
		fill(matrix, this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + this.object.width, this.getStringPosY() + 1, Color.BLUE.getRGB());
		//horizontal line bottom
		fill(matrix, this.getStringPosX(), this.getStringPosY() + this.object.height, this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height + 1, Color.BLUE.getRGB());
		//vertical line left
		fill(matrix, this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + 1, this.getStringPosY() + this.object.height, Color.BLUE.getRGB());
		//vertical line right
		fill(matrix, this.getStringPosX() + this.object.width, this.getStringPosY(), this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height, Color.BLUE.getRGB());
	
		//Render pos and size values
		FontRenderer font = Minecraft.getInstance().fontRenderer;
		RenderUtils.setScale(matrix, 0.5F);
		font.drawString(matrix, Locals.localize("helper.creator.items.border.orientation")+ ": " + this.object.orientation, this.getStringPosX()*2, (this.getStringPosY()*2) - 35, Color.WHITE.getRGB());
		font.drawString(matrix, Locals.localize("helper.creator.items.string.border.scale") + ": " + this.getStringScale(), this.getStringPosX()*2, (this.getStringPosY()*2) - 26, Color.WHITE.getRGB());
		font.drawString(matrix, Locals.localize("helper.creator.items.border.posx") + ": " + this.getStringPosX(), this.getStringPosX()*2, (this.getStringPosY()*2) - 17, Color.WHITE.getRGB());
		font.drawString(matrix, Locals.localize("helper.creator.items.border.width") + ": " + this.object.width, this.getStringPosX()*2, (this.getStringPosY()*2) - 8, Color.WHITE.getRGB());
		
		font.drawString(matrix, Locals.localize("helper.creator.items.border.posy") + ": " + this.getStringPosY(), ((this.getStringPosX() + this.object.width)*2)+3, ((this.getStringPosY() + this.object.height)*2) - 14, Color.WHITE.getRGB());
		font.drawString(matrix, Locals.localize("helper.creator.items.border.height") + ": " + this.object.height, ((this.getStringPosX() + this.object.width)*2)+3, ((this.getStringPosY() + this.object.height)*2) - 5, Color.WHITE.getRGB());
		RenderUtils.postScale(matrix);
	}
	
	@Override
	protected void renderHighlightBorder(MatrixStack matrix) {
		Color c = new Color(0, 200, 255, 255);
		
		//horizontal line top
		AbstractGui.fill(matrix, this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + this.object.width, this.getStringPosY() + 1, c.getRGB());
		//horizontal line bottom
		AbstractGui.fill(matrix, this.getStringPosX(), this.getStringPosY() + this.object.height, this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height + 1, c.getRGB());
		//vertical line left
		AbstractGui.fill(matrix, this.getStringPosX(), this.getStringPosY(), this.getStringPosX() + 1, this.getStringPosY() + this.object.height, c.getRGB());
		//vertical line right
		AbstractGui.fill(matrix, this.getStringPosX() + this.object.width, this.getStringPosY(), this.getStringPosX() + this.object.width + 1, this.getStringPosY() + this.object.height, c.getRGB());
	}
	
	private int getStringPosX() {
		return (int)(this.object.getPosX(this.handler) * this.getStringScale());
	}
	
	private int getStringPosY() {
		return (int)(this.object.getPosY(this.handler) * this.getStringScale());
	}
	
	private float getStringScale() {
		return ((StringCustomizationItem)this.object).scale;
	}
	
	private boolean isStringCentered() {
		return ((StringCustomizationItem)this.object).centered;
	}
	
	private StringCustomizationItem getObject() {
		return ((StringCustomizationItem)this.object);
	}
	
	@Override
	public boolean isGrabberPressed() {
		return false;
	}
	
	@Override
	public int getActiveResizeGrabber() {
		return -1;
	}
	
	@Override
	protected void setOrientation(String pos) {
		super.setOrientation(pos);
		if (this.isStringCentered()) {
			if (this.object.orientation.endsWith("-right")) {
				this.object.posX += this.object.width;
			}
			if (this.object.orientation.endsWith("-centered")) {
				this.object.posX += this.object.width / 2;
			}
		}
	}
	
	public void setScale(float scale) {
		if (this.getObject().scale != scale) {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
		}
		
		((StringCustomizationItem)this.object).scale = scale;
		this.setWidth((int)(Minecraft.getInstance().fontRenderer.getStringWidth(this.object.value)*scale));
		this.setHeight((int)(7*scale));
	}
	
	public void setText(String text) {
		if (!this.getObject().value.equals(text)) {
			this.handler.history.saveSnapshot(this.handler.history.createSnapshot());
		}
		
		this.object.value = text;
		this.setScale(this.getStringScale());
	}
	
	private void setTextCallback(String text) {
		if (text == null) {
			this.handler.setMenusUseable(true);
			return;
		}
		if (text.length() > 0) {
			this.setText(StringUtils.convertFormatCodes(text, "&", "§"));
			this.handler.setMenusUseable(true);
		} else {
			this.handler.displayNotification(300, "§c§l" + Locals.localize("helper.creator.texttooshort.title"), "", Locals.localize("helper.creator.texttooshort.desc"), "", "", "", "");
		}
	}
	
	private void setScaleCallback(String scale) {
		if (scale == null) {
			this.handler.setMenusUseable(true);
			return;
		}
		if (MathUtils.isFloat(scale)) {
			this.setScale(Float.valueOf(scale));
			this.handler.setMenusUseable(true);
		} else {
			this.handler.displayNotification(300, "§c§l" + Locals.localize("helper.creator.items.string.scale.invalidvalue.title"), "", Locals.localize("helper.creator.items.string.scale.invalidvalue.desc"), "", "", "", "", "");
		}
	}
	
	@Override
	protected void updateHovered(int mouseX, int mouseY) {
		if ((mouseX >= this.getStringPosX()) && (mouseX <= this.getStringPosX() + this.object.width) && (mouseY >= this.getStringPosY()) && mouseY <= this.getStringPosY() + this.object.height) {
			this.hovered = true;
		} else {
			this.hovered = false;
		}
	}
	
	@Override
	public List<PropertiesSection> getProperties() {
		List<PropertiesSection> l = new ArrayList<PropertiesSection>();
		
		PropertiesSection p1 = new PropertiesSection("customization");
		p1.addEntry("action", "addtext");
		p1.addEntry("value", this.object.value);
		p1.addEntry("x", "" + this.object.posX);
		p1.addEntry("y", "" + this.object.posY);
		p1.addEntry("orientation", this.object.orientation);
		p1.addEntry("scale", "" + this.getObject().scale);
		p1.addEntry("shadow", "" + this.getObject().shadow);
		p1.addEntry("centered", "" + this.getObject().centered);
		l.add(p1);
		
		return l;
	}

}