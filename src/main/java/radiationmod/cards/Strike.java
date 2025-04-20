package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.RadiationPower;

public class Strike extends CustomCard {
    public static final String ID = "RadiationMod:Strike";
    private static final String NAME = "辐射打击";
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png";
    private static final int COST = 0;
    private static final String DESCRIPTION = "造成 !D! 点伤害。施加 1 层辐射。";
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS;
    private static final CardRarity RARITY = CardRarity.BASIC;
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int DAMAGE_AMOUNT = 6;
    private static final int UPGRADE_PLUS_DAMAGE = 3;
    private static final int RADIATION_AMOUNT = 1;

    public Strike() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseDamage = DAMAGE_AMOUNT;
        this.initializeDescription();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeDamage(UPGRADE_PLUS_DAMAGE);
            this.initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(
                new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn), 
                AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
        );
        
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new RadiationPower(m, RADIATION_AMOUNT), RADIATION_AMOUNT)
        );
    }
}