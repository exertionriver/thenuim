package river.exertion.kcop.sim.menu.params

data class ProfileMenuDataParams(var profileAssetTitles: List<String>? = null,
                                 var selectedProfileAssetTitle: String? = null,
                                 var selectedProfileAssetInfo: List<String>? = null,
                                 var selectedProfileAssetName: String? = null)