package com.gigabytedx.gigenchantments.constants;

public enum ConfigPaths {
	COOLDOWN_PATH("cooldown"),
	NAME_PATH("name"),
	DURATION_PATH("duration"),
	RANGE_PATH("range"),
	MAX_LEVEL_PATH("max level"),
	HEALING_AOE_PATH("healing aoe"),
	DAMAGE_AOE_PATH("damage aoe"),
	HEAL_SPELL_PATH("healing spell"),
	SPEED_AOE_PATH("speed aoe"),
	STRENTH_SPELL_PATH("strength spell"),
	BONUS_SPELL_PATH("strength spell"),
	AGRO_AOE_PATH("agro aoe"),
	RESISTANCE_SPELL_PATH("protection spell");
	
private final String path;
	
	ConfigPaths(String path){
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
