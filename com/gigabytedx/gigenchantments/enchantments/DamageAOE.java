package com.gigabytedx.gigenchantments.enchantments;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import com.gigabytedx.gigenchantments.Main;
import com.gigabytedx.gigenchantments.constants.ConfigPaths;
import com.gigabytedx.gigenchantments.constants.ErrorsAndMessages;
import com.rit.sucy.CustomEnchantment;

public class DamageAOE extends CustomEnchantment {
	static final Material[] DAMAGE_AOE_ITEMS = new Material[] { Material.MAGMA_CREAM };
	static HashMap<UUID, Long> playerCooldown = new HashMap<>();
	private int maxLevel;
	private int cooldown;
	private int range;

	public DamageAOE(Main plugin) {
		super(plugin.getConfig().getString(ConfigPaths.DAMAGE_AOE_PATH.getPath() + "." + ConfigPaths.NAME_PATH.getPath()), DAMAGE_AOE_ITEMS,
				2);

		this.maxLevel = plugin.getConfig().getInt(ConfigPaths.DAMAGE_AOE_PATH.getPath() + "." + ConfigPaths.MAX_LEVEL_PATH.getPath());
		this.cooldown = plugin.getConfig().getInt(ConfigPaths.DAMAGE_AOE_PATH.getPath() + "." + ConfigPaths.COOLDOWN_PATH.getPath());
		this.range = plugin.getConfig().getInt(ConfigPaths.DAMAGE_AOE_PATH.getPath() + "." + ConfigPaths.RANGE_PATH.getPath());
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
				if (entity instanceof LivingEntity && !(entity instanceof Player)) {
					LivingEntity livingEntity = (LivingEntity) entity;
					livingEntity.damage(4 * enchantLevel, player);
					livingEntity.getWorld().spawnParticle(Particle.DAMAGE_INDICATOR, livingEntity.getLocation().getX(),
							livingEntity.getLocation().getY(), livingEntity.getLocation().getZ(), 2 * enchantLevel,
							0.25, 1, 0.25);
					livingEntity.getWorld().spawnParticle(Particle.CRIT, livingEntity.getLocation().getX(),
							livingEntity.getLocation().getY(), livingEntity.getLocation().getZ(), 20, 0.25, 1, 0.25);
					livingEntity.getWorld().playSound(player.getLocation(), Sound.ENTITY_HOSTILE_HURT, 100, 50);
					livingEntity.getWorld().playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 100, 50);
					livingEntity.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 50);
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
