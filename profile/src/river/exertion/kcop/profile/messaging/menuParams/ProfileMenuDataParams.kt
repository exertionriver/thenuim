package river.exertion.kcop.profile.messaging.menuParams

data class ProfileMenuDataParams(var profileAssetTitles: List<String>? = null,
                                 var selectedProfileAssetTitle: String? = null,
                                 var selectedProfileAssetInfo: List<String>? = null,
                                 var selectedProfileAssetName: String? = null)