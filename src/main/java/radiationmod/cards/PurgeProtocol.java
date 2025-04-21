package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import radiationmod.powers.SelfRadiationPower;
import radiationmod.modcore.CardColorEnum;

/**
 * 净化协议 - 技能牌，移除自我辐射并转化为格挡
 */
public class PurgeProtocol extends CustomCard {
    public static final String ID = "RadiationMod:PurgeProtocol";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 临时使用Strike图片
    private static final int COST = 1;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BLOCK_PER_STACK = 4;
    private static final int UPGRADE_BLOCK = 2;

    public PurgeProtocol() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = BLOCK_PER_STACK;
        this.exhaust = true;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查玩家是否有自我辐射
        AbstractPower selfRadiation = p.getPower(SelfRadiationPower.POWER_ID);
        if (selfRadiation != null && selfRadiation.amount > 0) {
            int radiationAmount = selfRadiation.amount;
            
            // 移除所有自我辐射
            AbstractDungeon.actionManager.addToBottom(
                new RemoveSpecificPowerAction(p, p, SelfRadiationPower.POWER_ID)
            );
            
            // 获得格挡
            int blockAmount = radiationAmount * this.magicNumber;
            AbstractDungeon.actionManager.addToBottom(
                new GainBlockAction(p, p, blockAmount)
            );
        }
    }
    
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_BLOCK);
            this.initializeDescription();
        }
    }
} 