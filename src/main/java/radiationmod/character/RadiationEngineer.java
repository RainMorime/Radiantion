package radiationmod.character;

import basemod.abstracts.CustomPlayer;
// import basemod.animations.SpineAnimation; // 移除动画导入
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import radiationmod.modcore.CardColorEnum; // 导入卡牌颜色枚举
import com.megacrit.cardcrawl.ui.panels.energyorb.EnergyOrbInterface; // Import EnergyOrbInterface

import java.util.ArrayList;

import static radiationmod.modcore.RadiationMod.MOD_ID; // 导入主Mod类的MOD_ID

/**
 * 辐射工兵角色类
 * 继承自BaseMod的CustomPlayer
 */
public class RadiationEngineer extends CustomPlayer {

    // Orb texture paths - required for constructor
    private static final String[] ORB_TEXTURES = {
            "RadiationModResources/img/char/small_orb.png", // Layer 1
            "RadiationModResources/img/char/small_orb.png", // Layer 2 (reuse)
            "RadiationModResources/img/char/small_orb.png", // Layer 3 (reuse)
            "RadiationModResources/img/char/small_orb.png", // Layer 4 (reuse)
            "RadiationModResources/img/char/small_orb.png"  // Layer 5 (reuse) - Try with 5 layers instead of 6
    };
    private static final String ORB_VFX_PATH = "RadiationModResources/img/char/small_orb.png"; // Reuse small_orb for VFX

    // --- 角色基础属性 ---
    public static final int ENERGY_PER_TURN = 3; // 每回合能量
    public static final int STARTING_HP = 70; // 初始生命值
    public static final int MAX_HP = 70; // 最大生命值
    public static final int STARTING_GOLD = 99; // 初始金币
    public static final int CARD_DRAW = 5; // 每回合抽牌数
    public static final int ORB_SLOTS = 0; // 初始充能球槽位 (辐射工兵可能不需要)

    // --- 角色文本 --- (从JSON加载)
    private static final String CHARACTER_ID = MOD_ID + ":RadiationEngineer"; // 角色唯一ID
    private static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(CHARACTER_ID); // 读取本地化文件
    public static final String NAME = characterStrings.NAMES[0]; // 角色名称 (从JSON读取)
    private static final String[] DESCRIPTION = characterStrings.TEXT; // 角色描述和其他文本 (从JSON读取)

    // --- 资源文件路径 --- (!!!重要!!! 确保这些文件实际存在于你的项目中!)
    // 提供一个占位符角色图片路径，如果需要静态图的话
    public static final String CHARACTER_IMG = "RadiationModResources/img/char/character.png"; 
    public static final String SHOULDER_1 = "RadiationModResources/img/char/shoulder1.png"; // 肩部小图1 (战斗界面左上角)
    public static final String SHOULDER_2 = "RadiationModResources/img/char/shoulder2.png"; // 肩部小图2 (角色信息界面)
    public static final String CORPSE = "RadiationModResources/img/char/corpse.png"; // 角色死亡图片
    // 角色选择界面按钮和肖像
    public static final String BUTTON = "RadiationModResources/img/char/Character_Button.png";
    public static final String PORTRAIT = "RadiationModResources/img/char/Character_Portrait.png";


    // --- 构造函数 ---
    public RadiationEngineer(String name, PlayerClass setClass) {
        // 使用直接提供纹理路径的构造函数，明确 model 和 animation 为 null
        super(name, setClass, 
            ORB_TEXTURES, // Orb texture paths array
            ORB_VFX_PATH, // Orb VFX path
            (String)null, // Explicitly cast null for model path
            (String)null); // Explicitly cast null for animation path
        
        // --- 角色初始化 ---
        initializeClass(CHARACTER_IMG, SHOULDER_1, SHOULDER_2, CORPSE,
                getLoadout(),
                20.0F, -10.0F, 220.0F, 290.0F,
                new EnergyManager(ENERGY_PER_TURN));
                
        // --- 对话框位置 --- (可选)
        // dialogX = (drawX + 0.0F * Settings.scale); // 对话框X坐标
        // dialogY = (drawY + 220.0F * Settings.scale); // 对话框Y坐标
    }

