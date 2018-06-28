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

package net.runelite.client.plugins.chatfilter;

import com.google.common.collect.Sets;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Provides;
import java.util.Set;
import java.util.regex.Pattern;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.events.SetMessage;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
	name = "Chat Filter"
)
public class ChatFilterPlugin extends Plugin
{
	@Inject
	private ChatFilterConfig config;

	private static final String RWT_REGEX = "(rsmall)|(gold)|(gp.)|(coin.)|(money)|(discount)|(acc.)|(sell)|(07)|(USD)|(C(O|0|\\(\\))M)|(runechat)";
	private static final String SCAMMING_REGEX = "(double)|(doubling)|(2x)|(gold)|(gp.)|(coin.)|(money)|(max)|(up to)|(\\d+(k|m))|(quitting)|(search)|(youtube)|(legit)|(trade)";
	private static final String BEGGING_REGEX = "(gold)|(gp.)|(coin.)|(money)|(can i)|(give me)|(someone)|(please)|(have)|(trade)|(\\d+(k|m))|(need)|(free)|(money)|(dancing)";

	private static Pattern RWT_PATTERN;
	private static Pattern SCAMMING_PATTERN;
	private static Pattern BEGGING_PATTERN;

	private Set<String> filterActors = Sets.newHashSet();

	@Provides
	ChatFilterConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(ChatFilterConfig.class);
	}

	@Override
	protected void startUp() throws Exception
	{
		RWT_PATTERN = Pattern.compile(RWT_REGEX, Pattern.CASE_INSENSITIVE);
		SCAMMING_PATTERN = Pattern.compile(SCAMMING_REGEX, Pattern.CASE_INSENSITIVE);
		BEGGING_PATTERN = Pattern.compile(BEGGING_REGEX, Pattern.CASE_INSENSITIVE);
	}

	@Subscribe
	private void onConfigChanged(ConfigChanged configChanged)
	{
		if (configChanged.getKey().equals("chatfilter"))
		{
			if (config.rwtFilter())
			{
				RWT_PATTERN = Pattern.compile(RWT_REGEX, Pattern.CASE_INSENSITIVE);

			}
			if (config.scamFilter())
			{
				SCAMMING_PATTERN = Pattern.compile(SCAMMING_REGEX, Pattern.CASE_INSENSITIVE);

			}
			if (config.begFilter())
			{
				BEGGING_PATTERN = Pattern.compile(BEGGING_REGEX, Pattern.CASE_INSENSITIVE);
			}
		}
	}

	// This event fires before SetMessage event
	@Subscribe
	private void onOverheadChanged(OverheadTextChanged overheadChanged)
	{
		if (overheadChanged.getActor().getName() != null && overheadChanged.getActor().getOverhead() != null)
		{
			if (overheadChanged.getActor().getOverhead().equalsIgnoreCase("Hello"))
			{
				filterActors.add(overheadChanged.getActor().getName());
				overheadChanged.getActor().setOverhead("");
				return;
			}

			if (config.rwtFilter())
			{
				if (shouldFilterMessage(RWT_PATTERN, overheadChanged.getActor().getOverhead()))
				{
					filterActors.add(overheadChanged.getActor().getName());
					overheadChanged.getActor().setOverhead("");
					return;
				}
			}
			if (config.scamFilter())
			{
				if (shouldFilterMessage(SCAMMING_PATTERN, overheadChanged.getActor().getOverhead()))
				{
					filterActors.add(overheadChanged.getActor().getName());
					overheadChanged.getActor().setOverhead("");
					return;
				}
			}
			if (config.begFilter())
			{
				if (shouldFilterMessage(BEGGING_PATTERN, overheadChanged.getActor().getOverhead()))
				{
					filterActors.add(overheadChanged.getActor().getName());
					overheadChanged.getActor().setOverhead("");
					return;
				}
			}
		}
	}

	@Subscribe
	private void onSetMessage(SetMessage message)
	{
		if (filterActors.contains(message.getName()))
		{
			message.getMessageNode().setType(ChatMessageType.FILTERED);
			filterActors.remove(message.getName());
		}
	}

	private boolean shouldFilterMessage(Pattern pattern, String message)
	{
		int matchesFound = 0;
		while (pattern.matcher(message.replaceAll("\\W", "")).find())
		{
			matchesFound++;
			if (matchesFound >= 3)
			{
				return true;
			}
		}
		return false;
	}
}