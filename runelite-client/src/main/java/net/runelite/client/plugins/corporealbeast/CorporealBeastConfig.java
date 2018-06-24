/*
 * Copyright (c) 2018, Infinitay <https://github.com/Infinitay>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.runelite.client.plugins.corporealbeast;

import java.awt.Color;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(
	keyName = "corporealbeast",
	name = "CorporealBeast Plugin Name",
	description = "CorporealBeast Plugin Description"
)
public interface CorporealBeastConfig extends Config
{
	@ConfigItem(
		keyName = "showDealtPercent",
		name = "Show Damage Dealt %",
		description = "Shows your damage dealt as a percentage",
		position = 0
	)
	default boolean showDealtPercent()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showAveragePercent",
		name = "Show Estimated Avg Dmg/Player",
		description = "Shows the ESTIMATED average damage per player (including you) as a percent",
		position = 1
	)
	default boolean showAveragepercent()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCoreTarget",
		name = "Show When Dark Core Targets You",
		description = "Shows a little text overlay when the Dark Core targets you",
		position = 2
	)
	default boolean showCoreTarget()
	{
		return true;
	}

	@ConfigItem(
		keyName = "showCoreOverlay",
		name = "Show Dark Core Overlay",
		description = "Shows the Dark Core's attack radius",
		position = 3
	)
	default boolean showCoreOverlay()
	{
		return true;
	}

	@ConfigItem(
		keyName = "coreOverlayColor",
		name = "Dark Core Overlay Color",
		description = "Configure the color for the Dark Core overlay",
		position = 3
	)
	default Color coreOverlayColor()
	{
		return Color.RED;
	}
}