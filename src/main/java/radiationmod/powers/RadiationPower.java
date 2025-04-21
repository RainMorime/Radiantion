package radiationmod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import radiationmod.actions.DecreaseMaxHPAction;

public class RadiationPower extends AbstractPower {
    public static final String POWER_ID = "RadiationMod:Radiation";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    
    private boolean noDecayThisTurn = false; // 本回合不衰减的标记

    public RadiationPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        
        this.loadRegion("poison");
        this.type = PowerType.DEBUFF;
        this.isTurnBased = true;
        
        this.updateDescription();
    }
    
    // 设置是否本回合不衰减
    public void setNoDecayThisTurn(boolean value) {
        this.noDecayThisTurn = value;
    }

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
        
        // 如果有不衰减标记，在描述中显示
        if (this.noDecayThisTurn) {
            if (DESCRIPTIONS.length > 2) {
                this.description += DESCRIPTIONS[2];
            } else {
                this.description += " NL 本回合不会衰减。";
            }
        }
    }

    @Override
    public void atStartOfTurn() {
        if (!this.owner.isPlayer) {
            if (this.owner != null && !this.owner.isDying && this.amount > 0) {
                this.flash();
                AbstractDungeon.actionManager.addToBottom(
                        new DecreaseMaxHPAction(this.owner, this.owner, this.amount)
                );
            }
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) {
            if (this.owner != null && !this.owner.isDying && this.amount > 0) {
                // 检查是否有不衰减标记
                if (this.noDecayThisTurn) {
                    this.flash();
                    this.noDecayThisTurn = false; // 清除标记，下回合正常衰减
                    this.updateDescription(); // 更新描述
                    return;
                }
                
                // 检查玩家是否有持久辐射能力
                if (AbstractDungeon.player.hasPower(LingeringRadiationPower.POWER_ID)) {
                    this.flash();
                    return;
                }
                
                // 执行正常衰减
                int amountToReduce = this.amount / 2;

                if (amountToReduce > 0) {
                    AbstractDungeon.actionManager.addToBottom(
                            new ReducePowerAction(this.owner, this.owner, this.ID, amountToReduce)
                    );
                } else if (this.amount == 1) {
                    AbstractDungeon.actionManager.addToBottom(
                            new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID)
                    );
                }
            }
        }
    }
} 