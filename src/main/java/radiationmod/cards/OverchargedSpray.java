package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.RadiationPower;
import radiationmod.powers.SelfRadiationPower;
import radiationmod.modcore.CardColorEnum;

/**
 * 过载喷射 - 攻击牌，造成高伤害同时施加辐射，但自身也受辐射影响
 */
public class OverchargedSpray extends CustomCard {
    public static final String ID = "RadiationMod:OverchargedSpray";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 临时使用Strike图片
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int DAMAGE = 10;
    private static final int UPGRADE_PLUS_DMG = 4;
    private static final int RADIATION_AMOUNT = 5;
    private static final int SELF_RADIATION_AMOUNT = 2;

    public OverchargedSpray() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = RADIATION_AMOUNT;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 造成伤害
        AbstractDungeon.actionManager.addToBottom(
            new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                AbstractGameAction.AttackEffect.SLASH_HEAVY)
        );
        
        // 施加辐射
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(m, p, new RadiationPower(m, this.magicNumber), this.magicNumber)
        );
        
        // 施加自我辐射
        AbstractDungeon.actionManager.addToBottom(
            new ApplyPowerAction(p, p, new SelfRadiationPower(p, SELF_RADIATION_AMOUNT), SELF_RADIATION_AMOUNT)
        );
    }
    
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DMG);
            this.initializeDescription();
        }
    }
} 