package river.exertion.thenuim.view.klop

import river.exertion.thenuim.view.layout.displayViewLayout.DVLayoutHandler

interface IDisplayViewLayoutHandler {

    fun build() { DVLayoutHandler.build() }
    fun clearContent() { DVLayoutHandler.clearContent() }
    //to do: add default mode, ie., KCOP-layout centric v. fullscreen-centric
}