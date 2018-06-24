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
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;

@Slf4j
public class CorporealBeastCoreOverlay extends Overlay
{
	private final CorporealBeastPlugin plugin;
	private final CorporealBeastConfig config;
	private final Client client;

	@Inject
	public CorporealBeastCoreOverlay(CorporealBeastPlugin plugin, CorporealBeastConfig config, Client client)
	{
		setPosition(OverlayPosition.DYNAMIC);
		setLayer(OverlayLayer.ABOVE_SCENE);
		this.plugin = plugin;
		this.config = config;
		this.client = client;
	}

	@Override
	public Dimension render(Graphics2D graphics)
	{
		if (config.showCoreOverlay() && plugin.getDamageWidget() != null)
		{
			if (plugin.getDarkCoreProjectileTargetLocation() != null)
			{
				OverlayUtil.renderPolygonBorderFill(graphics, Perspective.getCanvasTilePoly(client, plugin.getDarkCoreProjectileTargetLocation()), config.coreOverlayColor(), 25);
			}
			if (plugin.getDarkCore() != null)
			{
				OverlayUtil.renderPolygonBorderFill(graphics, Perspective.getCanvasTileAreaPoly(client, plugin.getDarkCore().getLocalLocation(), 3), config.coreOverlayColor(), 25);
			}
		}
		/*Player player = client.getLocalPlayer();
		WorldPoint corpWaitingRoom = new WorldPoint(2968, 4384, 2);
		if (corpWaitingRoom != null)
		{
			WorldArea portal = new WorldArea(corpWaitingRoom, 3);
			LocalPoint corpWaitingRoomLocal = LocalPoint.fromWorld(client, corpWaitingRoom);
			OverlayUtil.renderPolygonBorderFill(graphics, Perspective.getCanvasTilePoly(client, corpWaitingRoomLocal), Color.RED, Color.RED);
			List<WorldPoint> portalAreaList = portal.toWorldPoints();
			portalAreaList.forEach(worldPoint -> OverlayUtil.renderPolygonBorderFill(graphics, Perspective.getCanvasTilePoly(client, LocalPoint.fromWorld(client, worldPoint)), Color.GREEN));
			//log.debug("Distance: {}", client.getLocalPlayer().getWorldLocation().distanceTo(corpWaitingRoom));
			//log.debug("{}", portal.toWorldPoints());
			//log.debug("{}", player.getWorldLocation());
			//log.debug("{}", portal.toWorldPoints().contains(player.getWorldLocation()));
			List<Player> found = client.getPlayers().stream().filter(portal::contains).collect(Collectors.toList());
			//log.debug("{}", found.stream().map(Player::getName).collect(Collectors.toList()));
			found.forEach(x -> OverlayUtil.renderPolygonBorderFill(graphics, x.getCanvasTilePoly(), Color.BLUE));
		}
		return null;*/
		return null;
	}
}