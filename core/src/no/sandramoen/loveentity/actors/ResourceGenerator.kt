package no.sandramoen.loveentity.actors

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable

import no.sandramoen.loveentity.utils.BaseActor
import no.sandramoen.loveentity.utils.BaseGame
import no.sandramoen.loveentity.utils.GameUtils
import kotlin.math.ceil
import kotlin.math.pow

class ResourceGenerator(x: Float, y: Float, s: Stage,
                        name: String, baseCost: Long, multiplier: Float, income: Float, incomeTime: Float) : BaseActor(x, y, s) {
    private var table: Table
    private var resourceName: String = name
    private var nameLabel: Label

    private var selfWidth = Gdx.graphics.width *.95f// 600f
    private var selfHeight = 300f

    var love = 0f
    var collectLove = false
    private var activated = false
    private var purchased = false

    private var owned: Int = 0
    private var baseCost: Long = baseCost
    private var multiplier: Float = multiplier
    private var price: Float = baseCost * multiplier.pow(owned)
    private var income: Float = income
    private var incomeTime: Float = incomeTime
    private var time: Float = 0f

    private lateinit var activateButton: Button
    private lateinit var ownedLabel: Label
    private lateinit var timeLabel: Label
    private lateinit var timeProgress: BaseActor

    init {
        loadTexture("images/whitePixel.png")
        width = selfWidth
        height = selfHeight
        // color = Color(random(0, 255) / 255f, random(0, 255) / 255f, random(0, 255) / 255f, 1f)
        color = Color.DARK_GRAY

        nameLabel = Label(name, BaseGame.labelStyle)
        nameLabel.setFontScale(.75f)

        table = Table()
        table.width = selfWidth
        table.height = selfHeight

        table.add(nameLabel).top().colspan(3).row()
        table.add(leftTable(s)).pad(selfWidth*.01f)
        table.add(rightTable(s))

        addActor(table)

        /*table.debug()
        this.debug*/
    }

    override fun act(dt: Float) {
        super.act(dt)

        if (time >= incomeTime) {
            collectLove = true
            // activated = false
            activated = true
            time = 0f
            timeLabel.setText("0")
            timeProgress.width = 0f
        }

        if (activated) {
            time += dt
            timeLabel.setText("${incomeTime - time.toInt()}")
            timeProgress.width = (selfWidth * .68f) * (time / incomeTime)
        }
        timeProgress.setPosition(0f, timeProgress.y) // solves some weird displacement bug...
    }

    fun collectLove(): Float {
        if (collectLove) {
            collectLove = false
            return income * owned
        }
        return 0f
    }

    fun price(): Float {
        if (purchased) {
            purchased = false
            return baseCost * multiplier.pow(owned - 1)
        }
        return 0f
    }

    private fun leftTable(s: Stage): Table {
        val buttonStyle = Button.ButtonStyle()
        val buttonTex = Texture(Gdx.files.internal("images/whitePixel.png"))
        val buttonRegion = TextureRegion(buttonTex)
        buttonRegion.regionWidth = (selfWidth * .25f).toInt()
        buttonRegion.regionHeight = (selfHeight * .8f).toInt()
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        activateButton = Button(buttonStyle)
        activateButton.color = Color.PINK

        ownedLabel = Label("$owned", BaseGame.labelStyle)
        ownedLabel.setFontScale(.5f)
        ownedLabel.color = Color.YELLOW

        val levelProgress = BaseActor(0f, 0f, s)
        levelProgress.loadTexture("images/whitePixel.png")
        levelProgress.width = selfWidth * .25f
        levelProgress.height = selfHeight * .175f
        levelProgress.color = Color.FIREBRICK

        ownedLabel.setPosition((levelProgress.width / 2) - ownedLabel.width / 3, -levelProgress.height / 2) // weird offsets that just works...
        levelProgress.addActor(ownedLabel)

        activateButton.addActor(levelProgress)
        activateButton.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                if (owned > 0) {
                    activated = true
                }
            }
            false
        }
        return activateButton
    }

    private fun rightTable(s: Stage): Table {

        // progress
        timeProgress = BaseActor(0f, 0f, s)
        timeProgress.loadTexture("images/whitePixel.png")
        timeProgress.width = 0f
        timeProgress.height = selfHeight * .375f
        timeProgress.color = Color.GREEN

        // buy
        val buttonStyle = Button.ButtonStyle()
        val buttonTex = Texture(Gdx.files.internal("images/whitePixel.png"))
        val buttonRegion = TextureRegion(buttonTex)
        buttonRegion.regionWidth = (selfWidth * .5f).toInt()
        buttonRegion.regionHeight = (selfHeight * .375f).toInt()
        buttonStyle.up = TextureRegionDrawable(buttonRegion)

        val buyLabel = Label("Price: ${ceil(price).toInt()}", BaseGame.labelStyle)
        buyLabel.setFontScale(.5f)
        val buy = Button(buttonStyle)
        buy.addActor(buyLabel)
        buy.color = Color.ORANGE
        buy.addListener { e: Event ->
            if (GameUtils.isTouchDownEvent(e)) {
                timeProgress.setPosition(0f, timeProgress.y) // solves some weird displacement bug...
                if (love >= price) {
                    purchased = true
                    love -= price
                    owned++
                    ownedLabel.setText("$owned")
                    price = baseCost * multiplier.pow(owned)
                    buyLabel.setText("Price: ${ceil(price).toInt()}")
                }
            }
            false
        }

        // time
        timeLabel = Label("?", BaseGame.labelStyle)
        timeLabel.setFontScale(.5f)
        val time = BaseActor(0f, 0f, s)
        time.addActor(timeLabel)
        time.loadTexture("images/whitePixel.png")
        time.width = selfWidth * .167f
        time.height = selfHeight * .375f
        time.color = Color.LIGHT_GRAY

        val table = Table()
        table.add(timeProgress).colspan(2).pad(selfWidth*.01f).row()
        table.add(buy).pad(selfWidth*.01f)
        table.add(time).pad(selfWidth*.01f)
        return table
    }
}