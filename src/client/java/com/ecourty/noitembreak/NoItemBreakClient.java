package com.ecourty.noitembreak;

import com.ecourty.noitembreak.command.NoItemBreakCommand;
import com.ecourty.noitembreak.config.ConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class NoItemBreakClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ConfigManager.load();
		NoItemBreakCommand.register();
	}
}