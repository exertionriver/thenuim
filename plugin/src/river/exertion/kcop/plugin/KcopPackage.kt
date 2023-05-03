package river.exertion.kcop.plugin

import river.exertion.kcop.asset.IAsset

class KcopPackage {

    var assets : MutableList<IAsset> = mutableListOf()
    var plugins : MutableList<IPlugin> = mutableListOf()
    var config = KcopPackageConfig()
    var controller = KcopPackageController()
}