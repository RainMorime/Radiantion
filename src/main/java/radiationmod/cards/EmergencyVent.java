package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.modcore.CardColorEnum;

/**
 * 紧急排放 - 技能牌，以生命值为代价获得能量
 */
public class EmergencyVent extends CustomCard {
    public static final String ID = "RadiationMod:EmergencyVent";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 临时使用Strike图片
    private static final int COST = 0;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int HP_LOSS = 4;
    private static final int UPGRADE_REDUCE_HP_LOSS = 1;
    private static final int ENERGY_GAIN = 2;

    public EmergencyVent() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = HP_LOSS;
        this.exhaust = true;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 失去生命值
        AbstractDungeon.actionManager.addToBottom(
            new LoseHPAction(p, p, this.magicNumber)
        );
        
        // 获得能量
        AbstractDungeon.actionManager.addToBottom(
            new GainEnergyAction(ENERGY_GAIN)
        );
    }
    
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(-UPGRADE_REDUCE_HP_LOSS); // 减少生命值损失
            this.initializeDescription();
        }
    }
} 