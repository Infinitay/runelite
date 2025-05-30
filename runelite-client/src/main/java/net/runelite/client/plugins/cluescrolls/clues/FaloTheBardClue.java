/*
 * Copyright (c) 2019, Twiglet1022 <https://github.com/Twiglet1022>
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
package net.runelite.client.plugins.cluescrolls.clues;

import com.google.common.collect.ImmutableList;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.List;
import javax.annotation.Nonnull;
import lombok.Getter;
import net.runelite.api.Item;
import net.runelite.api.NPC;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.gameval.ItemID;
import static net.runelite.client.plugins.cluescrolls.ClueScrollOverlay.TITLED_CONTENT_COLOR;
import net.runelite.client.plugins.cluescrolls.ClueScrollPlugin;
import static net.runelite.client.plugins.cluescrolls.ClueScrollWorldOverlay.IMAGE_Z_OFFSET;
import net.runelite.client.plugins.cluescrolls.clues.item.AnyRequirementCollection;
import net.runelite.client.plugins.cluescrolls.clues.item.ItemRequirement;
import net.runelite.client.plugins.cluescrolls.clues.item.RangeItemRequirement;
import net.runelite.client.plugins.cluescrolls.clues.item.SingleItemRequirement;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;
import net.runelite.client.ui.overlay.components.PanelComponent;
import net.runelite.client.ui.overlay.components.TitleComponent;

@Getter
public class FaloTheBardClue extends ClueScroll implements NpcClueScroll
{
	static final List<FaloTheBardClue> CLUES = ImmutableList.of(
		new FaloTheBardClue("A blood red weapon, a strong curved sword, found on the island of primate lords.", any("Dragon scimitar", item(ItemID.DRAGON_SCIMITAR), item(ItemID.DRAGON_SCIMITAR_ORNAMENT))),
		new FaloTheBardClue("A book that preaches of some great figure, lending strength, might and vigour.", any("Any completed god book", item(ItemID.SARADOMINBOOK_COMPLETE), item(ItemID.GUTHIXBOOK_COMPLETE), item(ItemID.ZAMORAKBOOK_COMPLETE), item(ItemID.ARMADYLBOOK_COMPLETE), item(ItemID.BANDOSBOOK_COMPLETE), item(ItemID.ZAROSBOOK_COMPLETE), item(ItemID.LEAGUE_3_BOOK_SARADOMIN), item(ItemID.LEAGUE_3_BOOK_GUTHIX), item(ItemID.LEAGUE_3_BOOK_ZAMORAK), item(ItemID.LEAGUE_3_BOOK_ARMADYL), item(ItemID.LEAGUE_3_BOOK_BANDOS), item(ItemID.LEAGUE_3_BOOK_ZAROS))),
		new FaloTheBardClue("A bow of elven craft was made, it shimmers bright, but will soon fade.", any("Crystal Bow", EmoteClue.ACTIVE_CRYSTAL_BOW_OR_BOW_OF_FAERDHINEN)),
		new FaloTheBardClue("A fiery axe of great inferno, when you use it, you'll wonder where the logs go.", any("Infernal axe", item(ItemID.INFERNAL_AXE), item(ItemID.INFERNAL_AXE_EMPTY), item(ItemID.TRAILBLAZER_AXE), item(ItemID.TRAILBLAZER_AXE_EMPTY), item(ItemID.TRAILBLAZER_RELOADED_AXE), item(ItemID.TRAILBLAZER_RELOADED_AXE_EMPTY))),
		new FaloTheBardClue("A mark used to increase one's grace, found atop a seer's place.", item(ItemID.GRACE)),
		new FaloTheBardClue("A molten beast with fiery breath, you acquire these with its death.", item(ItemID.LAVA_DRAGON_BONES)),
		new FaloTheBardClue("A shiny helmet of flight, to obtain this with melee, struggle you might.", item(ItemID.ARMADYL_HELMET)),
		new FaloTheBardClue("A sword held in the other hand, red its colour, Cyclops strength you must withstand.", any("Dragon or Avernic Defender", item(ItemID.DRAGON_PARRYINGDAGGER), item(ItemID.DRAGON_PARRYINGDAGGER_T), item(ItemID.DRAGON_PARRYINGDAGGER_TROUVER), item(ItemID.DRAGON_PARRYINGDAGGER_T_TROUVER), item(ItemID.INFERNAL_DEFENDER), item(ItemID.INFERNAL_DEFENDER_TROUVER), item(ItemID.INFERNAL_DEFENDER_GHOMMAL_5), item(ItemID.INFERNAL_DEFENDER_GHOMMAL_5_TROUVER), item(ItemID.INFERNAL_DEFENDER_GHOMMAL_6), item(ItemID.INFERNAL_DEFENDER_GHOMMAL_6_TROUVER))),
		new FaloTheBardClue("A token used to kill mythical beasts, in hopes of a blade or just for an xp feast.", item(ItemID.WARGUILD_TOKENS)),
		new FaloTheBardClue("Green is my favourite, mature ale I do love, this takes your herblore above.", item(ItemID.MATURE_GREENMANS_ALE)),
		new FaloTheBardClue("It can hold down a boat or crush a goat, this object, you see, is quite heavy.", any("Barrelchest anchor", item(ItemID.BRAIN_ANCHOR), item(ItemID.BH_BRAIN_ANCHOR_IMBUE))),
		new FaloTheBardClue("It comes from the ground, underneath the snowy plain. Trolls aplenty, with what looks like a mane.", item(ItemID.BASALT)),
		new FaloTheBardClue("No attack to wield, only strength is required, made of obsidian, but with no room for a shield.", any("Tzhaar-ket-om", item(ItemID.TZHAAR_MAUL), item(ItemID.TZHAAR_MAUL_T))),
		new FaloTheBardClue("Penance healers runners and more, obtaining this body often gives much deplore.", any("Fighter Torso", item(ItemID.BARBASSAULT_PENANCE_FIGHTER_TORSO), item(ItemID.BARBASSAULT_PENANCE_FIGHTER_TORSO_TROUVER))),
		new FaloTheBardClue("Strangely found in a chest, many believe these gloves are the best.", item(ItemID.HUNDRED_GAUNTLETS_LEVEL_10)),
		new FaloTheBardClue("These gloves of white won't help you fight, but aid in cooking, they just might.", item(ItemID.GAUNTLETS_OF_COOKING)),
		new FaloTheBardClue("They come from some time ago, from a land unto the east. Fossilised they have become, this small and gentle beast.", item(ItemID.FOSSIL_NUMULITE)),
		new FaloTheBardClue("To slay a dragon you must first do, before this chest piece can be put on you.", item(ItemID.RUNE_PLATEBODY)),
		new FaloTheBardClue("Vampyres are agile opponents, damaged best with a weapon of many components.", any("Rod of Ivandis or Ivandis/Blisterwood flail", range(ItemID.BURGH_ROD_COMMAND_FINAL_10, ItemID.BURGH_ROD_COMMAND_FINAL_1), item(ItemID.IVANDIS_FLAIL), item(ItemID.BLISTERWOOD_FLAIL)))
	);

	private static final WorldPoint LOCATION = new WorldPoint(2689, 3550, 0);
	private static final String FALO_THE_BARD = "Falo the Bard";

	private static SingleItemRequirement item(int itemId)
	{
		return new SingleItemRequirement(itemId);
	}

	private static AnyRequirementCollection any(String name, ItemRequirement... requirements)
	{
		return new AnyRequirementCollection(name, requirements);
	}

	private static RangeItemRequirement range(int startItemId, int endItemId)
	{
		return range(null, startItemId, endItemId);
	}

	private static RangeItemRequirement range(String name, int startItemId, int endItemId)
	{
		return new RangeItemRequirement(name, startItemId, endItemId);
	}

	private final String text;
	@Nonnull
	private final ItemRequirement[] itemRequirements;

	private FaloTheBardClue(String text, @Nonnull ItemRequirement... itemRequirements)
	{
		this.text = text;
		this.itemRequirements = itemRequirements;
	}

	@Override
	public void makeOverlayHint(PanelComponent panelComponent, ClueScrollPlugin plugin)
	{
		panelComponent.getChildren().add(TitleComponent.builder().text("Falo the Bard Clue").build());

		panelComponent.getChildren().add(LineComponent.builder().left("NPC:").build());
		panelComponent.getChildren().add(LineComponent.builder()
			.left(FALO_THE_BARD)
			.leftColor(TITLED_CONTENT_COLOR)
			.build());

		panelComponent.getChildren().add(LineComponent.builder().left("Item:").build());

		Item[] inventory = plugin.getInventoryItems();

		// If inventory is null, the player has nothing in their inventory
		if (inventory == null)
		{
			inventory = new Item[0];
		}

		for (ItemRequirement requirement : itemRequirements)
		{
			boolean inventoryFulfilled = requirement.fulfilledBy(inventory);

			panelComponent.getChildren().add(LineComponent.builder()
				.left(requirement.getCollectiveName(plugin.getClient()))
				.leftColor(TITLED_CONTENT_COLOR)
				.right(inventoryFulfilled ? "\u2713" : "\u2717")
				.rightFont(FontManager.getDefaultFont())
				.rightColor(inventoryFulfilled ? Color.GREEN : Color.RED)
				.build());
		}

		renderOverlayNote(panelComponent, plugin);
	}

	@Override
	public void makeWorldOverlayHint(Graphics2D graphics, ClueScrollPlugin plugin)
	{
		if (!LOCATION.isInScene(plugin.getClient()))
		{
			return;
		}

		for (NPC npc : plugin.getNpcsToMark())
		{
			OverlayUtil.renderActorOverlayImage(graphics, npc, plugin.getClueScrollImage(), Color.ORANGE, IMAGE_Z_OFFSET);
		}
	}

	@Override
	public String[] getNpcs(ClueScrollPlugin plugin)
	{
		return new String[] {FALO_THE_BARD};
	}

	@Override
	public int[] getConfigKeys()
	{
		return new int[]{text.hashCode()};
	}

	public static FaloTheBardClue forText(String text)
	{
		for (FaloTheBardClue clue : CLUES)
		{
			if (clue.text.equalsIgnoreCase(text))
			{
				return clue;
			}
		}

		return null;
	}
}
