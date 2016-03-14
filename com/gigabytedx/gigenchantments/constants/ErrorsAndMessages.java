package com.gigabytedx.gigenchantments.constants;

import org.bukkit.ChatColor;

public enum ErrorsAndMessages {
	ON_COOLDOWN(ChatColor.DARK_RED + "You have " + ChatColor.GOLD + "&S" + ChatColor.DARK_RED +  " seconds until you can use this again!"),
	INTERNAL_ERROR(ChatColor.RED + "An internal error has occurred! Please notify an administrator.");
	
	private final String errorMessage;
	
	ErrorsAndMessages(String errorMessage){
		this.errorMessage = errorMessage;
	}

	public String getErrorMessage() {
		return errorMessage;
	}
}
