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

public class PolluteLand extends CustomCard {
    public static final String ID = "RadiationMod:PolluteLand";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 2;
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY; // 目标为所有敌人

    private static final int RADIATION_AMOUNT = 4;
    private static final int UPGRADE_PLUS_RADIATION = 2;

    public PolluteLand() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = RADIATION_AMOUNT;
        this.exhaust = true; // 卡牌消耗
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) { // m 在这里通常是 null，因为目标是 ALL_ENEMY
        // 遍历所有怪物
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            // 确保怪物存活且未死亡
            if (monster != null && !monster.isDeadOrEscaped()) {
                AbstractDungeon.actionManager.addToBottom(
                        new ApplyPowerAction(monster, p, new RadiationPower(monster, this.magicNumber), this.magicNumber)
                );
            }
        }
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