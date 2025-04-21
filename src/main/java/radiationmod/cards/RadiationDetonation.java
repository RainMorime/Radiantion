package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import radiationmod.powers.RadiationPower;
import radiationmod.modcore.CardColorEnum;

public class RadiationDetonation extends CustomCard {
    public static final String ID = "RadiationMod:RadiationDetonation";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION; // Load upgrade description
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // Use Strike image
    private static final int COST = 2;
    private static final CardType TYPE = CardType.ATTACK;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
    private static final CardRarity RARITY = CardRarity.UNCOMMON; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.ENEMY;

    private static final int DAMAGE_MULTIPLIER = 3;
    private static final int UPGRADE_PLUS_MULTIPLIER = 1;

    public RadiationDetonation() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseMagicNumber = this.magicNumber = DAMAGE_MULTIPLIER;
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
        super.calculateCardDamage(mo); // 先执行基类的计算
        AbstractPower radiation = mo.getPower(RadiationPower.POWER_ID);
        int radiationAmount = (radiation != null) ? radiation.amount : 0;
        
        // 使用本地化描述
        this.rawDescription = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION; 
        this.rawDescription += " NL (造成 " + (radiationAmount * this.magicNumber) + " 点伤害)"; 
        initializeDescription(); // 更新卡面描述
    }

    // 当鼠标悬停在卡牌上时，恢复基础描述，让calculateCardDamage处理目标
    @Override
    public void applyPowers() {
        super.applyPowers();
        this.rawDescription = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
        initializeDescription();
    }

     @Override
    public void onMoveToDiscard() {
        // 当卡牌离开手牌时，恢复原始描述
        this.rawDescription = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
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