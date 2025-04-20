package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.RadiationPower;
import radiationmod.powers.RadiationDecayPreventionPower;

public class ToxicSludge extends CustomCard {
    public static final String ID = "RadiationMod:ToxicSludge";
    private static final String NAME = "毒性淤泥"; // 需要本地化
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 1;
    // 更新描述以反映新效果，并使用关键词 ID
    private static final String DESCRIPTION = "选择一名敌人。施加 !M! 层 radiationmod:radiation 。本回合其 radiationmod:radiation 不会衰减。"; // 需要本地化
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS; // 示例颜色
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
        // 施加辐射
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new RadiationPower(m, this.magicNumber), this.magicNumber)
        );
        // 施加阻止衰减的临时 Power
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(m, p, new RadiationDecayPreventionPower(m, 1), 1) // amount 为 1 表示阻止 1 个回合
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