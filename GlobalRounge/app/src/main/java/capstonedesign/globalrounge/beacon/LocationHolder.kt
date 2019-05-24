package capstonedesign.globalrounge.beacon

data class LocationHolder(var lastX: Float, var x: Float) {
    fun calculate(): Float = x - lastX
}