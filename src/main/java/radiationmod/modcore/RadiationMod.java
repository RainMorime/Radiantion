package radiationmod.modcore;

import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;

import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.KeywordStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import radiationmod.cards.Strike;
import radiationmod.cards.RadiationSpray;
import radiationmod.cards.ToxicSludge;
import radiationmod.cards.PolluteLand;
import radiationmod.cards.LingeringRadiation;
import radiationmod.cards.RadiationDetonation;
import radiationmod.cards.RadiationShielding;
import radiationmod.cards.OverchargedSpray;
import radiationmod.cards.EmergencyVent;
import radiationmod.cards.IrradiatedCore;
import radiationmod.cards.PurgeProtocol;
import radiationmod.cards.DesperateBlast;
import radiationmod.cards.Strike_Rad;
import radiationmod.cards.Defend_Rad;
import com.badlogic.gdx.graphics.Color;
import radiationmod.character.RadiationEngineer;


@SpireInitializer
public class RadiationMod implements
        EditCardsSubscriber,
        EditKeywordsSubscriber,
        EditStringsSubscriber,
        EditCharactersSubscriber {

    public static final String MOD_ID = "RadiationMod";
    // 定义颜色常量
    private static final Color RADIATION_COLOR = new Color(0.2f, 0.8f, 0.2f, 1.0f);

    // --- 资源路径 --- (保持更新)
    private static final String ATTACK_CARD_BG = "RadiationModResources/img/512/bg_attack_512.png";
    private static final String SKILL_CARD_BG = "RadiationModResources/img/512/bg_skill_512.png";
    private static final String POWER_CARD_BG = "RadiationModResources/img/512/bg_power_512.png";
    private static final String ENERGY_ORB = "RadiationModResources/img/char/small_orb.png"; 
    private static final String CARD_ENERGY_ORB = "RadiationModResources/img/char/cost_orb.png"; 
    private static final String ATTACK_CARD_BG_PORTRAIT = "RadiationModResources/img/1024/bg_attack.png";
    private static final String SKILL_CARD_BG_PORTRAIT = "RadiationModResources/img/1024/bg_skill.png";
    private static final String POWER_CARD_BG_PORTRAIT = "RadiationModResources/img/1024/bg_power.png";
    // 尝试提供一个有效的 VFX 路径 (暂时重用 small_orb.png)
    private static final String ENERGY_ORB_PORTRAIT = "RadiationModResources/img/char/small_orb.png";

    public RadiationMod() {
        BaseMod.subscribe(this);
        
        // 添加颜色 (使用 null 作为大能量球路径)
        BaseMod.addColor(CardColorEnum.RADIATION_GREEN, 
                RADIATION_COLOR, RADIATION_COLOR, RADIATION_COLOR, RADIATION_COLOR, RADIATION_COLOR, RADIATION_COLOR, RADIATION_COLOR, // 各种颜色设置
                ATTACK_CARD_BG, 
                SKILL_CARD_BG, 
                POWER_CARD_BG, 
                ENERGY_ORB, // 小能量球路径
                ATTACK_CARD_BG_PORTRAIT, 
                SKILL_CARD_BG_PORTRAIT, 
                POWER_CARD_BG_PORTRAIT, 
                ENERGY_ORB_PORTRAIT, // <<< 使用 null
                CARD_ENERGY_ORB); // 卡牌能量球路径
    }

    public static void initialize() {
        new RadiationMod();
    }

    private static String makeLocalizationPath(String filename) {
        String langFolder = "zhs";
        return "RadiationModResources/localization/" + langFolder + "/" + filename;
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(
            new RadiationEngineer(RadiationEngineer.NAME, PlayerClassEnum.RADIATION_ENGINEER),
            RadiationEngineer.BUTTON, // Button image path
            RadiationEngineer.PORTRAIT, // Portrait image path
            PlayerClassEnum.RADIATION_ENGINEER);
    }

    @Override
    public void receiveEditStrings() {
        // Load character strings
        BaseMod.loadCustomStringsFile(CharacterStrings.class, makeLocalizationPath("characterStrings.json"));
        
        // Load other strings
        BaseMod.loadCustomStringsFile(CardStrings.class, makeLocalizationPath("cards.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, makeLocalizationPath("powers.json"));
        BaseMod.loadCustomStringsFile(KeywordStrings.class, makeLocalizationPath("keywords.json"));
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new Strike_Rad()); // 添加角色专属打击
        BaseMod.addCard(new Defend_Rad()); // 添加角色专属防御
        BaseMod.addCard(new Strike());
        BaseMod.addCard(new RadiationSpray());
        BaseMod.addCard(new ToxicSludge());
        BaseMod.addCard(new PolluteLand());
        BaseMod.addCard(new LingeringRadiation());
        BaseMod.addCard(new RadiationDetonation());
        BaseMod.addCard(new RadiationShielding());
        
        // 高风险高回报与自我污染系列卡牌
        BaseMod.addCard(new OverchargedSpray());
        BaseMod.addCard(new EmergencyVent());
        BaseMod.addCard(new IrradiatedCore());
        BaseMod.addCard(new PurgeProtocol());
        BaseMod.addCard(new DesperateBlast());
    }

    @Override
    public void receiveEditKeywords() {
        BaseMod.addKeyword("radiationmod", "辐射", new String[]{"辐射", "radiation", "radiationmod:radiation"}, 
            "目标回合开始时，失去层数点最大生命值。回合结束时，层数向下减半。");
    }
    
   
}


