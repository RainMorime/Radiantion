package radiationmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;


/**
 * 自我辐射Power - 负面效果，对玩家产生持续伤害和削弱效果
 */
public class SelfRadiationPower extends AbstractPower {
    public static final String POWER_ID = "RadiationMod:SelfRadiation";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private boolean appliedStrDebuff = false; // 是否已应用力量/敏捷减益
    private boolean appliedEnergyDebuff = false; // 是否已应用能量减益

    public SelfRadiationPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        
        this.type = PowerType.DEBUFF;
        this.isTurnBased = false;
        
        // 加载图标
        this.loadRegion("poison"); // 暂用poison图标
        
        updateDescription();
        checkDebuffs(); // 检查并应用减益效果
    }
    
    // 检查当前层数是否需要应用减益效果
    private void checkDebuffs() {
        // 层数 > 3: 力量和敏捷-1
        if (this.amount > 3 && !appliedStrDebuff) {
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new StrengthPower(owner, -1), -1)
            );
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new DexterityPower(owner, -1), -1)
            );
            appliedStrDebuff = true;
        } else if (this.amount <= 3 && appliedStrDebuff) {
            // 如果层数降至3或以下，移除减益
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1)
            );
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(owner, owner, new DexterityPower(owner, 1), 1)
            );
            appliedStrDebuff = false;
        }
        
        // 层数 > 5: 能量-1
        if (this.amount > 5 && !appliedEnergyDebuff) {
            appliedEnergyDebuff = true;
            // 能量减益在回合开始时应用
        } else if (this.amount <= 5 && appliedEnergyDebuff) {
            appliedEnergyDebuff = false;
        }
    }
    
    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        if (this.amount > 3) {
            this.description += DESCRIPTIONS[2];
        }
        if (this.amount > 5) {
            this.description += DESCRIPTIONS[3];
        }
    }
    
    @Override
    public void atStartOfTurn() {
        if (this.owner != null && !this.owner.isDying && this.amount > 0) {
            this.flash();
            
            // 造成层数等量的伤害
            AbstractDungeon.actionManager.addToBottom(
                new DamageAction(this.owner, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS))
            );
            
            // 如果层数>5，减少1点能量
            if (this.amount > 5 && appliedEnergyDebuff) {
                // 直接减少玩家能量
                AbstractDungeon.player.loseEnergy(1);
            }
        }
    }
    
    @Override
    public void stackPower(int stackAmount) {
        this.fontScale = 8.0F;
        this.amount += stackAmount;
        
        // 检查新的层数是否触发减益
        checkDebuffs();
        
        // 更新描述
        updateDescription();
    }
    
    @Override
    public void reducePower(int reduceAmount) {
        if (reduceAmount > 0) {
            this.fontScale = 8.0F;
            this.amount -= reduceAmount;
            if (this.amount <= 0) {
                this.amount = 0;
            }
            
            // 检查减少后是否需要移除减益
            checkDebuffs();
            
            // 更新描述
            updateDescription();
        }
    }
} 