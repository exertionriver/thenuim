package river.exertion.kcop.assets

class KcopPackage {

    var assets : MutableList<IAsset> = mutableListOf()
    var plugins : MutableList<IPlugin> = mutableListOf()
    var config = KcopPackageConfig()
    var controller = KcopPackageController()
}