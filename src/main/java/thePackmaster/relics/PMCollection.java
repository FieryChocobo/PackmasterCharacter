package thePackmaster.relics;

import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import thePackmaster.SpireAnniversary5Mod;
import thePackmaster.packs.AbstractCardPack;
import thePackmaster.util.Wiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static thePackmaster.SpireAnniversary5Mod.*;

public class PMCollection extends AbstractPackmasterRelic {
    public static final String ID = makeID("PMCollection");

    public PMCollection() {
        super(ID, RelicTier.SHOP, LandingSound.FLAT, true);
    }

    @Override
    public void onEquip() {
        AbstractDungeon.getCurrRoom().rewards.clear();

        AbstractCardPack pack = getRandomPackFromAll(new Random(Settings.seed + 37));
        for (String s : pack.getCards()) {
            if (CardLibrary.getCard(s).rarity == AbstractCard.CardRarity.COMMON ||
                    CardLibrary.getCard(s).rarity == AbstractCard.CardRarity.UNCOMMON ||
                    CardLibrary.getCard(s).rarity == AbstractCard.CardRarity.RARE) {

                ArrayList<AbstractCard> cardReward = new ArrayList<>();
                cardReward.add(CardLibrary.getCard(s).makeCopy());
                RewardItem r = StSLib.generateCardReward(cardReward, false);
                r.cards.forEach(c -> Wiz.p().relics.forEach(rel -> rel.onPreviewObtainCard(c)));
                AbstractDungeon.getCurrRoom().addCardReward(r);
            }
        }

        recordStats(pack.packID);
        skipDefaultCardRewards = true;
        AbstractDungeon.combatRewardScreen.open(this.DESCRIPTIONS[1]);
        skipDefaultCardRewards = false;
        AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.0F;
    }

    private static final Map<String, String> stats = new HashMap<>();
    private static final String PACK_STAT = "pack";

    public String getStatsDescription() {
        AbstractCardPack pack = SpireAnniversary5Mod.packsByID.getOrDefault(stats.get(PACK_STAT), null);
        return pack != null ? DESCRIPTIONS[2].replace("{0}", pack.name) : "";
    }

    public String getExtendedStatsDescription(int totalCombats, int totalTurns) {
        return getStatsDescription();
    }

    public void resetStats() {
        stats.put(PACK_STAT, null);
    }

    public JsonElement onSaveStats() {
        Gson gson = new Gson();
        List<String> statsToSave = new ArrayList<>();
        statsToSave.add(stats.get(PACK_STAT));
        return gson.toJsonTree(statsToSave);
    }

    public void onLoadStats(JsonElement jsonElement) {
        if (jsonElement != null) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            JsonElement pack = jsonArray.get(0);
            stats.put(PACK_STAT, !pack.isJsonNull() ? pack.getAsString() : null);
        } else {
            resetStats();
        }
    }

    public static void recordStats(String pack) {
        stats.put(PACK_STAT, pack);
    }
}
