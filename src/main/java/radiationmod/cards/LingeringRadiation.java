package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.LingeringRadiationPower; // 需要创建这个 Power 类
import radiationmod.modcore.CardColorEnum;

public class LingeringRadiation extends CustomCard {
    public static final String ID = "RadiationMod:LingeringRadiation";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 3;
    private static final CardType TYPE = CardType.POWER; // 类型为能力
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.RARE; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.SELF; // 目标为自己

    public LingeringRadiation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        // 能力牌通常没有 Magic Number 或 Damage/Block
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 给玩家施加 LingeringRadiationPower
        AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(p, p, new LingeringRadiationPower(p, 1), 1) // 数量通常为 1
        );
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBaseCost(2); // 升级后费用变为 2
            initializeDescription();
        }
    }
} 