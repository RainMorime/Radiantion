package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.LingeringRadiationPower; // 需要创建这个 Power 类

public class LingeringRadiation extends CustomCard {
    public static final String ID = "RadiationMod:LingeringRadiation";
    private static final String NAME = "持久辐射"; // 需要本地化
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 3;
    private static final String DESCRIPTION = "你施加的 radiationmod:radiation 不再衰减。"; // 使用关键词 ID
    private static final CardType TYPE = CardType.POWER; // 类型为能力
    private static final CardColor COLOR = CardColor.COLORLESS; // 示例颜色
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
            // 能力牌升级通常是降低费用或增加效果（如果适用）
            // upgradeBaseCost(2); // 例如，升级后费用变为 2
            // 如果有可升级的效果，在这里添加
            initializeDescription(); // 如果升级改变了描述文本，需要更新
        }
    }
} 