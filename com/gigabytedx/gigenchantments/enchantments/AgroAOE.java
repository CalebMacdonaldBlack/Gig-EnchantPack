package com.gigabytedx.gigenchantments.enchantments;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gigabytedx.gigenchantments.Main;
import com.gigabytedx.gigenchantments.constants.ConfigPaths;
import com.gigabytedx.gigenchantments.constants.ErrorsAndMessages;
import com.rit.sucy.CustomEnchantment;

public class AgroAOE extends CustomEnchantment {
	static final Material[] AGRO_AOE_ITEMS = new Material[] { Material.MAGMA_CREAM };
	static HashMap<UUID, Long> playerCooldown = new HashMap<>();
	private int maxLevel;
	private int cooldown;
	private int range;

	public AgroAOE(Main plugin) {
		super(plugin.getConfig().getString(ConfigPaths.AGRO_AOE_PATH.getPath() + "." + ConfigPaths.NAME_PATH.getPath()), AGRO_AOE_ITEMS,
				2);

		this.maxLevel = plugin.getConfig().getInt(ConfigPaths.AGRO_AOE_PATH.getPath() + "." + ConfigPaths.MAX_LEVEL_PATH.getPath());
		this.cooldown = plugin.getConfig().getInt(ConfigPaths.AGRO_AOE_PATH.getPath() + "." + ConfigPaths.COOLDOWN_PATH.getPath());
		this.range = plugin.getConfig().getInt(ConfigPaths.AGRO_AOE_PATH.getPath() + "." + ConfigPaths.RANGE_PATH.getPath());
		this.max = maxLevel;
		this.base = 1;
	}

	@Override
	public void applyMiscEffect(Player player, int enchantLevel, PlayerInteractEvent event) {
		if (enchantLevel > maxLevel) {
			enchantLevel = maxLevel;
		}
		if (isOnCooldown(player, enchantLevel)) {
			player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 50);
			for (Entity entity : player.getNearbyEntities(range, range, range)) {
				if (entity instanceof Creature) {
					Creature creature = (Creature) entity;
					
					creature.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, creature.getLocation().getX(),
							creature.getLocation().getY(), creature.getLocation().getZ(), 10, 0.25, 1, 0.25);
					creature.getWorld().playSound(creature.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 50);
					creature.getWorld().playSound(creature.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 50);
					
					creature.setTarget(player);
					
				}
			}
		}
	}

	private boolean isOnCooldown(Player player, int enchantLevel) {
		if (playerCooldown.containsKey(player.getUniqueId())) {
			if (System.currentTimeMillis() < playerCooldown.get(player.getUniqueId())) {
				// user is still on cool down
				int secondsRemaining = (int) (playerCooldown.get(player.getUniqueId()) - System.currentTimeMillis())
						/ 1000;
				String response = ErrorsAndMessages.ON_COOLDOWN.getErrorMessage().replace("&S", secondsRemaining + "");
				player.sendMessage(response);
				return false;
			}
		}
		// user is not on cool down
		long cooldownTime = (cooldown - (enchantLevel * 5)) * 1000;
		playerCooldown.put(player.getUniqueId(), System.currentTimeMillis() + cooldownTime);

		return true;
	}
}
