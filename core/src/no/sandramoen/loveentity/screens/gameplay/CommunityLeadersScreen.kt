package no.sandramoen.loveentity.screens.gameplay

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.BaseScreen
import no.sandramoen.loveentity.utils.GameUtils

class CommunityLeadersScreen : BaseScreen() {
    private lateinit var titleImage: Image
    private lateinit var loveLabel: Label
    private lateinit var subtitleLabel: Label
    private lateinit var descriptionLabel: Label

    private lateinit var communityLeadersTable: Table

    private lateinit var infoTable: Table
    private lateinit var exitButton: Button

    override fun initialize() {
        if (BaseGame.english)
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerCommunityLeaders"))
        else
            titleImage = Image(BaseGame.textureAtlas!!.findRegion("bannerOrganisasjonsledere"))

        if (BaseGame.english)
            if (BaseGame.longScale)
                loveLabel = Label("${GameUtils.presentLongScale(BaseGame.love)} love", BaseGame.labelStyle)
            else
                loveLabel = Label("${GameUtils.presentShortScale(BaseGame.love)} love", BaseGame.labelStyle)
        else
            if (BaseGame.longScale)
                loveLabel = Label("${GameUtils.presentLongScale(BaseGame.love)} kjærlighet", BaseGame.labelStyle)
            else
                loveLabel = Label("${GameUtils.presentShortScale(BaseGame.love)} kjærlighet", BaseGame.labelStyle)
        loveLabel.setFontScale(.5f)

        val exitButtonStyle = Button.ButtonStyle()
        exitButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("cross-white")))
        exitButton = Button(exitButtonStyle)
        exitButton.isTransform = true
        exitButton.scaleBy(-.25f)
        exitButton.setOrigin(Align.top)
        exitButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (!BaseGame.muteAudio) BaseGame.clickSound!!.play(.25f)
                BaseGame.setActiveScreen(LevelScreen())
            }
            false
        }

        // info table
        val infoLabel = Label("", BaseGame.labelStyle)
        if (BaseGame.english)
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
        else
            infoLabel.setText("Cupcake ipsum dolor sit amet. Gingerbread marshmallow sugar plum pastry dragée gingerbread candy cookie. Bonbon dessert tiramisu dragée.\n" +
                    "\n" + "Powder jelly-o lollipop. Cookie chupa chups powder cake muffin pudding. Soufflé cupcake chocolate apple pie danish toffee dessert powder. Cake pudding jelly cake jelly tootsie roll.")
        infoLabel.color = Color.PURPLE
        infoLabel.setWrap(true)
        infoLabel.setFontScale(.3f)

        infoTable = Table()
        val whiteInfoTable = Table()
        whiteInfoTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(1f, 1f, 1f, 1f))
        whiteInfoTable.add(infoLabel).expand().fill().pad(20f)
        infoTable.add(whiteInfoTable).width(Gdx.graphics.width * .5f)//.height(Gdx.graphics.height * .3f)
        infoTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(0f, 0f, 0f, .9f))
        infoTable.isVisible = false
        infoTable.addListener(object : ActorGestureListener() {
            override fun tap(event: InputEvent?, x: Float, y: Float, count: Int, button: Int) {
                infoTable.isVisible = !infoTable.isVisible
            }
        })
        // infoTable.debug = true

        val infoButtonStyle = Button.ButtonStyle()
        infoButtonStyle.up = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("question")))
        val infoButton = Button(infoButtonStyle)
        infoButton.isTransform = true
        infoButton.scaleBy(.2f)
        infoButton.setOrigin(Align.center)
        infoButton.color = Color(95 / 255f, 152 / 255f, 209 / 255f, 1f) // grey blue
        infoButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e))
                infoTable.isVisible = !infoTable.isVisible
            false
        }

        // upper table
        if (BaseGame.english)
            subtitleLabel = Label("Community leaders make compassion easier", BaseGame.labelStyle)
        else
            subtitleLabel = Label("Organisasjonsledere gjør medfølelse enklere", BaseGame.labelStyle)
        subtitleLabel.setFontScale(.3f)
        if (BaseGame.english)
            descriptionLabel = Label("Recruit one to act as a continuous love hub for you when you're away!", BaseGame.labelStyle)
        else
            descriptionLabel = Label("Rekrutter for å elske kontinuerlig når du er borte!", BaseGame.labelStyle)
        descriptionLabel.setFontScale(.25f)

        val upperTable = Table()
        // upperTable.add(titleLabel).expandX().center().row()
        upperTable.add(titleImage).width(Gdx.graphics.width * .95f).height(Gdx.graphics.height * .1f).row()
        upperTable.add(loveLabel).row()
        upperTable.add(subtitleLabel).row()
        upperTable.add(descriptionLabel)
        // upperTable.add(infoButton).width(Gdx.graphics.width * .06f).height(Gdx.graphics.width * .06f).row()
        // upperTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.2f, .2f, .2f, 1f))

        val table = Table()
        table.setFillParent(true)
        table.add(exitButton).top().right().pad(Gdx.graphics.width * .01f)
        uiTable.add(table)

        // community leaders table
        communityLeadersTable = Table()
        initializeCommunityLeaders()

        val scroll = ScrollPane(communityLeadersTable)

        val scrollableTable = Table()
        scrollableTable.add(scroll).padTop(Gdx.graphics.height * .015f)

        // table layout
        val mainTable = Table()

        mainTable.add(upperTable).fillX()
        mainTable.row()
        mainTable.add(scrollableTable)

        val stack = Stack() // stack allows for scene2d elements to overlap each other
        stack.add(mainTable)
        stack.add(infoTable)
        stack.setFillParent(true)
        mainTable.align(Align.top)

        mainStage.addActor(stack)

        /*stack.debug = true
        mainTable.debug = true*/

        GameUtils.fadeOut(uiStage)
    }

    override fun update(dt: Float) {
        if (BaseGame.english)
            if (BaseGame.longScale)
                loveLabel.setText("${GameUtils.presentLongScale(BaseGame.love)} love")
            else
                loveLabel.setText("${GameUtils.presentShortScale(BaseGame.love)} love")
        else
            if (BaseGame.longScale)
                loveLabel.setText("${GameUtils.presentLongScale(BaseGame.love)} kjærlighet")
            else
                loveLabel.setText("${GameUtils.presentShortScale(BaseGame.love)} kjærlighet")

        for (i in 0 until BaseGame.communityLeaders.size) {
            if (BaseGame.communityLeaders[i].remove) {
                BaseGame.communityLeaders[i].remove()
                initializeCommunityLeaders()
                break
            }
        }

        for (generator in BaseGame.resourceGenerators)
            generator.act(Gdx.graphics.deltaTime)

        if (infoTable.isVisible) {
            exitButton.touchable = Touchable.disabled
            for (leader in BaseGame.communityLeaders)
                leader.touchable = Touchable.disabled
        } else {
            exitButton.touchable = Touchable.enabled
            for (leader in BaseGame.communityLeaders)
                leader.touchable = Touchable.enabled
        }
    }

    override fun keyDown(keycode: Int): Boolean {
        if (keycode == Input.Keys.BACK)
            BaseGame.setActiveScreen(LevelScreen())
        return false;
    }

    private fun initializeCommunityLeaders() {
        communityLeadersTable.reset()
        communityLeadersTable.background = TextureRegionDrawable(TextureRegion(BaseGame.textureAtlas!!.findRegion("whitePixel"))).tint(Color(.05f, .05f, .05f, 1f))

        for (i in 0 until BaseGame.resourceGenerators.size) { // assumes BaseGame.resourceGenerators.size == communityLeaders.size
            if (BaseGame.resourceGenerators[i].isVisible &&
                    !BaseGame.communityLeaders[i].remove &&
                    !BaseGame.resourceGenerators[i].hasCommunityLeader) {

                BaseGame.communityLeaders[i].isVisible = true
                BaseGame.communityLeaders[i].checkAffordable()
                communityLeadersTable.add(BaseGame.communityLeaders[i]).padBottom(Gdx.graphics.height * .03f).row()

                if (!BaseGame.resourceGenerators[i].hideTable.isVisible)
                    BaseGame.communityLeaders[i].hideTable.isVisible = false
            }
        }
    }
}
