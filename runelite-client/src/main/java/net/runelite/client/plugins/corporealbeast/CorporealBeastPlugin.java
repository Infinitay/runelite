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

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import java.util.Arrays;
import java.util.List;
import javax.inject.Inject;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Hitsplat;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
import net.runelite.api.Player;
import net.runelite.api.Projectile;
import net.runelite.api.ProjectileID;
import net.runelite.api.Varbits;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.events.SetMessage;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.events.WidgetLoaded;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.chat.ChatColorType;
import net.runelite.client.chat.ChatMessageBuilder;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@Slf4j
@PluginDescriptor(
	name = "Corporeal Beast"
)
public class CorporealBeastPlugin extends Plugin
{
	@Inject
	private CorporealBeastConfig config;

	@Inject
	private CorporealBeastCoreOverlay coreOverlay;

	@Inject
	private CorporealBeastPanelOverlay panelOverlay;

	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	ChatMessageManager chatMessageManager;

	private static final String KC_STRING = "Your Corporeal Beast kill count is:";

	@Getter
	private Widget damageWidget;

	@Getter
	private NPC darkCore;

	@Getter
	private LocalPoint darkCoreProjectileTargetLocation;

	@Getter
	private List<String> darkCoreTargets; // remove

	@Getter
	private int damageDone;

	@Getter
	private long totalDamageTakenByCorp;

	@Getter
	private String target;

	@Getter
	private int playersAtCorp;

	@Getter
	private int prevDamageDone;

	@Provides
	CorporealBeastConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CorporealBeastConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(coreOverlay);
		overlayManager.add(panelOverlay);
		darkCoreTargets = Lists.newArrayList();
	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(coreOverlay);
		overlayManager.remove(panelOverlay);
		if (damageWidget != null)
		{
			damageWidget.setHidden(false);
		}
		darkCore = null;
		damageWidget = null;
		prevDamageDone = 0;
		damageDone = 0;
		playersAtCorp = 0;
		darkCoreTargets.clear();
	}

	@Subscribe
	private void onVarbitChanged(VarbitChanged event)
	{
		int damage = client.getVar(Varbits.CORPOREAL_BEAST_DAMAGE_DONE);
		if (damageDone != damage)
		{
			prevDamageDone = damageDone;
			damageDone = damage;
		}
	}

	@Subscribe
	private void onWidgetLoaded(WidgetLoaded event)
	{
		if (event.getGroupId() == WidgetID.CORPOREAL_BEAST_DAMAGE_GROUP_ID)
		{
			log.debug("Damage widget has loaded");
			damageWidget = client.getWidget(WidgetInfo.CORPOREAL_BEAST_DAMAGE);
			damageDone = 0;
		}
	}

	@Subscribe
	private void onNPCSpawned(NpcSpawned event)
	{
		NPC npc = event.getNpc();
		if (npc != null && npc.getId() == NpcID.DARK_ENERGY_CORE)
		{
			log.debug("Dark Core has spawned");
			darkCore = npc;
			darkCoreProjectileTargetLocation = null;
		}
		else if (npc != null && npc.getId() == NpcID.CORPOREAL_BEAST)
		{
			log.debug("Corporeal Beast has spawned");
			// when it first spawns, ::getHealth returns -1
			totalDamageTakenByCorp = npc.getHealth() == -1 ? 0 : Math.abs(npc.getHealth() - 2000); // in case we walk into the room and corp has already been damaged
		}
	}

	@Subscribe
	private void onNPCDespawned(NpcDespawned event)
	{
		NPC npc = event.getNpc();
		if (npc != null && npc.isDead())
		{
			if (npc.getId() == NpcID.DARK_ENERGY_CORE)
			{
				log.debug("Dark Core is dead or despawned (changing targets)");
				darkCore = null;
				darkCoreProjectileTargetLocation = null;
			}
			if (npc.getId() == NpcID.CORPOREAL_BEAST)
			{
				log.debug("Corporeal Beast is dead");
				//darkCoreTargets.clear();
				darkCore = null;
				damageDone = 0;
				totalDamageTakenByCorp = 0;
			}
		}
	}

	@Subscribe
	private void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();
		LocalPoint targetLocalPoint = event.getPosition();

		if (projectile.getId() == ProjectileID.CORPOREAL_BEAST_DARK_CORE_AOE)
		{
			log.debug("Dark core projectile found");
			darkCoreProjectileTargetLocation = targetLocalPoint;
			darkCore = null;
		}
	}

	private Player[] testing;

	@Subscribe
	private void onGameTick(GameTick event)
	{
		//if (!client.getCachedPlayers().equals(testing)) {
			testing = client.getCachedPlayers();
			log.debug("Cached Players Changed? {} |  {}", Arrays.asList(client.getCachedPlayers()));
		//}
		if (darkCoreProjectileTargetLocation != null)
		{
			if (client.getLocalPlayer().getWorldLocation().distanceTo(WorldPoint.fromLocal(client, darkCoreProjectileTargetLocation)) < 2)
			{
				target = client.getLocalPlayer().getName();
			}
		}
		else if (darkCore != null)
		{
			if (client.getLocalPlayer().getWorldLocation().distanceTo(darkCore.getWorldLocation()) < 2)
			{
				target = client.getLocalPlayer().getName();
			}
		}
		target = "";
		/*darkCoreTargets.clear();
		if (darkCore != null)
		{
			client.getPlayers().stream()
				.filter(player -> player.getWorldLocation().distanceTo(darkCore.getWorldLocation()) < 2)
				.map(Player::getName).collect(Collectors.toCollection(() -> darkCoreTargets));
		}
		if (darkCoreProjectileTargetLocation != null)
		{
			client.getPlayers().stream()
				.filter(player -> player.getWorldLocation().distanceTo(WorldPoint.fromLocal(client, darkCoreProjectileTargetLocation)) < 2)
				.map(Player::getName).collect(Collectors.toCollection(() -> darkCoreTargets));
		}
		if (darkCoreTargets.isEmpty())
		{
			darkCoreTargets.add("None");
		}*/


	}

	@Subscribe
	private void onGameStateChanged(GameStateChanged event)
	{
		log.debug("Game state changed to {}", event.getGameState());
		darkCore = null;
		damageWidget = null;
		damageDone = 0;
		totalDamageTakenByCorp = 0;
		darkCoreProjectileTargetLocation = null;
		darkCoreTargets.clear();
	}

	@Subscribe
	private void onSetMessage(SetMessage event)
	{
		if (event.getType().equals(ChatMessageType.SERVER))
		{
			String originalMessage = event.getValue();
			if (originalMessage.contains(KC_STRING))
			{
				event.getMessageNode().setValue(new ChatMessageBuilder().append(originalMessage)
					.append(ChatColorType.NORMAL).append(" You've dealt ")
					.append(ChatColorType.HIGHLIGHT).append(String.valueOf(prevDamageDone))
					.append(ChatColorType.NORMAL).append(" damage.").build());
			}
		}
	}

	@Subscribe
	private void onHitSplat(HitsplatApplied event)
	{
		if (event.getActor().getName().equals("Corporeal Beast") && event.getHitsplat().getHitsplatType().equals(Hitsplat.HitsplatType.DAMAGE))
		{
			totalDamageTakenByCorp = totalDamageTakenByCorp + event.getHitsplat().getAmount();
			playersAtCorp = client.getPlayers().size();
		}
	}
}