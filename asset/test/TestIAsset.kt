import org.junit.jupiter.api.Test
import river.exertion.kcop.asset.IAsset
import kotlin.random.Random

class TestIAsset {

    @Test
    fun testFilename() {

        val specialCharFilename = ".@{}!\\`´\"^=()&[]$'~#%*:<>?/|, "
        val sanSpecialCharFilename = IAsset.sanitizeFilename(specialCharFilename)

        println ("special char filename:$specialCharFilename")
        println ("sanitized special char filename:$sanSpecialCharFilename")

        assert(sanSpecialCharFilename == "_")

        val specialCharAlphaNumFilename = "a.0sdf@9q{w}8e!rt\\z`7x´c\"v^uio=6p(h)j&k5[4l]v\$b'~#%3n*:<>?2/|,1 m"
        val sanSpecialCharAlphaNumFilename = IAsset.sanitizeFilename(specialCharAlphaNumFilename)

        println ("special char alphanum filename:$specialCharAlphaNumFilename")
        println ("sanitized special char alphanu, filename:$sanSpecialCharAlphaNumFilename")

        assert(sanSpecialCharAlphaNumFilename == "a_0sdf_9q_w_8e_rt_z_7x_c_v_uio_6p_h_j_k5_4l_v_b_3n_2_1_m")

        val newAssetFileName = "asdf!@#\$QW\nER%^&*zXcV(      )_+\tg"
        val randId = Random.nextInt(9999).toString().padStart(4, '0')

        val sanNewAssetFileName = IAsset.newAssetFilename(newAssetFileName, randId)

        println ("newAssetFileName:$newAssetFileName")
        println ("sanNewAssetFileName:$sanNewAssetFileName")

        assert(sanNewAssetFileName == "asdf_QW_ER_zXcV_g_$randId")
    }
}