package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import radiationmod.powers.RadiationPower;
import radiationmod.powers.SelfRadiationPower;
import radiationmod.modcore.CardColorEnum;

/**
 * 孤注一掷 - 攻击牌，强力AOE，施加大量辐射，但有巨大风险
 */
public class DesperateBlast extends CustomCard {
    public static final String ID = "RadiationMod:DesperateBlast";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 临时使用Strike图片
    private static final int COST = 3;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

    private static final int DAMAGE = 15;
    private static final int UPGRADE_PLUS_DMG = 5;
    private static final int RADIATION_AMOUNT = 8;

    public DesperateBlast() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE;
        this.baseMagicNumber = this.magicNumber = RADIATION_AMOUNT;
        this.isMultiDamage = true; // 多目标伤害
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 对所有敌人造成伤害
        AbstractDungeon.actionManager.addToBottom(
            new DamageAllEnemiesAction(p, this.multiDamage, this.damageTypeForTurn, 
                AbstractGameAction.AttackEffect.FIRE)
        );
        
        // 对所有敌人施加辐射
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDying && !monster.isDead) {
                AbstractDungeon.actionManager.addToBottom(
                    new ApplyPowerAction(monster, p, new RadiationPower(monster, this.magicNumber), this.magicNumber)
                );
            }
        }
        
        // 使自我辐射翻倍
        AbstractPower selfRadiation = p.getPower(SelfRadiationPower.POWER_ID);
        if (selfRadiation != null && selfRadiation.amount > 0) {
            int currentAmount = selfRadiation.amount;
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new SelfRadiationPower(p, currentAmount), currentAmount)
            );
        }
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