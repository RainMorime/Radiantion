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
    public static final String[] DESCRIPTIONS = {
            "在你的回合开始时，失去 #b",
            " 点最大生命值。 NL 回合结束时，层数减半（向下取整）。"
    };

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

    @Override
    public void updateDescription() {
        this.description = DESCRIPTIONS[0] + this.amount + DESCRIPTIONS[1];
    }

    @Override
    public void atStartOfTurn() {
        if (!this.owner.isPlayer) {
            if (this.owner != null && !this.owner.isDying && this.amount > 0) {
                if (this.owner.hasPower(RadiationDecayPreventionPower.POWER_ID)) {
                    this.flashWithoutSound();
                    return;
                }

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