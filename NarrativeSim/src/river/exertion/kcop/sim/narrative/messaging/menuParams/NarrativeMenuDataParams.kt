package river.exertion.kcop.sim.narrative.messaging.menuParams

data class NarrativeMenuDataParams(var narrativeAssetTitles: List<String>? = null,
                                   var selectedNarrativeAssetTitle: String? = null,
                                   var selectedNarrativeAssetInfo: List<String>? = null,
                                   var selectedNarrativeAssetName: String? = null)