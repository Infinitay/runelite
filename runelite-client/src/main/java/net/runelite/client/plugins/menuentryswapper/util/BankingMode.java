package net.runelite.client.plugins.menuentryswapper.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BankingMode
{
	ONE("Withdraw/Deposit One", "withdrawOne"),
	FIVE("Withdraw/Deposit Five", "withdrawFive"),
	TEN("Withdraw/Deposit Ten", "withdrawTen"),
	X("Withdraw/Deposit X", "withdrawX"),
	ALL("Withdraw/Deposit Five", "withdrawAll");

	private final String mode;
	private final String hiddenKeyName;
}
