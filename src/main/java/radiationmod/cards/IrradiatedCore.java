package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.IrradiatedCorePower;
import radiationmod.modcore.CardColorEnum;

/**
 * 辐能核心 - 能力牌，将辐射伤害转化为力量
 */
public class IrradiatedCore extends CustomCard {
    public static final String ID = "RadiationMod:IrradiatedCore";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 临时使用Strike图片
    private static final int COST = 2;
    private static final CardType TYPE = CardType.POWER;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int POWER_AMOUNT = 1;
    private static final int UPGRADE_POWER_AMOUNT = 1;

    public IrradiatedCore() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = POWER_AMOUNT;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 施加辐能核心能力
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new IrradiatedCorePower(p, this.magicNumber), this.magicNumber)
        );
    }
    
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_POWER_AMOUNT);
            this.initializeDescription();
        }
    }
} 