package radiationmod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

// 临时的、不可见的 Power，用于标记本回合辐射不衰减
public class RadiationDecayPreventionPower extends AbstractPower {
    public static final String POWER_ID = "RadiationMod:RadiationDecayPrevention";
    // 不需要名字或描述，因为它是隐藏的
    // private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    // public static final String NAME = powerStrings.NAME;
    // public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public RadiationDecayPreventionPower(AbstractCreature owner, int amount) {
        this.name = "Radiation Decay Prevention"; // 内部名称
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount; // 通常是 1，代表阻止衰减的回合数
        this.type = PowerType.DEBUFF; // 虽然是阻止衰减，但施加给敌人，类型仍是DEBUFF
        this.isTurnBased = true; // 这是关键，它会在回合开始时自动减少 amount
        this.priority = 99; // 确保它在其他回合结束效果之前处理（如果需要）

        // 使其不可见
        this.loadRegion("flight"); // 可以随便用一个图标，因为它不会显示
                                   // 或者不加载图标，但有时会导致默认问号图标

        // 不需要 updateDescription，因为它是隐藏的
    }

    // 在回合开始时，减少层数。当层数变为0时，移除自身。
    // 这是 isTurnBased = true 的标准行为，但我们重写它以确保立即移除
    @Override
    public void atStartOfTurn() {
        if (this.amount <= 0) {
             AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        } else {
             // 如果需要持续多回合阻止，可以在这里减少 amount
             // 但对于"本回合"，我们希望它在下回合开始时就消失
             AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
        }
    }

     
     

    // 这个能力不需要可见的描述
    @Override
    public void updateDescription() {
        this.description = "Hidden power.";
    }
} 