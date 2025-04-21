package radiationmod.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import radiationmod.powers.RadiationPower; // 需要导入 RadiationPower
import radiationmod.modcore.CardColorEnum;

public class RadiationShielding extends CustomCard {
    public static final String ID = "RadiationMod:RadiationShielding";
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final String NAME = cardStrings.NAME;
    private static final String DESCRIPTION = cardStrings.DESCRIPTION;
    private static final String IMG_PATH = "RadiationModResources/img/cards/Strike.png"; // 示例路径
    private static final int COST = 1;
    // 使用 MagicNumber 存储额外格挡值
    private static final CardType TYPE = CardType.SKILL;
    private static final CardColor COLOR = CardColorEnum.RADIATION_GREEN;
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

    // 更新动态描述部分，使用本地化文本
    @Override
    public void applyPowers() {
        int currentExtraBlock = 0;
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (monster != null && !monster.isDeadOrEscaped() && monster.hasPower(RadiationPower.POWER_ID)) {
                currentExtraBlock += this.magicNumber;
            }
        }
        
        // 存储原始基础格挡值
        int originalBaseBlock = this.baseBlock;
        
        // 更新基础格挡值以包含临时的额外格挡
        this.baseBlock = BASE_BLOCK + currentExtraBlock;
        super.applyPowers(); // 调用父类计算最终格挡
        
        // 计算完后恢复基础格挡值
        this.baseBlock = originalBaseBlock;
        this.isBlockModified = (this.block != this.baseBlock); // 标记是否修改
        
        // 更新描述（可选，如果需要显示动态计算的总和）
        // String desc = upgraded ? UPGRADE_DESCRIPTION : DESCRIPTION;
        // this.rawDescription = desc + " NL (当前额外: " + currentExtraBlock + ")";
        // initializeDescription();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        int currentExtraBlock = 0;
         for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
             if (monster != null && !monster.isDeadOrEscaped() && monster.hasPower(RadiationPower.POWER_ID)) {
                 currentExtraBlock += this.magicNumber;
             }
         }
         
         // 存储原始基础格挡值
        int originalBaseBlock = this.baseBlock;
         
         this.baseBlock = BASE_BLOCK + currentExtraBlock;
         super.calculateCardDamage(mo); // 父类方法计算 block
         
         // 恢复基础格挡值
         this.baseBlock = originalBaseBlock;
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