package com.gigabytedx.gigenchantments;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;

import com.gigabytedx.gigenchantments.enchantments.AgroAOE;
import com.gigabytedx.gigenchantments.enchantments.BoostDamage;
import com.gigabytedx.gigenchantments.enchantments.DamageAOE;
import com.gigabytedx.gigenchantments.enchantments.HealAOE;
import com.gigabytedx.gigenchantments.enchantments.HealingSpell;
import com.gigabytedx.gigenchantments.enchantments.ResistanceSpell;
import com.gigabytedx.gigenchantments.enchantments.SpeedAOE;
import com.rit.sucy.EnchantPlugin;
import com.rit.sucy.EnchantmentAPI;

public class Main extends EnchantPlugin {
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();
		registerEvents();
		registerCommands();
		registerConfig();
		logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");
	}

	public void registerConfig() {
		saveDefaultConfig();
	}

	private void registerCommands() {
		// getCommand("command").setExecutor(new commandclass(this));
	}

	@Override
	public void registerEnchantments() {
		System.out.println("running");
		EnchantmentAPI.registerCustomEnchantment(new HealingSpell(this));
		EnchantmentAPI.registerCustomEnchantment(new BoostDamage(this));
		EnchantmentAPI.registerCustomEnchantment(new HealAOE(this));
		EnchantmentAPI.registerCustomEnchantment(new DamageAOE(this));
		EnchantmentAPI.registerCustomEnchantment(new ResistanceSpell(this));
		EnchantmentAPI.registerCustomEnchantment(new SpeedAOE(this));
		EnchantmentAPI.registerCustomEnchantment(new AgroAOE(this));
		
	}

	private void registerEvents() {
		@SuppressWarnings("unused")
		PluginManager pm = getServer().getPluginManager();
		// pm.registerEvents(new listenerclass(this), this);
	}

	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();

		logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
	}
}
