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

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.text.DecimalFormat;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;
import net.runelite.client.util.StackFormatter;

@Slf4j
public class CorporealBeastPanelOverlay extends Overlay
{
	private final CorporealBeastPlugin plugin;
	private final CorporealBeastConfig config;
	private final Client client;
	private final PanelComponent panelComponent = new PanelComponent();
	private static final DecimalFormat PERCENT_FORMAT = new DecimalFormat("##.##%");

	@Inject
	public CorporealBeastPanelOverlay(CorporealBeastPlugin plugin, CorporealBeastConfig config, Client client)
	{
		setPosition(OverlayPosition.TOP_LEFT);
		setPriority(OverlayPriority.MED);
		this.plugin = plugin;
		this.config = config;
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		panelComponent.getChildren().clear();
		Widget damageWidget = client.getWidget(WidgetInfo.CORPOREAL_BEAST_DAMAGE);
		long damageDone = plugin.getDamageDone();
		NPC core = plugin.getDarkCore();

		if (damageWidget != null)
		{
			damageWidget.setHidden(true);
			panelComponent.getChildren().add(LineComponent.builder().left("Damage Dealt").right(StackFormatter.formatNumber(damageDone)).build());

			if (plugin.getTotalDamageTakenByCorp() != 0)
			{
				if (config.showDealtPercent())
				{
					double damageDonePercent = (double) damageDone / plugin.getTotalDamageTakenByCorp();
					panelComponent.getChildren().add(LineComponent.builder().left("Damage Dealt").right(PERCENT_FORMAT.format(damageDonePercent)).build());
				}
				if (config.showAveragepercent() && plugin.getPlayersAtCorp() > 0)
				{
					System.out.println(plugin.getPlayersAtCorp());
					long recommendedDamage = plugin.getTotalDamageTakenByCorp() / plugin.getPlayersAtCorp();
					//panelComponent.getChildren().add(LineComponent.builder().left("Recommended").right(PERCENT_FORMAT.format(recommendedDamage / plugin.getTotalDamageTakenByCorp())).build());
					panelComponent.getChildren().add(LineComponent.builder().left("Recommended").right(StackFormatter.formatNumber(recommendedDamage)).build());
				}
			}

			if (config.showCoreTarget())
			{
				//plugin.getDarkCoreTargets().forEach(playerName -> panelComponent.getChildren().add(TitleComponent.builder().text(playerName).build()));
				if (!plugin.getTarget().equals(""))
				{
					panelComponent.getChildren().add(TitleComponent.builder().text("Dark Core Target").build());
					panelComponent.getChildren().add(TitleComponent.builder().text(plugin.getTarget()).build());
				}
			}
		}
		return panelComponent.render(graphics);
	}
}