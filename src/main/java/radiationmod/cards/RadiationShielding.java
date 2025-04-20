package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.RadiationPower; // 需要导入 RadiationPower

public class RadiationShielding extends CustomCard {
    public static final String ID = "RadiationMod:RadiationShielding";
    private static final String NAME = "辐射护盾"; // 需要本地化
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 示例路径
    private static final int COST = 1;
    // 使用 MagicNumber 存储额外格挡值
    private static final String DESCRIPTION = "获得 !B! 点格挡。 NL 每有一个带有 radiationmod:radiation 的敌人，额外获得 !M! 点格挡。"; // 使用关键词 ID
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColor.COLORLESS; // 示例颜色
    private static final CardRarity RARITY = CardRarity.COMMON; // 示例稀有度
    private static final CardTarget TARGET = CardTarget.SELF;

    private static final int BASE_BLOCK = 7;
    private static final int UPGRADE_PLUS_BASE_BLOCK = 3;
    private static final int EXTRA_BLOCK_PER_RADIATED = 2; // 每有一个辐射敌人获得的额外格挡
    private static final int UPGRADE_PLUS_EXTRA_BLOCK = 1; // 升级后额外格挡增加

    public RadiationShielding() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.baseBlock = BASE_BLOCK;
        this.baseMagicNumber = this.magicNumber = EXTRA_BLOCK_PER_RADIATED;
        this.initializeDescription();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        // 先获得基础格挡
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, this.block));

        // 计算额外格挡
        int extraBlock = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped() && monster.hasPower(RadiationPower.POWER_ID)) {
                extraBlock += this.magicNumber;
            }
        }

        // 如果有额外格挡，则添加
        if (extraBlock > 0) {
            AbstractDungeon.actionManager.addToBottom(new GainBlockAction(p, p, extraBlock));
        }
    }

    // 当卡牌在手牌中或使用时，需要动态更新格挡值以正确显示
    @Override
    public void applyPowers() {
        int currentExtraBlock = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped() && monster.hasPower(RadiationPower.POWER_ID)) {
                currentExtraBlock += this.magicNumber;
            }
        }
        // 更新基础格挡值以包含临时的额外格挡，这样 !B! 才会显示正确的总数
        this.baseBlock = BASE_BLOCK + currentExtraBlock;
        // 调用父类方法计算最终格挡（可能受其他效果影响）
        super.applyPowers();
        // 计算完后恢复基础格挡值，防止永久改变
        this.baseBlock = BASE_BLOCK;
        // 标记格挡值是否被修改，用于显示（如果基础值和计算值不同）
        this.isBlockModified = (this.block != this.baseBlock);

        // 更新描述可能有点复杂，因为 !B! 已经包含了额外格挡
        // 可以选择不在描述中显示动态计算的总和，只显示基础值和额外值逻辑
        // 或者像下面这样尝试更新描述（可能需要调整）
        // this.rawDescription = DESCRIPTION + " NL (当前额外: " + currentExtraBlock + ")";
        // initializeDescription();
    }

    // 鼠标悬停时也计算
    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int currentExtraBlock = 0;
         for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
             if (monster != null && !monster.isDeadOrEscaped() && monster.hasPower(RadiationPower.POWER_ID)) {
                 currentExtraBlock += this.magicNumber;
             }
         }
         this.baseBlock = BASE_BLOCK + currentExtraBlock;
         super.calculateCardDamage(mo); // 父类方法名虽然叫 damage，但也会计算 block
         this.baseBlock = BASE_BLOCK;
         this.isBlockModified = (this.block != this.baseBlock);
    }

     @Override
    public void onMoveToDiscard() {
        // 恢复原始描述
        // this.rawDescription = DESCRIPTION;
        // initializeDescription();
    }


    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeBlock(UPGRADE_PLUS_BASE_BLOCK); // 升级基础格挡
            upgradeMagicNumber(UPGRADE_PLUS_EXTRA_BLOCK); // 升级额外格挡系数
            // 更新描述文本（如果升级改变了基础值和额外值的描述）
            // this.rawDescription = UPGRADED_DESCRIPTION; // 如果有单独的升级描述
            initializeDescription();
        }
    }
} 