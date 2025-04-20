package radiationmod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

// 这个能力施加给玩家，用来标记辐射是否应该衰减
public class LingeringRadiationPower extends AbstractPower {
    public static final String POWER_ID = "RadiationMod:LingeringRadiationPower";
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public LingeringRadiationPower(AbstractCreature owner, int amount) {
        this.name = NAME;
        this.ID = POWER_ID;
        this.owner = owner; // 这个 owner 是玩家
        this.amount = amount; // 通常为 1，表示能力激活
        this.type = PowerType.BUFF; // 对玩家是增益效果
        this.isTurnBased = false; // 不是回合制效果

        // 加载图标 (需要准备 84x84 和 32x32 的 png 图片)
        // this.loadRegion("your_lingering_radiation_icon"); // 替换成你的图标文件名
        this.loadRegion("static_discharge"); // 临时使用静电释放图标

        updateDescription();
    }

    @Override
    public void updateDescription() {
        // 这个能力的描述通常很简单，因为它只作为标记
        this.description = DESCRIPTIONS[0];
    }

    // 这个能力本身通常不做任何事情，它的存在被其他代码检查
} 