    // --- 初始卡组 ---
    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("RadiationMod:Strike_Rad"); // 使用卡牌中定义的完整ID
        retVal.add("RadiationMod:Strike_Rad");
        retVal.add("RadiationMod:Strike_Rad");
        retVal.add("RadiationMod:Strike_Rad");
        retVal.add("RadiationMod:Defend_Rad"); // 使用卡牌中定义的完整ID
        retVal.add("RadiationMod:Defend_Rad");
        retVal.add("RadiationMod:Defend_Rad");
        retVal.add("RadiationMod:Defend_Rad");
        retVal.add("RadiationMod:RadiationSpray"); // 使用卡牌中定义的完整ID
        retVal.add("RadiationMod:OverchargedSpray"); // 使用卡牌中定义的完整ID
        return retVal;
    }

    // --- 初始遗物 ---
    @Override
    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add("Burning Blood"); // 示例：使用一个原版遗物ID
        // retVal.add(MyCustomRelic.ID); // 在这里添加你的自定义初始遗物ID
        return retVal;
    }

    // --- 角色选择界面信息 ---
    @Override
    public CharSelectInfo getLoadout() {
        // 返回包含角色选择界面所有信息的对象
        return new CharSelectInfo(NAME, // 角色名称
                DESCRIPTION[0], // 角色描述文本 (来自JSON)
                STARTING_HP, // 初始生命值
                MAX_HP, // 最大生命值
                ORB_SLOTS, // 充能球槽位
                STARTING_GOLD, // 初始金币
                CARD_DRAW, // 每回合抽牌数
                this, // 角色对象本身
                getStartingRelics(), // 初始遗物列表
                getStartingDeck(), // 初始卡组列表
                false); // 是否为测试角色 (通常为false)
    }

    // --- 卡牌池与基础设置 ---
    @Override
    public String getTitle(PlayerClass playerClass) {
        // 返回角色在游戏中显示的职业名称
        return NAME;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        // 返回此角色使用的卡牌颜色枚举
        return CardColorEnum.RADIATION_GREEN;
    }

    @Override
    public Color getCardRenderColor() {
        // 返回卡牌渲染时的辉光颜色
        return Color.GREEN.cpy(); // 使用绿色副本
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        // 返回在某些事件中（如尼利事件）给予的卡牌
        // 尝试加载角色专属打击，如果失败则回退
        try {
             Class<?> strikeClass = Class.forName("radiationmod.cards.Strike_Rad");
             return (AbstractCard) strikeClass.newInstance();
         } catch (Exception e) {
             // 如果找不到Strike_Rad，返回通用的Strike卡牌
             return new radiationmod.cards.Strike(); 
         }
    }

    @Override
    public Color getCardTrailColor() {
        // 返回卡牌拖拽时的轨迹颜色
        return Color.GREEN.cpy(); // 使用绿色副本
    }

    @Override
    public int getAscensionMaxHPLoss() {
        // 返回每级进阶模式损失的最大生命值
        return 5;
    }

    // --- 其他角色方法 ---
    @Override
    public String getLocalizedCharacterName() {
        // 返回本地化的角色名称
        return NAME;
    }

    @Override
    public String getSpireHeartText() {
        // 返回与心脏战斗失败时的对话 (来自JSON)
        return characterStrings.TEXT[1];
    }

    @Override
    public String getVampireText() {
        // 返回吸血鬼事件中的对话 (来自JSON)
        return characterStrings.TEXT[2];
    }

    @Override
    public AbstractPlayer newInstance() {
        // 返回一个新的角色实例 (用于存档加载等)
        return new RadiationEngineer(this.name, this.chosenClass);
    }
    
    @Override
    public Color getSlashAttackColor() {
        // 返回普通攻击动画（劈砍）的颜色
        return Color.GREEN.cpy(); // 使用绿色副本
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        // 返回与心脏战斗时的特殊攻击效果
        return new AbstractGameAction.AttackEffect[] {
                AbstractGameAction.AttackEffect.POISON, // 示例：毒效果
                AbstractGameAction.AttackEffect.FIRE, // 示例：火效果
                AbstractGameAction.AttackEffect.SLASH_HEAVY // 示例：重劈效果
        };
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        // 返回用于显示能量数字的字体颜色
        return FontHelper.energyNumFontGreen; // 使用绿色能量字体
    }

    // --- 屏幕震动效果 --- (可选)
    @Override
    public void doCharSelectScreenSelectEffect() {
        // 在角色选择界面选中角色时的效果
        CardCrawlGame.sound.playA("ATTACK_FIRE", -0.4f); // 播放火焰攻击音效
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, true); // 轻微短时屏幕震动
    }
    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        // 在自定义模式中选择角色按钮的音效
         return "ATTACK_FIRE"; // 使用火焰攻击音效
    }

    // --- 移除能量球图像获取方法覆盖 --- 
    /*
    @Override
    public com.badlogic.gdx.graphics.Texture getEnergyImage() {
        return com.megacrit.cardcrawl.helpers.ImageMaster.loadImage("RadiationModResources/img/char/small_orb.png");
    }
    */

    // --- 移除无效的能量球覆盖方法 --- 
    /*
    @Override 
    public String getOrbTexturePath() { 
        return ORB_TEXTURE_PATH; 
    }
    
    @Override
    public String getOrbVfxTexturePath() {
        return ORB_VFX_TEXTURE_PATH;
    }
    */

} 