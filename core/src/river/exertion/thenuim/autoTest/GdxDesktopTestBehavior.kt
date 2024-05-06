import river.exertion.thenuim.automation.btree.behavior.ClickDisplayModeButtonBehavior

object GdxDesktopTestBehavior {

    fun testBehavior() {

        river.exertion.thenuim.automation.AutoUserTestHandler.currentTest =
            river.exertion.thenuim.automation.AutoUserTest()
        river.exertion.thenuim.automation.AutoUserTestHandler.currentTest.testBehaviors.add(ClickDisplayModeButtonBehavior())

    }
}