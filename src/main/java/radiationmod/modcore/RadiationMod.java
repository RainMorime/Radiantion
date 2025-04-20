package radiationmod.modcore;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import radiationmod.cards.Strike;
import radiationmod.cards.RadiationSpray;
import radiationmod.cards.ToxicSludge;
import radiationmod.cards.PolluteLand;
import radiationmod.cards.LingeringRadiation;
import radiationmod.cards.RadiationDetonation;
import radiationmod.cards.RadiationShielding;

@SpireInitializer
public class RadiationMod implements
        EditCardsSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber {

    public static final String MOD_ID = "RadiationMod";

    public RadiationMod() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new RadiationMod();
    }

    private static String makeLocalizationPath(String filename) {
        String langFolder = "zhs";
        return MOD_ID + "Resources/localization/" + langFolder + "/" + filename;
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath("cards.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath("powers.json"));
        BaseMod.loadCustomStringsFile(KeywordStrings.class, makeLocalizationPath("keywords.json"));
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new RadiationSpray());
        BaseMod.addCard(new ToxicSludge());
        BaseMod.addCard(new PolluteLand());
        BaseMod.addCard(new LingeringRadiation());
        BaseMod.addCard(new RadiationDetonation());
        BaseMod.addCard(new RadiationShielding());
    }

    @Override
    public void receiveEditKeywords() {
        // BaseMod automatically handles keyword registration via loadCustomStringsFile
        // in receiveEditStrings if the JSON format is correct.
        // No need to manually parse and add keywords here.
    }
}


