package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower; // 需要导入 AbstractPower
import radiationmod.powers.RadiationPower; // 需要导入 RadiationPower

public class RadiationDetonation extends CustomCard {
    public static final String ID = "RadiationMod:RadiationDetonation";
    private static final String NAME = "辐射引爆"; // 需要本地化
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 2;
    // 使用 M 作为倍率，动态计算伤害
    private static final String DESCRIPTION = "对一名敌人造成其 radiationmod:radiation 层数x !M! 的伤害。移除其所有 radiationmod:radiation 。"; // 使用关键词 ID
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColor.COLORLESS; // 示例颜色
    private static final CardRarity RARITY = CardRarity.UNCOMMON; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int DAMAGE_MULTIPLIER = 3; // 伤害倍率
    private static final int UPGRADE_PLUS_MULTIPLIER = 1; // 升级增加倍率

    public RadiationDetonation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = DAMAGE_MULTIPLIER;
        // 这个卡牌的基础伤害 baseDamage 设置为 0 或不设置，因为伤害完全依赖于辐射层数
        this.baseDamage = 0;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 检查目标是否有辐射能力
        AbstractPower radiation = m.getPower(RadiationPower.POWER_ID);
        if (radiation != null) {
            int radiationAmount = radiation.amount;
            // 计算最终伤害
            int finalDamage = radiationAmount * this.magicNumber;

            // 如果计算出的伤害大于0，则执行伤害动作
            if (finalDamage > 0) {
                AbstractDungeon.actionManager.addToBottom(
                        new DamageAction(m, new DamageInfo(p, finalDamage, DamageInfo.DamageType.NORMAL), // 使用计算出的伤害，类型为普通
                                AbstractGameAction.AttackEffect.FIRE) // 换个爆炸特效
                );
            }

            // 移除目标的所有辐射
            AbstractDungeon.actionManager.addToBottom(
                    new RemoveSpecificPowerAction(m, p, RadiationPower.POWER_ID)
            );
        }
    }

    // 当卡牌在手牌中时，动态计算伤害以更新描述
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        super.calculateCardDamage(mo); // 先执行基类的计算（如果有的话）
        AbstractPower radiation = mo.getPower(RadiationPower.POWER_ID);
        int radiationAmount = 0;
        if (radiation != null) {
            radiationAmount = radiation.amount;
        }
        this.rawDescription = "对一名敌人造成其 radiationmod:radiation 层数x !M! 的伤害。移除其所有 radiationmod:radiation 。"; // 基础描述
        this.rawDescription += " NL (造成 " + (radiationAmount * this.magicNumber) + " 点伤害)"; // 附加当前伤害信息
        initializeDescription(); // 更新卡面描述
    }

    // 当鼠标悬停在卡牌上时，如果目标改变，也需要更新描述
    @Override
    public void applyPowers() {
        super.applyPowers();
        // 这里可以尝试获取当前目标怪物，但applyPowers时可能没有明确目标
        // 为了简化，我们主要依赖 calculateCardDamage，它在选择目标时会被调用
        // 如果需要在没有目标时也显示基础倍率，可以在这里处理
         this.rawDescription = "对一名敌人造成其 radiationmod:radiation 层数x !M! 的伤害。移除其所有 radiationmod:radiation 。"; // 恢复基础描述
         initializeDescription();
    }

     @Override
    public void onMoveToDiscard() {
        // 当卡牌离开手牌时，恢复原始描述
        this.rawDescription = "对一名敌人造成其 radiationmod:radiation 层数x !M! 的伤害。移除其所有 radiationmod:radiation 。"; // 恢复基础描述
        initializeDescription();
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeMagicNumber(UPGRADE_PLUS_MULTIPLIER); // 升级倍率
            initializeDescription();
        }
    }
} 