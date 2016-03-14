package com.gigabytedx.gigenchantments.enchantments;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BlockIterator;

import com.gigabytedx.gigenchantments.Main;
import com.gigabytedx.gigenchantments.constants.ConfigPaths;
import com.gigabytedx.gigenchantments.constants.ErrorsAndMessages;
import com.rit.sucy.CustomEnchantment;

public class HealingSpell extends CustomEnchantment {
	static final Material[] HEAL_SPELL_ITEMS = new Material[] { Material.RED_ROSE };
	static HashMap<UUID, Long> playerCooldown = new HashMap<>();
	@SuppressWarnings("unused")
	private Main plugin;
	private int maxLevel;
	private int cooldown;
	private int duration;

	public HealingSpell(Main plugin) {
		
		super(plugin.getConfig().getString(ConfigPaths.HEAL_SPELL_PATH.getPath() + "." + ConfigPaths.NAME_PATH.getPath()), HEAL_SPELL_ITEMS,
				2);

		this.maxLevel = plugin.getConfig().getInt(ConfigPaths.HEAL_SPELL_PATH.getPath() + "." + ConfigPaths.MAX_LEVEL_PATH.getPath());
		this.cooldown = plugin.getConfig().getInt(ConfigPaths.HEAL_SPELL_PATH.getPath() + "." + ConfigPaths.COOLDOWN_PATH.getPath());
		this.duration = plugin.getConfig().getInt(ConfigPaths.HEAL_SPELL_PATH.getPath() + "." + ConfigPaths.DURATION_PATH.getPath());
		
		this.plugin = plugin;
		this.max = maxLevel;
		this.base = 1;
	}

	@Override
	public void applyMiscEffect(Player player, int enchantLevel, PlayerInteractEvent event) {
		System.out.println(maxLevel + " " + cooldown + " " + duration);
		if (enchantLevel > maxLevel) {
			enchantLevel = maxLevel;
		}
		if (getTarget(player) instanceof Player) {
			if (isOnCooldown(player, enchantLevel)) {
				player.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 50);
				Player playerClicked = (Player) getTarget(player);
				playerClicked.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, enchantLevel - 1),
						true);
				playerClicked.getWorld().spawnParticle(Particle.HEART, playerClicked.getLocation().getX(),
						playerClicked.getLocation().getY(), playerClicked.getLocation().getZ(), 20, 0.25, 1, 0.25);
				playerClicked.getWorld().playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_TOUCH, 100, 50);
				playerClicked.getWorld().playSound(player.getLocation(), Sound.BLOCK_ANVIL_BREAK, 100, 50);
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

	public static Entity getTarget(final Player player) {

		BlockIterator iterator = new BlockIterator(player.getWorld(), player.getLocation().toVector(),
				player.getEyeLocation().getDirection(), 0, 20);
		Entity target = null;
		while (iterator.hasNext()) {
			Block item = iterator.next();
			for (Entity entity : player.getNearbyEntities(100, 100, 100)) {
				int acc = 2;
				for (int x = -acc; x < acc; x++)
					for (int z = -acc; z < acc; z++)
						for (int y = -acc; y < acc; y++)
							if (entity.getLocation().getBlock().getRelative(x, y, z).equals(item)) {
								return target = entity;
							}
			}
		}
		return target;
	}
}
