package thePackmaster.cards.weaponspack;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import thePackmaster.actions.weaponspack.EquipAction;
import thePackmaster.orbs.weaponspack.SwordOfChaosOrb;

import static thePackmaster.SpireAnniversary5Mod.makeID;

public class SwordOfChaos extends AbstractWeaponsPackCard {

    public static final String ID = makeID("SwordOfChaos");
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    private static final int COST = 0;
    private static final int WEAPON_ATTACK = 3;
    private static final int WEAPON_DURABILITY = 3;
    private static final int UPGRADE_PLUS_WEAPON_DURABILITY = 1;

    public SwordOfChaos() {
        super(ID, COST, CardType.SKILL, CardRarity.SPECIAL, CardTarget.SELF);

        this.baseMagicNumber = this.magicNumber = WEAPON_DURABILITY;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new EquipAction(new SwordOfChaosOrb(WEAPON_ATTACK, this.magicNumber, false)));
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_PLUS_WEAPON_DURABILITY);
        }
    }

}
