package com.sebastian.levoria.mixin.client;

import com.sebastian.levoria.Levoria;
import com.sebastian.levoria.client_cfg.ClientConfig;
import com.sebastian.levoria.util.hide_experimental_warning.HideExperimentalScreenWidget;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.navigation.GuiNavigationPath;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.ExperimentalWarningScreen;
import net.minecraft.client.gui.screen.world.BackupPromptScreen;
import net.minecraft.client.gui.widget.Widget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Mixin(BackupPromptScreen.class)
public abstract class ExperimentalWarningsScreenMixin {

	@Shadow @Final protected BackupPromptScreen.Callback callback;

	@Inject(at = @At("HEAD"), method = "init")
	public void init(CallbackInfo ci) {
		if(ClientConfig.INSTANCE.isHideExperimentalWarning()) {
			callback.proceed(false, false);
		}
	}

	//@Shadow @Final protected BackupPromptScreen.Callback callback;
	//	private static Method addRenderableWidgetMethod;
	//
	//	static {
	//		try {
	//			addRenderableWidgetMethod = Screen.class.getDeclaredMethod("addDrawableChild", Widget.class);
	//			addRenderableWidgetMethod.setAccessible(true);
	//		} catch (NoSuchMethodException e) {
	//			Levoria.LOGGER.error(e.getLocalizedMessage());
	//		}
	//	}
	//
	//	private static Field widthField;
	//	private static Field heightField;
	//
	//	static {
	//		try {
	//			widthField = Screen.class.getDeclaredField("width");
	//			widthField.setAccessible(true);
	//
	//			heightField = Screen.class.getDeclaredField("height");
	//			heightField.setAccessible(true);
	//		} catch (NoSuchFieldException e) {
	//			Levoria.LOGGER.warn(e.getLocalizedMessage());
	//		}
	//	}
	//
	//	@Inject(at = @At("HEAD"), method = "init")
	//	private void init(CallbackInfo ci) {
	//		try {
	//			int width = (int) widthField.get(MinecraftClient.getInstance().currentScreen);
	//			System.out.println("Screen width: " + width);
	//
	//			// Lese den Wert des privaten Feldes 'height'
	//			int height = (int) heightField.get(MinecraftClient.getInstance().currentScreen);
	//			System.out.println("Screen height: " + height);
	//			addRenderableWidgetMethod.invoke(this, new HideExperimentalScreenWidget(width / 2 - 100, height - 70, width, ClientConfig.INSTANCE.getHideExperimentalWarningDelay(), (w) -> {
	//				callback.proceed(false, false);
	//			}));
	//		} catch (Exception e) {
	//			Levoria.LOGGER.error(e.getLocalizedMessage());
	//		}
	//	}
}