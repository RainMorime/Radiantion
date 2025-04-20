package radiationmod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class DecreaseMaxHPAction extends AbstractGameAction {
    private static final float DURATION = 0.1F;

    public DecreaseMaxHPAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.target = target;
        this.source = source;
        this.amount = amount;
        this.actionType = ActionType.DAMAGE; // Or perhaps ActionType.SPECIAL
        this.duration = DURATION;
    }

    public void update() {
        if (this.duration == DURATION && this.target != null) {
            // Check if target is alive
            if (this.target.isDying || this.target.currentHealth <= 0) {
                this.isDone = true;
                return;
            }

            // Reduce max HP, ensuring it doesn't go below 1
            this.target.decreaseMaxHealth(this.amount);

            // Optional: Add visual effect like purple swirl
            // AbstractDungeon.effectsQueue.add(new StrikeEffect(this.target, this.target.hb.cX, this.target.hb.cY, this.amount));
            // AbstractDungeon.effectsQueue.add(new HealthBarUpdatedEffect(this.target));


            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }

        this.tickDuration(); // Important to make the action finish
    }
} 