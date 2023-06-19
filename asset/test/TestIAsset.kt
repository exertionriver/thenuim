import kotlinx.serialization.Serializable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.AssetManagerHandler
import river.exertion.kcop.asset.AssetStatus
import river.exertion.kcop.asset.IAsset
import river.exertion.kcop.base.Id
import kotlin.random.Random

class TestIAsset : IAsset {

    @Serializable
    class TestIAssetData(var dataString : String = Id.randomId())

    var testIAssetData = TestIAssetData()
    override fun assetData() : Any = testIAssetData

    override fun assetId() : String = testIAssetData.dataString
    override fun assetName() : String = "testIAsset_name_${assetId()}"

    override fun assetTitle() : String = "testIAsset_title_${assetId()}"
    override fun assetPath() : String = newAssetFilename()

    override var assetStatus: AssetStatus? = null

    override var persisted: Boolean = false

    override fun newAssetFilename(): String = TestIAssets.iAssetPath(super.newAssetFilename())

    override fun assetInfo(): List<String> = listOf(assetId(), "test", "IAsset", "Info")

    override fun saveTyped(assetSaveLocation : String?) {
        persisted = AssetManagerHandler.saveAsset<TestIAssetData>(this, assetSaveLocation).persisted
    }

    @Test
    fun testFilename() {

        val specialCharFilename = ".@{}!\\`´\"^=()&[]$'~#%*:<>?/|, "
        val sanSpecialCharFilename = IAsset.sanitizeFilename(specialCharFilename)

        println ("special char filename:$specialCharFilename")
        println ("sanitized special char filename:$sanSpecialCharFilename")

        assertEquals("_", sanSpecialCharFilename)

        val specialCharAlphaNumFilename = "a.0sdf@9q{w}8e!rt\\z`7x´c\"v^uio=6p(h)j&k5[4l]v\$b'~#%3n*:<>?2/|,1 m"
        val sanSpecialCharAlphaNumFilename = IAsset.sanitizeFilename(specialCharAlphaNumFilename)

        println ("special char alphanum filename:$specialCharAlphaNumFilename")
        println ("sanitized special char alphanu, filename:$sanSpecialCharAlphaNumFilename")

        assertEquals("a_0sdf_9q_w_8e_rt_z_7x_c_v_uio_6p_h_j_k5_4l_v_b_3n_2_1_m", sanSpecialCharAlphaNumFilename)

        val newAssetFileName = "asdf!@#\$QW\nER%^&*zXcV(      )_+\tg"
        val randId = Random.nextInt(9999).toString().padStart(4, '0')

        val sanNewAssetFileName = IAsset.newAssetFilename(newAssetFileName, randId)

        println ("newAssetFileName:$newAssetFileName")
        println ("sanNewAssetFileName:$sanNewAssetFileName")

        assertEquals("asdf_QW_ER_zXcV_g_$randId", sanNewAssetFileName)
    }

    @Test
    fun testAssetInfoStr() {

        println("assetId: ${assetId()}")

        val assetInfo = assetInfo()
        assertEquals(4, assetInfo.size)
        assertEquals(assetInfo[0], assetId())

        val assetInfoStr = assetInfoStr()

        println("assetInfoStr: $assetInfoStr")
    }
}