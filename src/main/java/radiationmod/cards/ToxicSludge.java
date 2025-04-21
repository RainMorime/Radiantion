package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.RadiationPower;
import radiationmod.modcore.CardColorEnum;

public class ToxicSludge extends CustomCard {
    public static final String ID = "RadiationMod:ToxicSludge";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int RADIATION_AMOUNT = 5;
    private static final int UPGRADE_PLUS_RADIATION = 2;

    public ToxicSludge() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = RADIATION_AMOUNT;
        this.exhaust = false; // 默认不消耗，按描述是 Pollute Land 消耗
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 创建带有"不衰减"标记的辐射Power
        RadiationPower radiation = new RadiationPower(m, this.magicNumber);
        radiation.setNoDecayThisTurn(true); // 设置不衰减标记
        
        // 施加辐射
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, radiation, this.magicNumber)
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_RADIATION);
            initializeDescription();
        }
    }
} 