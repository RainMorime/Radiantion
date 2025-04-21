package radiationmod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

/**
 * 辐能核心Power - 当受到自我辐射伤害时获得力量
 */
public class IrradiatedCorePower extends AbstractPower {
    public static final String POWER_ID = "RadiationMod:IrradiatedCore";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public IrradiatedCorePower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount; // 通常amount为1
        
        this.type = PowerType.BUFF;
        this.isTurnBased = false;
        
        // 加载图标
        this.loadRegion("anger"); // 暂时使用怒气图标
        
        updateDescription();
    }
    
    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        } else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }
    
    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        // 当受到自我辐射伤害时触发
        if (info.owner == this.owner && info.type == DamageInfo.DamageType.THORNS) {
            this.flash();
            // 每点伤害获得1点力量
            AbstractDungeon.actionManager.addToBottom(
                new ApplyPowerAction(this.owner, this.owner, 
                    new StrengthPower(this.owner, this.amount), this.amount)
            );
        }
        
        return damageAmount;
    }
} 