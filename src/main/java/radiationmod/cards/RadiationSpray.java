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
import radiationmod.modcore.CardColorEnum;

public class RadiationSpray extends CustomCard {
    public static final String ID = "RadiationMod:RadiationSpray";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 1;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.COMMON; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int DAMAGE_AMOUNT = 6;
    private static final int UPGRADE_PLUS_DMG = 3;
    private static final int RADIATION_AMOUNT = 3;
    private static final int UPGRADE_PLUS_RADIATION = 1;

    public RadiationSpray() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE_AMOUNT;
        this.baseMagicNumber = this.magicNumber = RADIATION_AMOUNT; // 用 magicNumber 存储辐射层数
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 伤害动作
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, damageTypeForTurn), AbstractGameAction.AttackEffect.POISON) // 换个特效
        );
        // 施加辐射动作
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new RadiationPower(m, this.magicNumber), this.magicNumber)
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_PLUS_DMG);
            upgradeMagicNumber(UPGRADE_PLUS_RADIATION); // 升级辐射层数
            initializeDescription();
        }
    }
} 